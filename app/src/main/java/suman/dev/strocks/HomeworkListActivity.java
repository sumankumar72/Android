package suman.dev.strocks;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import suman.dev.strocks.Adapters.HomeworkListAdapter;
import suman.dev.strocks.Adapters.HomeworkListStudentsAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.HomeworkList;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by sshan on 9/16/2017.
 */

public class HomeworkListActivity extends AppCompatActivity {
    private TextView content_title;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private String selectedDate="";
    private String SubjectId="";
    private String classId="";
    private final VolleyService service = new VolleyService();
    private ArrayList<HomeworkList> homeworks = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeworkListAdapter adapter;

    private RecyclerView recyclerView1;
    private RecyclerView.LayoutManager layoutManager1;
    private HomeworkListStudentsAdapter adapter1;
    LinearLayout layouthomeworks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_list_teacher);
        UserProfileHeader  profile =  new UserProfileHeader(this,findViewById(R.id.userProfileHeader));
        profile.loadProfile();

        content_title = (TextView)findViewById(R.id.content_toolbar_title);
        content_title.setText("Homework List");

        layouthomeworks =(LinearLayout)findViewById(R.id.layouthomeworks);
        recyclerView1 = (RecyclerView)findViewById(R.id.recyclerview1);



        SubjectId = getIntent().getExtras().getString("SubjectId");
        classId = getIntent().getExtras().getString("classId");

        final TextView lblDate = (TextView)findViewById(R.id.lblDate);
        selectedDate=sdf.format(new Date());
        lblDate.setText("Issued Date: "+ selectedDate);
        loadHomeworkList();

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                selectedDate=sdf.format(calendar.getTime());
                lblDate.setText("Issued Date: "+ selectedDate);

                loadHomeworkList();
            }
        };

        final DatePickerDialog datePicker = new DatePickerDialog(this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

        lblDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });


    }

    private void loadHomeworkList(){
        service.MakeGetRequest(String.format(Const.GET_HOMEWORK_LIST, UserProfile.UserToken, classId, SubjectId, selectedDate), this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray array = result.getJSONArray("data");
                    HomeworkList homework;
                    for (int i=0;i<array.length();i++){
                        homework = new HomeworkList();
                        homework.Id = array.getJSONObject(i).getInt("id");
                        homework.ClassName = array.getJSONObject(i).getString("class_name");
                        homework.SubjectName = array.getJSONObject(i).getString("subject_name");
                        homework.IssuedDate = array.getJSONObject(i).getString("issued_date");
                        homework.EndDate = array.getJSONObject(i).getString("end_date");
                        homework.FileUrl = array.getJSONObject(i).getString("file_url");
                        homework.TeacherNote = array.getJSONObject(i).getString("teacher_note");
                        JSONArray students = array.getJSONObject(i).getJSONArray("student_home_work");
                        for(int j=0;j<students.length();j++){
                            homework.AddStudent(
                                    students.getJSONObject(j).getInt("student_id"),
                                    students.getJSONObject(j).getString("submitted_date"),
                                    students.getJSONObject(j).getString("student_note"),
                                    students.getJSONObject(j).getString("file_url"),
                                    students.getJSONObject(j).getString("student_name")
                            );
                        }
                        homeworks.add(homework);
                    }
                    setAdapter();
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void setAdapter(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeworkListAdapter(homeworks);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                layouthomeworks.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.VISIBLE);

                recyclerView1.setHasFixedSize(true);
                layoutManager1 = new LinearLayoutManager(HomeworkListActivity.this);
                recyclerView1.setLayoutManager(layoutManager1);
                adapter1 = new HomeworkListStudentsAdapter(homeworks.get(position));
                recyclerView1.setAdapter(adapter1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(layouthomeworks.getVisibility()==View.GONE) {
            recyclerView1.setVisibility(View.GONE);
            layouthomeworks.setVisibility(View.VISIBLE);
        }
        else
            super.onBackPressed();

    }
}
