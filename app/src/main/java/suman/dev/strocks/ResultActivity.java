package suman.dev.strocks;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import suman.dev.strocks.Adapters.AttandanceAdapter;
import suman.dev.strocks.Adapters.ResultAdapter;
import suman.dev.strocks.Adapters.ResultPostAdapter;
import suman.dev.strocks.Adapters.SwitchUserAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.ChildModel;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.Semester;
import suman.dev.strocks.Model.StudentResult;
import suman.dev.strocks.Model.UserClassData;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.Model.UserSubjectData;
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

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AttandanceAdapter resultAdapter;
    private ArrayList<ChildModel> students;
    private String classId;

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


        if(UserProfile.Role.toLowerCase().equals("teacher")) {
            initLayoutForTeacher();
        }
        else
        {
            initLayoutForStudent();
        }

    }
    private void initLayoutForTeacher(){
        LinearLayout layoutForStudent = (LinearLayout)findViewById(R.id.layoutForStudent);
        layoutForStudent.setVisibility(View.GONE);

        LinearLayout layoutForTeacher = (LinearLayout)findViewById(R.id.layoutForTeacher);
        layoutForTeacher.setVisibility(View.VISIBLE);

        Spinner ddlClass =(Spinner)findViewById(R.id.ddlClass);
        ArrayAdapter<UserClassData> spinerAdapter = new ArrayAdapter<UserClassData>(this, R.layout.support_simple_spinner_dropdown_item, UserProfile.Classes);
        ddlClass.setAdapter(spinerAdapter);

        try {
            ddlClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    classId= UserProfile.Classes.get(position).Id + "";
                    loadStudents();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (Exception e){

        }

    }

    //This function execute when user loggedin as Teacher Role
    private void loadStudents(){
        students= new ArrayList<>();
        service.MakeGetRequest(String.format(Const.GET_STUDENT_BY_CLASS, UserProfile.SessionData.Id, classId), ResultActivity.this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try{
                    ChildModel child ;
                    JSONArray array = result.getJSONArray("data");
                    for(int i=0;i<array.length();i++){
                        JSONObject studentJson =array.getJSONObject(i).getJSONObject("student");
                       child =  new ChildModel(
                               studentJson.getString("user_token"),
                               studentJson.getString("first_name"),
                               studentJson.getString("middle_name"),
                               studentJson.getString("last_name")
                        );
                        JSONArray subjects = studentJson.getJSONArray("student_class_subject_data");
                        UserSubjectData subjectData;
                        for(int j=0;j<subjects.length();j++){
                            subjectData=new UserSubjectData();
                            subjectData.Id = subjects.getJSONObject(j).getInt("id");
                            subjectData.Name = subjects.getJSONObject(j).getString("name");
                            boolean flag= false;
                            for(UserSubjectData d: UserProfile.TeacherSubjects){
                                if(d.is_class_teacher.equals("Yes") && d.Id==subjectData.Id ){
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag)
                                child.Subjects.add(subjectData);
                        }
                        students.add(child);
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

    private void setAdapter(final ArrayList<ChildModel> students)
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        resultAdapter = new AttandanceAdapter(students, true);
        recyclerView.setAdapter(resultAdapter);

        resultAdapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                createResultDialog(students.get(position).Subjects);
            }
        });
    }

    private void initLayoutForStudent(){
        loadSemester();
        ddlSemester = (Spinner) findViewById(R.id.ddlSemester);

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
    private void createResultDialog(ArrayList<UserSubjectData> dataSet){
        try {
            ResultPostAdapter postAdapter= new ResultPostAdapter(dataSet);
            RecyclerView.LayoutManager postResultLayoutManager= new LinearLayoutManager(this);

            final Dialog dialog = new Dialog(ResultActivity.this);
            dialog.setContentView(R.layout.create_result_dialog);
            RecyclerView postResultRecyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerview);
            postResultRecyclerView.setLayoutManager(postResultLayoutManager);
            postResultRecyclerView.setAdapter(postAdapter);

            //Set Dialog width
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(lp);


//            adapter.setClickListener(new ItemClickListener() {
//                @Override
//                public void onClick(View view, int position) {
//                    for (ChildModel c : UserProfile.childs)
//                        c.Selected = false;
//                    UserProfile.childs.get(position).Selected= true;
//                    selectedClassId =Integer.parseInt(UserProfile.childs.get(position).UserToken);
//                    dialog.dismiss();
//                    intent = new Intent(MainActivity.this, HomeworkActivity.class);
//                    intent.putExtra("headerText", "Homework");
//                    intent.putExtra("classId", selectedClassId+"");
//                    startActivity(intent);
//                }
//            });

            dialog.setTitle("Post Result");
            dialog.show();
        } catch (Exception e) {
            e.getStackTrace();
            FirebaseCrash.report(e);
        }

    }
}
