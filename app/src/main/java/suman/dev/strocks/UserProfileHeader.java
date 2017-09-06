package suman.dev.strocks;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Streams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.ChildModel;
import suman.dev.strocks.Model.HomeworkDetails;
import suman.dev.strocks.Model.TeacherProfile;
import suman.dev.strocks.Model.UserClassData;
import suman.dev.strocks.Model.UserDetailCallback;
import suman.dev.strocks.Model.UserHomework;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.Model.UserSessionData;
import suman.dev.strocks.Model.UserSubjectData;
import suman.dev.strocks.WebService.VolleyHomeworkCallback;
import suman.dev.strocks.WebService.VolleyJsonArrayCallback;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by Node1 on 7/20/2017.
 */

public class UserProfileHeader {
    private ImageView profilePicture;
    private TextView lblName, lblOccupation,lblSession,lblLastLogin, txtLastLogin;
    private Context _context;
    private JSONArray jArray;
    private JSONObject jsonObject;
    private VolleyService service = new VolleyService();
    private UserHomework homework;
    private UserDetailCallback _callback;


    public UserProfileHeader(Context context, View view){
        profilePicture = (ImageView)view.findViewById(R.id.profilePicture);
        lblName = (TextView) view.findViewById(R.id.lblName);
        lblOccupation = (TextView) view.findViewById(R.id.lblOccupation);
        lblSession = (TextView) view.findViewById(R.id.lblSession);
        lblLastLogin = (TextView) view.findViewById(R.id.lblLastLogin);
        txtLastLogin = (TextView)view.findViewById(R.id.txtLastLogin);
        _context =context;
    }

    public void loadProfile()
    {
        if(UserProfile.IsCompleted) {
            setDetails();
            return;
        }

        service.MakeGetRequest(Const.GET_LOGIN_DATA,_context, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result!=null){
                    try {
                        JSONObject json = result.getJSONObject("user");
                        loadUserProfile(json.getString("user_token"));
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
                Toast.makeText(_context, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadProfile(UserDetailCallback callback)
    {
        _callback = callback;
        loadProfile();
    }



    public void setDetails()
    {
        lblName.setText(UserProfile.getFullName());
        lblOccupation.setText(UserProfile.LoginAs);
        lblSession.setText(UserProfile.SessionData.Name);
        lblLastLogin.setText("");
        Picasso.with(_context).load(Const.PROFILE_IMAGE_BASE+ UserProfile.Image).placeholder(R.drawable.profile).into(profilePicture);

        if(UserProfile.Impersonated) {
            for(ChildModel c: UserProfile.childs) {
                if(c.Selected)
                setSwitchedUserDetail(c.GetFullName());
            }
        }
    }

    public void loadClassData(JSONObject json)
    {
        try{
            if (json.getJSONObject("student_class_data") != null) {
                jsonObject = json.getJSONObject("student_class_data");
                UserProfile.ClassData.Id = jsonObject.getInt("id");
                UserProfile.ClassData.SchoolCode = jsonObject.getInt("school_code");
                UserProfile.ClassData.Name = jsonObject.getString("name");
            }
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            e.getStackTrace();
        }
    }
    public void loadSessionData(JSONObject json)
    {
        try{
            if (json.getJSONObject("student_session_data") != null) {
                jsonObject = json.getJSONObject("student_session_data");
                UserProfile.SessionData.Id = jsonObject.getInt("id");
                UserProfile.SessionData.Name=jsonObject.getString("name");
            }
            if(UserProfile.Role.toLowerCase().equals("teacher") && json.getJSONObject("student_session_data") == null){
                UserProfile.SessionData.Id = 1;
                UserProfile.SessionData.Name="Test Session";
            }
        }
        catch (Exception e)
        {

            UserProfile.SessionData.Id = 1;
            UserProfile.SessionData.Name="Test Session";

            FirebaseCrash.report(e);
            e.getStackTrace();
        }
    }

    public void loadSubjectData(JSONObject json)
    {
        try {
            if(UserProfile.SubjectData.size()>0)
                UserProfile.SubjectData.clear();

            UserSubjectData subjectData;
            if (json.getJSONArray("student_class_subject_data") != null) {
                jArray = json.getJSONArray("student_class_subject_data");
                for (int i = 0; i < jArray.length(); i++) {
                    subjectData = new UserSubjectData();
                    subjectData.Id = jArray.getJSONObject(i).getInt("id");
                    subjectData.Name = jArray.getJSONObject(i).getString("name");
                    UserProfile.SubjectData.add(subjectData);
                }
            }
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            e.getStackTrace();
        }
    }

    public void setSwitchedUserDetail(String Name)
    {
        txtLastLogin.setText("Login As");
        lblLastLogin.setText(Name);
    }

    public void loadHomework(JSONObject json)
    {
        try{
            if(UserProfile.HomeworkData.size()>0)
                UserProfile.HomeworkData.clear();

            if(json.getJSONArray("student_home_work")!=null)
            {
                JSONArray jsonArray =json.getJSONArray("student_home_work");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject h = jsonArray.getJSONObject(i);
                    homework= new UserHomework();
                    homework.Id =h.getInt("id");
                    homework.StudentId =h.getInt("student_id");
                    homework.TeacherId =h.getInt("teacher_id");
                    homework.HomeworkId =h.getInt("homework_id");
                    homework.StudentId =h.getInt("subject_id");
                    homework.SubmittedDate =h.getString("submitted_date");
                    homework.StudentNote =h.getString("student_note");
                    homework.FileUrl =h.getString("file_url");
                    UserProfile.HomeworkData.add(homework);
                    //if(homework.SubmittedDate.isEmpty())
                    addHomeworkInSubject(h.getInt("subject_id"), homework.HomeworkId);
                }
            }
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            e.getStackTrace();
        }
    }

    public void loadChilds(JSONObject json)
    {
        try{

            if(json.getJSONArray("parent_child_data")!=null){
                JSONArray array= json.getJSONArray("parent_child_data");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject j=array.getJSONObject(i);
                    UserProfile.childs.add(new ChildModel(
                            j.getString("user_token"),
                            j.getString("first_name"),
                            j.getString("middle_name"),
                            j.getString("last_name")
                    ));
                }
            }
        }catch (Exception e)
        {
            FirebaseCrash.report(e);
            e.getStackTrace();
        }
    }


    private void loadUserProfile(final String UserToken)
    {
        service.MakeGetRequest(String.format(Const.GET_USER_PROFILE,UserToken), _context, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result!=null){
                    try {
                        JSONObject json = result.getJSONObject("data");
                        UserProfile.UserToken = UserToken;
                        UserProfile.SchoolCode = json.getString("school_code");
                        UserProfile.Email = json.getString("email");
                        UserProfile.Role = json.getString("role");
                        UserProfile.ParentId = json.getString("parent_id");
                        UserProfile.FirstName = json.getString("first_name");
                        UserProfile.MiddleName = json.getString("middle_name");
                        UserProfile.LastName = json.getString("last_name");
                        UserProfile.DOB = json.getString("dob");
                        UserProfile.Age = json.getString("age");
                        UserProfile.Address = json.getString("address");
                        UserProfile.Mobile = json.getString("mobile");
                        UserProfile.AlternateMobile = json.getString("alternate_mobile");
                        UserProfile.City = json.getString("city");
                        UserProfile.State = json.getString("state");
                        UserProfile.Pin = json.getString("pin");
                        UserProfile.Image = json.getString("user_image");

                        //Bind session data of logged in user
                        loadSessionData(json);


                        //Bind all subject of the logged in user
                        loadSubjectData(json);

                        //Binding class data of logged in user
                        loadClassData(json);

                        //Bind all homework

                        loadHomework(json);

                        loadChilds(json);
                        if(UserProfile.Role.toLowerCase().equals("teacher"))
                            loadTeacherSubject();

                        if(_callback!=null)
                            _callback.onLoaded(true);

                        setDetails();


                        //loadHomeworkDetail();
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
                FirebaseCrash.report(error);
                Toast.makeText(_context, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addHomeworkInSubject(int SubjectId, int HomeworkId)
    {
        for(int i=0;i<UserProfile.SubjectData.size();i++) {
            if(UserProfile.SubjectData.get(i).Id==SubjectId) {
                UserProfile.SubjectData.get(i).HasHomework = true;
                UserProfile.SubjectData.get(i).HomeworkId = HomeworkId+"";
            }
        }
    }

    private void loadTeacherSubject(){
        service.MakeGetRequest(String.format(Const.TEACHER_SUBJECT_DATA, UserProfile.SessionData.Id, 1, UserProfile.UserToken), _context, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try{
                    UserSubjectData subjectData;
                    JSONArray subjects = result.getJSONArray("subjects");
                    for (int i=0;i<subjects.length();i++){
                        subjectData = new UserSubjectData();
                        subjectData.Id = subjects.getJSONObject(i).getInt("id");
                        subjectData.ClassId = subjects.getJSONObject(i).getInt("class_id");
                        subjectData.Name = subjects.getJSONObject(i).getString("name");
                        //subjectData.is_class_teacher = "Yes";//subjects.getJSONObject(i).getString("is_class_teacher");
                        UserProfile.TeacherSubjects.add(subjectData);
                    }
                    JSONArray classes = result.getJSONArray("classes");
                    UserClassData classData;
                    for (int i=0;i<classes.length();i++) {
                        classData = new UserClassData();
                        classData.Id = classes.getJSONObject(i).getInt("id");
                        classData.SchoolCode = classes.getJSONObject(i).getInt("school_code");
                        classData.Name = classes.getJSONObject(i).getString("name");
                        UserProfile.Classes.add(classData);
                        UserProfile.childs.add(new ChildModel(classData.Id+"", classData.Name, "", ""));
                    }

                    JSONArray data = result.getJSONArray("data");
                    for(int i=0;i<data.length();i++){
                        for(UserSubjectData s : UserProfile.TeacherSubjects){
                            if(data.getJSONObject(i).getInt("subject_id")==s.Id)
                                s.is_class_teacher = data.getJSONObject(i).getString("is_class_teacher");
                        }
                    }

                }catch (Exception e){

                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}