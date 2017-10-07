package suman.dev.strocks;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import suman.dev.strocks.Common.Common;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.HomeworkDetails;
import suman.dev.strocks.Model.TeacherProfile;
import suman.dev.strocks.Model.UserHomework;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonArrayCallback;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 22/7/17.
 */

public class HomeworkCompleteActivity extends AppCompatActivity {
    private int HomeworkId;
    private int SubjectId;
    private TextView homework_complete_lblSubjectName, homework_complete_facultyName, homework_complete_submissionDate, homework_complete_facultyNotes;
    private ScrollView scrollView;
    private VolleyService service = new VolleyService();
    private UserHomework homework;
    private EditText txtStudentNote;
    private Button btnUpload, btnSave;
    private File file = null;

    private EditText txtNote;
    private FloatingActionButton btnAttach;
    private Button btnCreateHomework;
    private String selectedClassId="";
    private String endDate="";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_complete);
        UserProfileHeader profile = new UserProfileHeader(this, findViewById(R.id.homework_complete_userProfileHeader));
        profile.loadProfile();
        initControls();

        TextView content_title = (TextView) findViewById(R.id.content_toolbar_title);
        content_title.setText("Homework");
        content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.reading1_black, 0, 0, 0);

        btnCreateHomework = (Button) findViewById(R.id.btnCreateHomework);
        txtNote = (EditText) findViewById(R.id.txtNote);
        btnAttach = (FloatingActionButton) findViewById(R.id.btnAttach);
        SubjectId = getIntent().getIntExtra("SubjectId", 0);
        final TextView lblEndDate = (TextView)findViewById(R.id.lblEndDate);
        try {
            if (UserProfile.Role.toLowerCase().equals("teacher")) {

                endDate=sdf.format(new Date());
                lblEndDate.setText("End Date: "+ endDate);

                final Calendar calendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        endDate=sdf.format(calendar.getTime());
                        lblEndDate.setText("End Date: "+ endDate);
                    }
                };


                final DatePickerDialog datePicker = new DatePickerDialog(HomeworkCompleteActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

                lblEndDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        datePicker.show();
                    }
                });


                selectedClassId = getIntent().getStringExtra("classId");
                btnAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FilePickerBuilder.getInstance().setMaxCount(1).enableDocSupport(true).pickFile(HomeworkCompleteActivity.this);
                    }
                });

                btnCreateHomework.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createHomework();
                    }
                });

            } else {
                ScrollView uploadHomeworl = (ScrollView) findViewById(R.id.uploadHomeworl);
                uploadHomeworl.setVisibility(View.VISIBLE);
                lblEndDate.setVisibility(View.GONE);
                btnCreateHomework.setVisibility(View.GONE);
                txtNote.setVisibility(View.GONE);
                btnAttach.setVisibility(View.GONE);
                getHomework(SubjectId);
            }


            checkPermission();
        }catch (Exception e){
            String ex = e.getMessage();
        }
    }


    private void setDetails(UserHomework homework) {
        String s = homework.homeworkDetails.getSubjectName();
        homework_complete_lblSubjectName.setText(homework.homeworkDetails.getSubjectName());
        homework_complete_facultyName.setText(homework.homeworkDetails.TeacherProfile.getFullName());
        homework_complete_submissionDate.setText(homework.homeworkDetails.IssueDate);
        homework_complete_facultyNotes.setText(homework.homeworkDetails.TeacherNote);
        txtStudentNote.setText(homework.StudentNote);
        if (homework.StudentNote.trim().length() > 0) {
            btnUpload.setVisibility(View.GONE);
            btnSave.setEnabled(false);
        }
    }


    private void initControls() {
        homework_complete_lblSubjectName = (TextView) findViewById(R.id.homework_complete_lblSubjectName);
        homework_complete_facultyName = (TextView) findViewById(R.id.homework_complete_facultyName);
        homework_complete_submissionDate = (TextView) findViewById(R.id.homework_complete_submissionDate);
        homework_complete_facultyNotes = (TextView) findViewById(R.id.homework_complete_facultyNotes);
        btnUpload = (Button) findViewById(R.id.homework_complete_btnupload);
        btnSave = (Button) findViewById(R.id.btnSave);
        txtStudentNote = (EditText) findViewById(R.id.txtStudentNote);


        if (!UserProfile.Role.toLowerCase().equals("student")) {
            btnSave.setEnabled(false);
            btnUpload.setEnabled(false);
        }


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePickerBuilder.getInstance().setMaxCount(1).enableDocSupport(true).pickFile(HomeworkCompleteActivity.this);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHomework();
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FilePickerConst.REQUEST_CODE_DOC && resultCode == RESULT_OK) {
            ArrayList<String> docsPath = new ArrayList<>();
            docsPath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
            if (docsPath.size() > 0) {
                file = new File(docsPath.get(0));
                if (file.exists()) {
                    Toast.makeText(this, "File attached successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getHomework(int SubjectId) {
        service.MakeGetRequest(String.format(Const.GET_HOMEWORK, SubjectId), this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.length() > 0) {
                    try {
                        JSONObject json = result.getJSONArray("data").getJSONObject(0);
                        homework = new UserHomework();
                        homework.homeworkDetails = new HomeworkDetails();
                        homework.homeworkDetails.Id = json.getInt("id");
                        homework.homeworkDetails.ClassId = json.getInt("class_id");
                        homework.homeworkDetails.SubjectId = json.getInt("subject_id");
                        homework.homeworkDetails.IssueDate = json.getString("issued_date");
                        homework.homeworkDetails.EndDate = json.getString("end_date");
                        homework.homeworkDetails.TeacherNote = json.getString("teacher_note");
                        homework.homeworkDetails.FileUrl = json.getString("file_url");

                        JSONObject teacher = json.getJSONObject("teacher");
                        homework.homeworkDetails.TeacherProfile = new TeacherProfile();
                        homework.homeworkDetails.TeacherProfile.SchoolCode = teacher.getString("school_code");
                        homework.homeworkDetails.TeacherProfile.Email = teacher.getString("email");
                        //homework.homeworkDetails.TeacherProfile.Role = teacher.getString("role");
                        homework.homeworkDetails.TeacherProfile.ParentId = teacher.getString("parent_id");
                        homework.homeworkDetails.TeacherProfile.FirstName = teacher.getString("first_name");
                        homework.homeworkDetails.TeacherProfile.MiddleName = teacher.getString("middle_name");
                        homework.homeworkDetails.TeacherProfile.LastName = teacher.getString("last_name");
                        homework.homeworkDetails.TeacherProfile.DOB = teacher.getString("dob");
                        homework.homeworkDetails.TeacherProfile.Age = teacher.getString("age");
                        homework.homeworkDetails.TeacherProfile.Address = teacher.getString("address");
                        homework.homeworkDetails.TeacherProfile.Mobile = teacher.getString("mobile");
                        homework.homeworkDetails.TeacherProfile.AlternateMobile = teacher.getString("alternate_mobile");
                        homework.homeworkDetails.TeacherProfile.City = teacher.getString("city");
                        homework.homeworkDetails.TeacherProfile.State = teacher.getString("state");
                        homework.homeworkDetails.TeacherProfile.Pin = teacher.getString("pin");
                        homework.homeworkDetails.TeacherProfile.Image = teacher.getString("user_image");
                        setDetails(homework);
                    } catch (Exception e) {
                        FirebaseCrash.report(e);
                        e.getStackTrace();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(HomeworkCompleteActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //This method called by student
    private void saveHomework() {
        if (txtStudentNote.getText().toString().trim().length() <= 0) {
            Toast.makeText(HomeworkCompleteActivity.this, "Please enter note!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(file==null){
            Toast.makeText(HomeworkCompleteActivity.this, "Please select file!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("file_url", getBase64());
        params.put("student_note", txtStudentNote.getText().toString().trim());
        String endPoint = String.format(Const.COMPLETE_HOMEWORK, homework.homeworkDetails.Id);

        service.MakePostRequest(endPoint, params, HomeworkCompleteActivity.this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(HomeworkCompleteActivity.this, "Homework posted successfully!!!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(HomeworkCompleteActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createHomework(){
        if (txtNote.getText().toString().trim().length() <= 0) {
            Toast.makeText(HomeworkCompleteActivity.this, "Please enter note!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(file==null){
            Toast.makeText(HomeworkCompleteActivity.this, "Please select file!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("subject_topic_id","1");
        params.put("subject_id",SubjectId+"");
        params.put("teacher_id",UserProfile.UserToken);
        params.put("class_id",selectedClassId);
        params.put("issue_date",sdf.format(new Date()));
        params.put("end_date",endDate);
        params.put("teacher_note", txtNote.getText().toString().trim());
        params.put("file_url", getBase64());



        service.MakePostRequest(Const.CREATE_HOMEWORK, params, HomeworkCompleteActivity.this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(HomeworkCompleteActivity.this, R.string.homework_created, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(HomeworkCompleteActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getBase64() {
        Uri uri = Uri.fromFile(file);
        String ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);


        String base64 = Base64.encodeToString(convertFileToByteArray(file), Base64.DEFAULT);
        return "\"data:" + mimeType + ";base64," + base64;
    }

    public byte[] convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }
        return byteArray;
    }


}
