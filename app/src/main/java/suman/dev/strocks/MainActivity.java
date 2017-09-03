package suman.dev.strocks;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.lang.UCharacter;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import suman.dev.strocks.Adapters.SwitchUserAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Constant.Token;
import suman.dev.strocks.Model.ChildModel;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserDetailCallback;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private Button btnHomework, btnSyllabus, btnEvents , btnAttandance ,  btnResult,btnTimeTable  ,btnFees  ,
    btnPTAForum,btnHolidayList;
    private TextView toolbar_title;
    private SwitchUserAdapter adapter;

    private VolleyService service;
    private UserProfileHeader profile;
    private Menu _menu;
    private boolean menuInited=false;
    private int selectedClassId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();

        setupWindowAnimations();
        checkLogin();
        //toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        //toolbar_title.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        _menu = menu;
        getMenuInflater().inflate(R.menu.switch_user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.nav_switch_user){
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OnResume();
    }


    private void addToolBarEvent()
    {
        try {
            if (UserProfile.Role.toLowerCase().equals("parent")) {
                _menu.findItem(R.id.nav_switch_user).setVisible(true);

               /* toolbar_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_view_list, 0);

                toolbar_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                });
                */
            } else if (UserProfile.Role.toLowerCase().equals("student")) {
                btnPTAForum.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    //This function called when parent select user from the list
    private void loadSelectedUserData()
    {
        if(service==null)
            service = new VolleyService();




        service.MakeGetRequest(String.format(Const.GET_USER_PROFILE, UserProfile.UserToken), this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONObject json = result.getJSONObject("data");
                    profile.loadSessionData(json);
                    profile.loadHomework(json);
                    profile.loadSubjectData(json);
                    profile.loadClassData(json);
                }
                catch (Exception e)
                {
                    FirebaseCrash.report(e);
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void showDialog() {
        try {

            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.user_chooser_dialog);

            ListView lv = (ListView) dialog.findViewById(R.id.listviewUser);
            if (adapter == null)
                adapter = new SwitchUserAdapter(UserProfile.childs, this);
            lv.setAdapter(adapter);

            adapter.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Toast.makeText(MainActivity.this, "Switched to : "+ UCharacter.toTitleCase(Locale.US, UserProfile.childs.get(position).GetFullName(),null,0), Toast.LENGTH_SHORT).show();
                    UserProfile.setToken(UserProfile.childs.get(position).UserToken);
                    loadSelectedUserData();
                    profile.setSwitchedUserDetail(UserProfile.childs.get(position).GetFullName());

                    for (ChildModel c : UserProfile.childs)
                        c.Selected = false;
                    UserProfile.childs.get(position).Selected= true;
                    UserProfile.Impersonated = true;

                    dialog.dismiss();
                }
            });


            dialog.setCancelable(false);
            dialog.setTitle("ListView");
            dialog.show();
        } catch (Exception e) {
            e.getStackTrace();
            FirebaseCrash.report(e);
        }
    }

    private void checkLogin()
    {
        if(Token.access_token!=null && !Token.access_token.isEmpty()) {
            loadUserProfile();
        }
    }

    private void loadUserProfile() {
        profile = new UserProfileHeader(this, findViewById(R.id.userProfileHeader));
        profile.loadProfile(new UserDetailCallback() {
            @Override
            public void onLoaded(boolean loaded) {
                UserProfile.IsCompleted = true;
                OnResume();
                if(UserProfile.Role.toLowerCase().equals("parent") && UserProfile.childs!=null && UserProfile.childs.size()>1)
                    showDialog();
            }
        });

    }

    private void OnResume(){
        if(!UserProfile.IsCompleted)
            return;
        addToolBarEvent();
    }

    private void initControls()
    {

        btnHomework =(Button)findViewById(R.id.btnHomework);
        btnHomework.setOnClickListener(this);
        btnHomework.startAnimation(setButtonAnimation());

        btnSyllabus=(Button)findViewById(R.id.btnSyllabus);
        btnSyllabus.setOnClickListener(this);
        btnSyllabus.startAnimation(setButtonAnimation());

        btnEvents=(Button)findViewById(R.id.btnEvents);
        btnEvents.setOnClickListener(this);
        btnEvents.startAnimation(setButtonAnimation());

        btnAttandance=(Button)findViewById(R.id.btnAttandance);
        btnAttandance.setOnClickListener(this);
        btnAttandance.startAnimation(setButtonAnimation());

        btnResult=(Button)findViewById(R.id.btnResult);
        btnResult.setOnClickListener(this);
        btnResult.startAnimation(setButtonAnimation());

        btnTimeTable=(Button)findViewById(R.id.btnTimeTable);
        btnTimeTable.setOnClickListener(this);
        btnTimeTable.startAnimation(setButtonAnimation());

        btnFees=(Button)findViewById(R.id.btnFees);
        btnFees.setOnClickListener(this);
        btnFees.startAnimation(setButtonAnimation());

        btnPTAForum=(Button)findViewById(R.id.btnPTAForum);
        btnPTAForum.setOnClickListener(this);
        btnPTAForum.startAnimation(setButtonAnimation());

        btnHolidayList=(Button)findViewById(R.id.btnHolidayList);
        btnHolidayList.setOnClickListener(this);
        btnHolidayList.startAnimation(setButtonAnimation());


    }

    private Animation setButtonAnimation()
    {
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.dashborad_button);
        return animation;
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.btnHomework:
                if (UserProfile.Role.toLowerCase().equals("teacher")) {
                    openClassChooser(R.id.btnHomework);
                } else {
                    intent = new Intent(this, HomeworkActivity.class);
                    intent.putExtra("headerText", "Homework");
                }
                break;
            case R.id.btnSyllabus:
                intent = new Intent(this, HomeworkActivity.class);
                intent.putExtra("headerText", "Syllabus");
                break;
            case R.id.btnHolidayList:
                intent = new Intent(this, HolidayListActivity.class);
                break;
            case R.id.btnFees:
                intent = new Intent(this, FeeActivity.class);
                break;
            case R.id.btnResult:
                intent = new Intent(this, ResultActivity.class);
                break;
            case R.id.btnEvents:
                intent = new Intent(this, EventsActivity.class);
                break;
            case R.id.btnAttandance:
                intent = new Intent(this,AttendanceActivity.class);
                break;
            case R.id.btnPTAForum:
                intent = new Intent(this, PTAForumActivity.class);
                break;
            case R.id.btnTimeTable:
                intent= new Intent(this, TimetableActivity.class);
                break;

        }
        if(intent!=null)
        {
            startActivity(intent);
        }
    }


    private void setupWindowAnimations()
    {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    private void openClassChooser(final int id){
        try {

            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.user_chooser_dialog);
            TextView txtUserDialog = (TextView)dialog.findViewById(R.id.txtUserDialog);
            txtUserDialog.setText("Select Class");
            ListView lv = (ListView) dialog.findViewById(R.id.listviewUser);
            if (adapter == null)
                adapter = new SwitchUserAdapter(UserProfile.childs, this);
            lv.setAdapter(adapter);

            adapter.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    for (ChildModel c : UserProfile.childs)
                        c.Selected = false;
                    UserProfile.childs.get(position).Selected= true;
                    selectedClassId =Integer.parseInt(UserProfile.childs.get(position).UserToken);
                    dialog.dismiss();
                    intent = new Intent(MainActivity.this, HomeworkActivity.class);
                    intent.putExtra("headerText", "Homework");
                    intent.putExtra("classId", selectedClassId+"");
                    startActivity(intent);
                }
            });
            dialog.setTitle("ListView");
            dialog.show();
        } catch (Exception e) {
            e.getStackTrace();
            FirebaseCrash.report(e);
        }

    }
}

