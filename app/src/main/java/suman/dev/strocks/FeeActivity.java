package suman.dev.strocks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import suman.dev.strocks.Adapters.AttandanceAdapter;
import suman.dev.strocks.Adapters.FeeAdapter;
import suman.dev.strocks.Adapters.ResultPostAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.ChildModel;
import suman.dev.strocks.Model.FeeDetail;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserClassData;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.Model.UserSubjectData;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 22/7/17.
 */

public class FeeActivity  extends AppCompatActivity implements View.OnClickListener  {
    private final VolleyService service= new VolleyService();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FeeAdapter adapter;

    private Spinner ddlMonth;
    private Button fee_btnPayNow;
    private TextView content_title,fee_TotalAmount;
    private String title;
    private List<FeeDetail> feeDetails;
    private FeeDetail feeDetail;
    private float TotalDue=0;

    private String[] Months = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    private String SelectedMonth,CurrentMonth;
    private float AmountPaid=0;

    private String classId;
    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private AttandanceAdapter studentAdapter;
    private ArrayList<ChildModel> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);

        UserProfileHeader  profile =  new UserProfileHeader(this,findViewById(R.id.fee_userProfileHeader));
        profile.loadProfile();

        if(UserProfile.Role.toLowerCase().equals("teacher")){
            intiLayoutForTeacher();
        }
        else {
            initLayoutForStudent();
        }

    }

    //this function called when user role is student or parent
    private void initLayoutForStudent(){
        SimpleDateFormat month_date = new SimpleDateFormat("MM");
        CurrentMonth = month_date.format(new Date());

        TextView content_title = (TextView)findViewById(R.id.content_toolbar_title);
        content_title.setText("Fee");
        content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupee_black, 0, 0, 0);

        ddlMonth = (Spinner)findViewById(R.id.ddlMonth);

        ArrayAdapter spieradapter  = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, Months);
        ddlMonth.setAdapter(spieradapter);

        ddlMonth.setSelection(Integer.parseInt(CurrentMonth)-1);

        ddlMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedMonth = Months[position];
                loadFee();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fee_btnPayNow = (Button)findViewById(R.id.fee_btnPayNow);
        if(UserProfile.Role.toLowerCase().equals("student"))
            fee_btnPayNow.setEnabled(false);

        fee_TotalAmount = (TextView)findViewById(R.id.fee_TotalAmount);
    }

    private void intiLayoutForTeacher(){
        RelativeLayout layoutStudentFee = (RelativeLayout)findViewById(R.id.layoutStudentFee);
        layoutStudentFee.setVisibility(View.GONE);
        LinearLayout layoutForTeacher = (LinearLayout)findViewById(R.id.layoutForTeacher);
        layoutForTeacher.setVisibility(View.VISIBLE);

        Spinner ddlClass = (Spinner) findViewById(R.id.ddlClass);
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
        } catch (Exception e) {

        }
    }
    //This function execute when user loggedin as Teacher Role
    private void loadStudents(){
        students= new ArrayList<>();
        service.MakeGetRequest(String.format(Const.GET_STUDENT_BY_CLASS, UserProfile.SessionData.Id, classId),
                FeeActivity.this, new VolleyJsonObjectCallback() {
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
        studentRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        studentRecyclerView.setHasFixedSize(true);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);

        studentAdapter = new AttandanceAdapter(students, true);
        studentRecyclerView.setAdapter(studentAdapter);

        studentAdapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                createFeeDialog(students.get(position).UserToken);
            }
        });
    }

    private void createFeeDialog(final String StudentId){
        try {
            LinearLayout layout = new LinearLayout(FeeActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText txtReason = new EditText(FeeActivity.this);
            txtReason.setHint("Reason");
            layout.addView(txtReason);

            final EditText txtAmount = new EditText(FeeActivity.this);
            txtAmount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            txtAmount.setHint("Amount");
            layout.addView(txtAmount);


            AlertDialog.Builder dialog = new AlertDialog.Builder(FeeActivity.this);
            dialog.setView(layout);
            dialog.setTitle("Create Fine");
            dialog.show();

            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(FeeActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });


//            final Dialog dialog = new Dialog(FeeActivity.this);
//            dialog.setView(layout);;
//            dialog.setTitle("Create Fine");
//            dialog.show();
        } catch (Exception e) {
            e.getStackTrace();
            FirebaseCrash.report(e);
        }

    }


    @Override
    public void onClick(View v) {

    }

    private void loadFee()
    {
        String endPoint=String.format(Const.GET_FEE_DETAILS, UserProfile.UserToken,SelectedMonth.toLowerCase(),UserProfile.SessionData.Id);
        service.MakeGetRequest(endPoint,this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.getJSONArray("data") != null) {
                        feeDetails = new ArrayList<FeeDetail>();
                        JSONArray array = result.getJSONArray("data");
                        JSONObject json;
                        TotalDue = 0;
                        for (int i = 0; i < array.length(); i++) {
                            json = array.getJSONObject(i);
                            //TotalDue = TotalDue + Float.parseFloat(json.getString("amount"));
                            feeDetail = new FeeDetail();

                            feeDetail.Id = json.getInt("session_id");
                            feeDetail.FeeId = json.getInt("fees_id");
                            feeDetail.ForMonth = json.getString("for_month");
                            feeDetail.Amount = json.getString("amount");
                            feeDetail.Name = json.getString("fees_name");

                            feeDetails.add(feeDetail);
                        }
                        TotalDue = Float.parseFloat(result.getString("total_amount"));
                        array = result.getJSONArray("extra_fee");
                        if (array.length() > 0) {

                            JSONArray extraFee = array.getJSONObject(0).getJSONArray("due_particular_name");
                            TotalDue = TotalDue + Float.parseFloat(array.getJSONObject(0).getString("due_amount"));

                            for (int i = 0; i < extraFee.length(); i++) {
                                json = extraFee.getJSONObject(i);
                                feeDetail = new FeeDetail();

                                feeDetail.Id = -1;
                                feeDetail.FeeId = -1;
                                feeDetail.ForMonth = json.getString("month");
                                feeDetail.Amount = json.getString("amount");
                                feeDetail.Name = "Extra Fee - "+json.getString("particular_name");

                                feeDetails.add(feeDetail);
                            }
                        }
                        getPaidAmount();
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
                Toast.makeText(FeeActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }
private void getPaidAmount()
{
    service.MakeGetRequest(String.format(Const.GET_PAYMENT_DETAIL, UserProfile.UserToken, SelectedMonth.toLowerCase()), this, new VolleyJsonObjectCallback() {
        @Override
        public void onSuccess(JSONObject result) {
            if(result!=null){
                try {
                    JSONArray array = result.getJSONArray("data");
                    if(array.length()>0)
                    {
                        AmountPaid = array.getJSONObject(0).getLong("paid_amount");
                    }
                }
                catch (Exception e)
                {
                    FirebaseCrash.report(e);
                    e.getStackTrace();
                }
            }
            setAdapter();
            double amount = TotalDue-AmountPaid;
            DecimalFormat df = new DecimalFormat("#.##");
            fee_TotalAmount.setText(df.format(amount));
        }

        @Override
        public void onError(VolleyError error) {
            setAdapter();
            fee_TotalAmount.setText(String.valueOf(TotalDue-AmountPaid));
        }
    });
}
    private void setAdapter()
    {
        recyclerView = (RecyclerView) findViewById(R.id.feeRecycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FeeAdapter(feeDetails);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
    }
}
