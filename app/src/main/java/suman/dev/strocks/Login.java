package suman.dev.strocks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONObject;

import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Constant.Token;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by Suman on 7/20/2017.
 */

public class Login extends AppCompatActivity{
    private EditText txtUserName, txtPassword;
    private Button btnLogin;
    private TextView txtViewLoginType;
    private VolleyService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtUserName = (EditText)findViewById(R.id.txtUserName);
        txtViewLoginType = (TextView)findViewById(R.id.txtViewLoginType);

        txtViewLoginType.setText(UserProfile.LoginAs+" login");

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        if(UserProfile.LoginAs.toLowerCase().equals("student"))
            txtUserName.setText("student@example.com");
        else if(UserProfile.LoginAs.toLowerCase().equals("parent"))
            txtUserName.setText("parent3@example.com");
        else if(UserProfile.LoginAs.toLowerCase().equals("teacher"))
            txtUserName.setText("teacher@example.com");
    }
    private void login()
    {

        service= new VolleyService();
        service.Login(txtUserName.getText().toString(), txtPassword.getText().toString(), this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try
                {
                    Token.token_type = result.getString("token_type");
                    Token.access_token = result.getString("access_token");
                    Token.refresh_token = result.getString("refresh_token");
                    Token.expires_in = result.getLong("expires_in");
                    getLogData();
                }
                catch (Exception e)
                {
                    Toast.makeText(Login.this, "Username or password is invalid!!!", Toast.LENGTH_SHORT).show();
                    Token.destroy();
                    UserProfile.Role=null;
                    FirebaseCrash.report(e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(Login.this, "Username or password is invalid!!!", Toast.LENGTH_SHORT).show();
                FirebaseCrash.report(error);
            }
        });
    }


    private void getLogData()
    {
        if(service!=null)
            service= new VolleyService();
        service.MakeGetRequest(Const.GET_LOGIN_DATA, this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {
                    try {
                        JSONObject obj = result.getJSONObject("user");
                        if (obj.getString("role").toLowerCase().equals(UserProfile.LoginAs.toLowerCase())) {
                            UserProfile.Role = obj.getString("role").toLowerCase();
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Token.destroy();
                            UserProfile.Role=null;
                            Toast.makeText(Login.this, "Username or password is invalid!!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        FirebaseCrash.report(e);
                        Token.destroy();
                        UserProfile.Role=null;
                        Toast.makeText(Login.this, "Username or password is invalid!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                FirebaseCrash.report(error);
                Token.destroy();
                UserProfile.Role=null;
                Toast.makeText(Login.this, "Username or password is invalid!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
