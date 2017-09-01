package suman.dev.strocks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import suman.dev.strocks.Adapters.SyllabusAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.Syllabus;
import suman.dev.strocks.Model.SyllabusTopic;
import suman.dev.strocks.WebService.SyllabusCallback;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 29/7/17.
 */

public class SyllabusDetailActivity extends AppCompatActivity {
    private String SubjectId="";
    private VolleyService service = new VolleyService();
    private ArrayList<Syllabus> syllabuses;
    private Syllabus syllabus;
    private View loading_view;
    private ListView listView;
    private SyllabusAdapter adapter;

    private TextView content_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus_detail);

        UserProfileHeader profile = new UserProfileHeader(this, findViewById(R.id.userProfileHeader));
        profile.loadProfile();
        loading_view = findViewById(R.id.loading_view);
        SubjectId = getIntent().getStringExtra("SubjectId");
        listView = (ListView)findViewById(R.id.listView);

        content_title = (TextView)findViewById(R.id.content_toolbar_title);
        content_title.setText("Syllabus Topic");
        content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.syllabus_black, 0, 0, 0);

        loadSyllabus();
    }

    private void loadSyllabus()
    {
        service.MakeGetRequest(String.format(Const.GET_STUDENT_SYLLABUS, SubjectId), this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.getJSONArray("data") != null && result.getJSONArray("data").length()>0) {
                        syllabuses = new ArrayList<Syllabus>();
                        JSONObject data  = result.getJSONArray("data").getJSONObject(0);
                        String SubjectName = data.getString("name");
                        JSONArray s = data.getJSONArray("syllabus");
                        for (int i=0;i<s.length();i++) {
                            syllabus = new Syllabus(
                                    s.getJSONObject(i).getInt("id"),
                                    s.getJSONObject(i).getInt("subject_id"),
                                    SubjectName,
                                    s.getJSONObject(i).getString("name"),
                                    null
                            );
                            syllabuses.add(syllabus);
                        }

                        loading_view.setVisibility(View.GONE);
                        setAdapter();
                    }
                }
                catch (Exception e)
                {
                    FirebaseCrash.report(e);
                    e.getStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(SyllabusDetailActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setAdapter()
    {
        adapter = new SyllabusAdapter(syllabuses, this);
        listView.setAdapter(adapter);
    }

    private void loadSyllabusTopic(Syllabus syllabus)
    {
        service.LoadSyllabus(String.format(Const.GET_STUDENT_SYLLABUS_TOPIC, syllabus.Id), this, new SyllabusCallback() {
            @Override
            public void onSuccess(JSONObject result, Syllabus syllabus) {

                try
                {
                    if(result.getJSONArray("data")!=null && result.getJSONArray("data").length()>0) {
                        JSONArray topics = result.getJSONArray("data").getJSONObject(0).getJSONArray("syllabus_topic");
                        syllabus.SyllabusTopic = new ArrayList<SyllabusTopic>();
                        for (int i=0;i<topics.length();i++) {
                           JSONObject json=  topics.getJSONObject(i);
                            syllabus.SyllabusTopic.add(new SyllabusTopic(
                                    json.getInt("id"),
                                    json.getString("name")
                            ));
                        }
                        syllabuses.add(syllabus);
                    }
                }
                catch (Exception e)
                {
                    FirebaseCrash.report(e);
                    e.getStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                FirebaseCrash.report(error);
            }
        }, syllabus);
    }


}
