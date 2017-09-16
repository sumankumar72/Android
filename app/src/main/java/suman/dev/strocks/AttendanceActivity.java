package suman.dev.strocks;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import suman.dev.strocks.Adapters.AttandanceAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.ChildModel;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.StudentAttendance;
import suman.dev.strocks.Model.UserClassData;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonArrayCallback;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 27/7/17.
 */

public class AttendanceActivity extends AppCompatActivity {

    private PieChart chart;
    private TextView present, absent;
    private VolleyService service = new VolleyService();
    private StudentAttendance attendance;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AttandanceAdapter adapter;
    private ArrayList<ChildModel> students;
    private String classId="1";
    private String selectedDate="";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        UserProfileHeader profile = new UserProfileHeader(this, findViewById(R.id.result_userProfileHeader));
        profile.loadProfile();

        present = (TextView)findViewById(R.id.present);
        absent = (TextView)findViewById(R.id.absent);

        TextView content_title = (TextView)findViewById(R.id.content_toolbar_title);
        content_title.setText("Attendance");
        content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_queue_black, 0, 0, 0);
        if(!UserProfile.Role.toLowerCase().equals("teacher")) {
            loadAttendance();
        }
        else
        {
            LinearLayout attandanceGraph = (LinearLayout)findViewById(R.id.attandanceGraph);
            attandanceGraph.setVisibility(View.GONE);
            RelativeLayout viewStudenList  = (RelativeLayout) findViewById(R.id.viewStudenList);
            viewStudenList.setVisibility(View.VISIBLE);

            final Button btnPostAttendance =(Button)findViewById(R.id.btnPostAttendance);
            final TextView lblDate = (TextView)findViewById(R.id.lblDate);
            selectedDate=sdf.format(new Date());
            lblDate.setText("Date: "+ selectedDate);
            loadStudents();
            final Calendar calendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    selectedDate=sdf.format(calendar.getTime());
                    lblDate.setText("Date: "+ selectedDate);
                    loadStudents();
                    btnPostAttendance.setEnabled(selectedDate.toLowerCase().equals(sdf.format(new Date()).toLowerCase()));
                }
            };


            final DatePickerDialog datePicker = new DatePickerDialog(AttendanceActivity.this, date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

            lblDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePicker.show();
                }
            });



            recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
            btnPostAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postAttendance();
                }
            });
        }
    }

    private void postAttendance(){
        if(students==null || students.size()<=0)
        {
            Toast.makeText(this, "Attendance not avaliable!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> present= new ArrayList<>();
        ArrayList<String> absent = new ArrayList<>();
        for(ChildModel c: students){
            if(c.IsPresent)
                present.add(c.UserToken); //presentStudent=presentStudent+","+c.UserToken;
            else
               absent.add(c.UserToken); //absentStudent = absentStudent+","+c.UserToken;
        }

        JSONObject params = new JSONObject();

        JSONArray jsArray = new JSONArray(present);
        JSONArray jsAbsent = new JSONArray(absent);
        try {
            params.put("class_id", classId);
            params.put("session_id", UserProfile.SessionData.Id + "");
            params.put("teacher_id", UserProfile.UserToken);
            params.put("present_student_id", jsArray);
            params.put("absent_student_id", jsAbsent);
        }
        catch (Exception e){

        }

        service.MakePostRequest(Const.CREATE_STUDENT_ATTENDANCE, params, AttendanceActivity.this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(AttendanceActivity.this, "Attendance posted successfully!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(AttendanceActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void processPieChart() {
        chart = (PieChart) findViewById(R.id.result_pieChart);
        chart.setDrawHoleEnabled(false);
        chart.setDescription("");
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(attendance.Absent, 0));
        entries.add(new Entry(attendance.Present, 1));
        PieDataSet dataSet = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(44, 88, 195));
        colors.add(Color.rgb(19, 200, 40));

        dataSet.setColors(colors);
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Present");
        xVals.add("Absent");
        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new PercentFormatter());
        chart.setData(data);
        chart.invalidate();

        present.setText("Present "+attendance.Present);
        absent.setText("Absent "+attendance.Absent);

    }

    //This function execute when user loggedin as Student or Parent Role
    private void loadAttendance()
    {
        attendance = new StudentAttendance();
        String monthname=(String)android.text.format.DateFormat.format("MMMM", new Date());
        String year = (String)android.text.format.DateFormat.format("yyyy", new Date());
        String endPoint = String.format(Const.GET_STUDENT_ATTENDANCE, UserProfile.SessionData.Id,monthname, year, UserProfile.UserToken,UserProfile.SessionData.Id,UserProfile.ClassData.Id);

        service.MakeGetRequest(endPoint, this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result!=null)
                {
                    try {
                        if(result.getJSONObject("data")!=null)
                        {
                            JSONObject json = result.getJSONObject("data");
                            attendance.Present= json.getInt("x");
                            attendance.Absent = json.getInt("y");
                            attendance.TotalDays = json.getInt("totalDays");
                            processPieChart();
                        }
                    }
                    catch (Exception e)
                    {
                        FirebaseCrash.report(e);
                        e.getStackTrace();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(AttendanceActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //This function execute when user loggedin as Teacher Role
    private void loadStudents(){
        students= new ArrayList<>();
        if(!selectedDate.equals(sdf.format(new Date()))) {
            service.MakeGetRequest(String.format(Const.GET_STUDENTS_FOR_ATTENDANCE, classId, UserProfile.UserToken, selectedDate), AttendanceActivity.this, new VolleyJsonObjectCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        JSONArray array = result.getJSONArray("data");
                        ChildModel student;
                        for (int i = 0; i < array.length(); i++) {
                            student = new ChildModel(array.getJSONObject(i).getString("student_id"), array.getJSONObject(i).getString("student_name"), "", "");
                            if (array.getJSONObject(i).getString("attendance").toLowerCase().equals("present"))
                                student.IsPresent = true;
                            else
                                student.IsPresent = false;

                            students.add(student);

                        }
                        setAdapter(students);
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    String s = "";
                }
            });
        }
        else{
            service.MakeGetRequest(String.format(Const.GET_STUDENT_BY_CLASS, UserProfile.SessionData.Id, classId), AttendanceActivity.this, new VolleyJsonObjectCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try{
                        JSONArray array = result.getJSONArray("data");
                        for(int i=0;i<array.length();i++){
                            students.add(new ChildModel(
                                    array.getJSONObject(i).getJSONObject("student").getString("user_token"),
                                    array.getJSONObject(i).getJSONObject("student").getString("first_name"),
                                    array.getJSONObject(i).getJSONObject("student").getString("middle_name"),
                                    array.getJSONObject(i).getJSONObject("student").getString("last_name")
                            ));
                        }
                        setAdapter(students);
                    }
                    catch (Exception e){
                        e.getStackTrace();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    String s = "";
                }
            });
        }
    }

    private void setAdapter(final ArrayList<ChildModel> students)
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AttandanceAdapter(students);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(view.getId()==R.id.rdbPresentAbsent){
                    RadioGroup rdb = (RadioGroup)view.findViewById(R.id.rdbPresentAbsent);
                    int selectedId = rdb.getCheckedRadioButtonId();
                }
                else if(view.getId()==R.id.rdbPresent){
                    students.get(position).IsPresent=true;
                }
                else if (view.getId()==R.id.rdbAbsent){
                    students.get(position).IsPresent=false;
                }
            }
        });
    }

}