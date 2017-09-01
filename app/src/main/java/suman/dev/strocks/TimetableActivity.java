package suman.dev.strocks;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import suman.dev.strocks.Adapters.TabFragmentAdapter;
import suman.dev.strocks.Adapters.TimeTableAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.TimeTable;
import suman.dev.strocks.Model.TimeTableDetail;
import suman.dev.strocks.Model.TimeTableDetailModel;
import suman.dev.strocks.Model.Timetable;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

public class TimetableActivity extends AppCompatActivity {

    private ArrayList<TimeTable> timeTable = new ArrayList<>();
    private ArrayList<TimeTable> timeTable1 = new ArrayList<>();
    private ArrayList<TimeTable> finalTable = new ArrayList<>();

    private TimeTable table;
    private ArrayList<TimeTableDetail> details;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TimeTableAdapter adapter;

    private ArrayList<String> days= new ArrayList<>();
    private Gson gson = new Gson();

    private final VolleyService service= new VolleyService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        addTime();
        //getTimetable();
    }

    private void addTabs(){
        try {
            final ViewPager pager = (ViewPager) findViewById(R.id.pager);
            TabFragmentAdapter pagerAdapter = new TabFragmentAdapter(getSupportFragmentManager());
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

            for (TimeTable t : finalTable) {
                Bundle bundle = new Bundle();
                String json = gson.toJson(t.timetable);
                bundle.putString("day", t.Day);
                bundle.putString("timeTableDetail", json);
                TimeTableFragment f = new TimeTableFragment();
                f.setArguments(bundle);

                pagerAdapter.addFrag(f, t.Day);
                tabLayout.addTab(tabLayout.newTab().setText(t.Day));
            }

            pager.setAdapter(pagerAdapter);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setupWithViewPager(pager);

        }
        catch (Exception e){
            e.getStackTrace();
        }
    }
    private void getTimetable(){
        service.MakeGetRequest(String.format(Const.GET_TIMETABLE, UserProfile.SessionData.Id, UserProfile.ClassData.Id), this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try{
                    JSONArray arr = result.getJSONArray("data");
                    for (int i=0;i<arr.length();i++){
                        String day = arr.getJSONObject(i).getString("day");
                        if(day.length()>3)
                            day = day.substring(0,3);

                            table = new TimeTable(
                                    arr.getJSONObject(i).getJSONObject("subject").getString("name"),
                                    "Pankaj Kumar",
                                    day,
                                    arr.getJSONObject(i).getString("start_time"),
                                    arr.getJSONObject(i).getString("end_time")
                            );
                        timeTable.add(table);
                    }

                    getDays();
                    filterAndMigrate();
                    addTabs();
                }catch (Exception e){
                    Toast.makeText(TimetableActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void addTime()
    {
        try {
            table = new TimeTable("Math","","Mon", "13:00", "14:00");
            timeTable.add(table);

            table = new TimeTable("Math","","Tue", "07:00", "08:00");
            timeTable.add(table);

            table = new TimeTable("English","","Wed", "12:00", "13:00");
            timeTable.add(table);

            table = new TimeTable("English","","Thu", "09:00", "10:00");
            timeTable.add(table);

            table = new TimeTable("Hindi","","Fri", "11:00", "12:00");
            timeTable.add(table);

            table = new TimeTable("Math","","Sat", "11:00", "12:00");
            timeTable.add(table);

            table = new TimeTable("Hindi","","Mon", "07:00", "08:00");
            timeTable.add(table);


//            Collections.sort(timeTable, new Comparator<TimeTable>() {
//                @Override
//                public int compare(TimeTable o1, TimeTable o2) {
//                    return o1.StartDateTime.before(o2.StartDateTime) ? -1 : o1.StartDateTime.after(o2.StartDateTime) ? 0 : 1;
//                }
//            });

            getDays();
            filterAndMigrate();
            addTabs();
            //setAdapter();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    private void filterAndMigrate(){
        for (String day: days){
            for (TimeTable t :timeTable) {
                if (day.equals(t.Day))
                    timeTable1.add(t);
            }

            Collections.sort(timeTable1, new Comparator<TimeTable>() {
                @Override
                public int compare(TimeTable o1, TimeTable o2) {
                    return o1.StartDateTime.before(o2.StartDateTime) ? -1 : o1.StartDateTime.after(o2.StartDateTime) ? 0 : 1;
                }
            });

            details = new ArrayList<>();
            for (TimeTable t1: timeTable1){
                details.add(new TimeTableDetail("","",t1.StartTime, t1.EndTime));
            }

            Collections.sort(details, new Comparator<TimeTableDetail>() {
                @Override
                public int compare(TimeTableDetail o1, TimeTableDetail o2) {
                    return o1.StartDateTime.before(o2.StartDateTime) ? -1 : o1.StartDateTime.after(o2.StartDateTime) ? 0 : 1;
                }
            });

            table= new TimeTable("","",day,"","");
            table.index =finalTable.size()+1;
            table.Details.addAll(details);
            for(TimeTableDetail d : details){
                TimeTableDetailModel m = new TimeTableDetailModel();
                m.StartTime = d.StartTime;
                m.EndTime = d.EndTime;
                m.TeacherName = d.TeacherName;
                m.Subject= d.Subject;
                table.timetable.add(m);
            }
            finalTable.add(table);
        }
    }

    private void getDays()
    {
        for(TimeTable t : timeTable){
            if(checkExistingDay(t.Day))
                days.add(t.Day);
        }
    }
    private boolean checkExistingDay(String day){
        boolean flag = true;
        for(String s : days)
            if(s.equals(day))
                flag= false;
        return flag;
    }
}
