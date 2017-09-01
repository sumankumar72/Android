package suman.dev.strocks;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import suman.dev.strocks.Adapters.ResultAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.Semester;
import suman.dev.strocks.Model.StudentResult;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 23/7/17.
 */

public class ResultActivity extends AppCompatActivity {

    private VolleyService service = new VolleyService();
    private ArrayList<StudentResult> results;
    private ArrayList<Semester> semesters= new ArrayList<Semester>();
    private Semester semester;

    private ListView listView;
    private ResultAdapter adapter;
    private Spinner ddlSemester;

    private Context context;
    private String selectedClassId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        UserProfileHeader profile = new UserProfileHeader(this, findViewById(R.id.result_userProfileHeader));
        profile.loadProfile();
        context = this;
        listView = (ListView) findViewById(R.id.listView);

        TextView content_title = (TextView) findViewById(R.id.content_toolbar_title);
        content_title.setText("Result");
        content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.syllabus_black, 0, 0, 0);
        loadSemester();
        ddlSemester = (Spinner) findViewById(R.id.ddlSemester);

        selectedClassId = getIntent().getStringExtra("classId");

        ddlSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadResult(semesters.get(position).SemesterId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setAdapter()
    {
        if(results==null)
            return;
        adapter = new ResultAdapter(results, getApplicationContext());
        listView.setAdapter(adapter);
    }
    private void loadSemester()
    {
        service.MakeGetRequest(Const.GET_SEMESTER, this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result!=null)
                {
                    try{
                        JSONArray array = result.getJSONArray("data");
                        for (int i=0;i<array.length();i++){
                            semester= new Semester();
                            semester.SemesterId = array.getJSONObject(i).getInt("id");
                            semester.SemesterName = array.getJSONObject(i).getString("semester_name");
                            semesters.add(semester);
                        }
                        ArrayAdapter<Semester> spinerAdapter = new ArrayAdapter<Semester>(context, R.layout.support_simple_spinner_dropdown_item, semesters);
                        ddlSemester.setAdapter(spinerAdapter);

                    }catch (Exception e)
                    {
                        e.getStackTrace();
                        FirebaseCrash.report(e);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(ResultActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadResult(int SemesterId)
    {
        service.MakeGetRequest(String.format(Const.GET_STUDENT_RESULT, UserProfile.UserToken, SemesterId), this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.getJSONArray("data") != null) {
                        results = new ArrayList<StudentResult>();
                        JSONArray array = result.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            results.add(new StudentResult(
                                    json.getInt("id"),
                                    0,
                                    json.getInt("full_marks"),
                                    json.getInt("marks_obtained"),
                                    json.getString("grade"),
                                    json.getString("review"),
                                    json.getString("subject_name")
                            ));
                        }
                        setAdapter();
                    }
                }
                catch (Exception e)
                {
                    e.getStackTrace();
                    FirebaseCrash.report(e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                FirebaseCrash.report(error);
                Toast.makeText(ResultActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
