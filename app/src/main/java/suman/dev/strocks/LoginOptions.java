package suman.dev.strocks;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Constant.Token;
import suman.dev.strocks.Model.UserProfile;


public class LoginOptions extends AppCompatActivity  {
    private Button btnLoginAsAdmin,btnLoginAsParents,btnLoginAsPrinciple,btnLoginAsStudent,btnLoginAsTeacher;
    String loginAs="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_options);

        ActionBar actionBar = getActionBar();


        btnLoginAsAdmin = (Button)findViewById(R.id.btnLoginAsAdmin);
        btnLoginAsParents = (Button)findViewById(R.id.btnLoginAsParents);
        btnLoginAsPrinciple = (Button)findViewById(R.id.btnLoginAsPrinciple);
        btnLoginAsStudent = (Button)findViewById(R.id.btnLoginAsStudent);
        btnLoginAsTeacher = (Button)findViewById(R.id.btnLoginAsTeacher);


        btnLoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAs = "Administrator";
                openIntent(v);
            }
        });

        btnLoginAsParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAs = "Parent";
                openIntent(v);
            }
        });

        btnLoginAsPrinciple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAs = "Principle";
                openIntent(v);
            }
        });

        btnLoginAsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAs = "Student";
                openIntent(v);
            }
        });

        btnLoginAsTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAs = "Teacher";
                openIntent(v);
            }
        });
    }

    private void openIntent(View view)
    {
        UserProfile.LoginAs=loginAs;
        Intent intent= new Intent(this, Login.class);
        startActivityForResult(intent, Const.LOGIN_ACTIVITY);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Const.LOGIN_ACTIVITY && resultCode==RESULT_OK)
        {
            openMainActivity();
        }
    }

    private void openMainActivity()
    {
        if(Token.access_token!=null && !Token.access_token.isEmpty())
        {
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);

            finish();
        }
    }
}