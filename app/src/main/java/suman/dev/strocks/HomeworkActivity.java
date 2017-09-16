package suman.dev.strocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import suman.dev.strocks.Adapters.HomeworkAdapter;
import suman.dev.strocks.Model.HomeworkClickListener;
import suman.dev.strocks.Model.HomeworkList;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.Model.UserSubjectData;

/**
 * Created by Node1 on 7/21/2017.
 */


public class HomeworkActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeworkAdapter adapter;

    private TextView content_title;
    private String title;

    private Intent intent;
    private int selectedClassId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        UserProfileHeader  profile =  new UserProfileHeader(this,findViewById(R.id.userProfileHeader));
        profile.loadProfile();

        content_title = (TextView)findViewById(R.id.content_toolbar_title);
        if(content_title!=null)
        {
            title = getIntent().getStringExtra("headerText");
            content_title.setText(title);
            if(title.equals("Syllabus"))
                content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.syllabus_black, 0, 0, 0);
            else {
                content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.reading1_black, 0, 0, 0);
            }

            if(UserProfile.Role.toLowerCase().equals("teacher")) {
                selectedClassId = Integer.parseInt(getIntent().getStringExtra("classId"));
                UserProfile.SubjectData.clear();
                for (UserSubjectData s : UserProfile.TeacherSubjects) {
                    if(s.ClassId==selectedClassId)
                        UserProfile.SubjectData.add(s);
                }
            }
        }
        bindHomework();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homework_list_btnView) {
            Toast.makeText(this, "View button clicked",
                    Toast.LENGTH_LONG).show();


        }
    }

    private void bindHomework()
    {

        recyclerView = (RecyclerView) findViewById(R.id.homeworkRecycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if(title.equals("Syllabus")) {
            adapter = new HomeworkAdapter();
            adapter.isUsedforHomework = false;
        }
        else {
            adapter = new HomeworkAdapter();
            adapter.isUsedforHomework = true;
        }

        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new HomeworkClickListener() {
            @Override
            public void onClick(View view, UserSubjectData subject) {
                if(title.equals("Syllabus"))
                {
                    intent = new Intent(HomeworkActivity.this, SyllabusDetailActivity.class);
                    intent.putExtra("SubjectId",subject.Id+"");
                    startActivity(intent);
                }
                else
                {
                    //if(view.getId()==R.id.homework_list_btnCreate) {
                    if(view.getId()==R.id.homework_list_btnView && !UserProfile.Role.toLowerCase().equals("teacher")){
                        intent = new Intent(HomeworkActivity.this, HomeworkCompleteActivity.class);
                        intent.putExtra("SubjectId", subject.Id);
                        if (selectedClassId > 0)
                            intent.putExtra("classId", selectedClassId + "");
                        startActivity(intent);
                    }
                    else if(view.getId()==R.id.homework_list_btnCreate){
                        intent = new Intent(HomeworkActivity.this, HomeworkCompleteActivity.class);
                        intent.putExtra("SubjectId", subject.Id);
                        if (selectedClassId > 0)
                            intent.putExtra("classId", selectedClassId + "");
                        startActivity(intent);
                    }
                    else if(view.getId()==R.id.homework_list_btnView && UserProfile.Role.toLowerCase().equals("teacher")) {
                        Intent intent = new Intent(HomeworkActivity.this, HomeworkListActivity.class);
                        intent.putExtra("SubjectId", subject.Id+"");
                        intent.putExtra("classId", selectedClassId + "");
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
