package suman.dev.strocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import suman.dev.strocks.Adapters.HolidayAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.Holiday;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonArrayCallback;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 22/7/17.
 */

public class HolidayListActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Holiday> holidays= new ArrayList<>();
    private Holiday holiday;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HolidayAdapter adapter;

    private TextView content_title;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);
        setupWindowAnimations();
        UserProfileHeader  profile =  new UserProfileHeader(this,findViewById(R.id.holiday_list_userProfileHeader));
        profile.loadProfile();
        loadHoliday();;

        TextView content_title = (TextView)findViewById(R.id.content_toolbar_title);
        content_title.setText("Holiday");
        content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.holiday_black, 0, 0, 0);


    }

    @Override
    public void onClick(View v) {

    }


    private void loadHoliday()
    {
        VolleyService service = new VolleyService();
        service.MakeGetRequest(Const.GET_HOLIDAY_LIST+ UserProfile.SessionData.Id, this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray jsonArray = result.getJSONArray("data");
                    for (int i = 0; i < result.length(); i++) {
                        holiday = new Holiday();
                        holiday.Id = jsonArray.getJSONObject(i).getInt("id");
                        holiday.Session_Id = jsonArray.getJSONObject(i).getInt("session_id");
                        holiday.Name = jsonArray.getJSONObject(i).getString("name");
                        holiday.Month = jsonArray.getJSONObject(i).getString("month");
                        holiday.Start_Date = jsonArray.getJSONObject(i).getString("start_date");
                        holiday.End_Date = jsonArray.getJSONObject(i).getString("end_date");
                        holiday.Comment = jsonArray.getJSONObject(i).getString("comment");
                        holiday.Total_Days = jsonArray.getJSONObject(i).getInt("total_days");
                        holidays.add(holiday);
                    }
                } catch (Exception e) {
                    FirebaseCrash.report(e);
                    e.getStackTrace();
                }



                if (holidays.size() > 0) {
                    setAdapter();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(HolidayListActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setAdapter()
    {
        recyclerView = (RecyclerView) findViewById(R.id.holiday_list_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HolidayAdapter(holidays);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

}
