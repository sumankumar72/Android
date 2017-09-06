package suman.dev.strocks.WebService;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import suman.dev.strocks.Common.Common;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Constant.Token;
import suman.dev.strocks.Model.HomeworkDetails;
import suman.dev.strocks.Model.Syllabus;
import suman.dev.strocks.Model.UserHomework;

/**
 * Created by Node1 on 7/20/2017.
 */

public class VolleyService {
    Common common;
    public void MakeGetRequest(String endPoint, Context context, final VolleyJsonArrayCallback callback)
    {
        common = new Common(context);
        common.ShowPleaseWait();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Const.BASE_URL + endPoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                common.HidePleaseWait();
                try {
                    callback.onSuccess(response);
                } catch (Exception ex) {
                    FirebaseCrash.report(ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                common.HidePleaseWait();
                callback.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Token.AuthenticationCode());
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void MakeGetRequest(String endPoint,Context context, final VolleyJsonObjectCallback callback)
    {
        common = new Common(context);
        common.ShowPleaseWait();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, Const.BASE_URL + endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                common.HidePleaseWait();
                try {
                    if(response!=null && response!="")
                    {
                        JSONObject obj = new JSONObject(response);
                        callback.onSuccess(obj);
                    }

                } catch (Exception ex) {
                    FirebaseCrash.report(ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                common.HidePleaseWait();
                callback.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Token.AuthenticationCode());
                return params;
            }
        };

        requestQueue.add(request);
    }


    public void Login(final String username, final String password, final Context context, final VolleyJsonObjectCallback callback)
    {
        common = new Common(context);
        common.ShowPleaseWait();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Const.BASE_URL+Const.TOKEN ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                common.HidePleaseWait();
                try {
                    JSONObject token = new JSONObject(response);
                    callback.onSuccess(token);
                }
                catch (Exception e)
                {
                    e.getStackTrace();
                    FirebaseCrash.report(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                callback.onError(error);
                common.HidePleaseWait();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("client_id",Const.CLIENT_ID);
                params.put("client_secret",Const.CLIENT_SECRET);
                params.put("grant_type","password");
                params.put("username",username);
                params.put("password",password);
                params.put("scope","");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(postRequest);
    }


    public void MakeGetRequest(String endPoint, Context context, final VolleyHomeworkCallback callback, final UserHomework homework)
    {
        common  =new Common(context);
        common.ShowPleaseWait();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, Const.BASE_URL + endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                common.HidePleaseWait();
                try {
                    if(response!=null && response!="")
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray obj = jsonObject.getJSONArray("data");
                        callback.onSuccess(obj, homework);
                    }

                } catch (Exception ex) {
                    FirebaseCrash.report(ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                common.HidePleaseWait();
                callback.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Token.AuthenticationCode());
                return params;
            }
        };

        requestQueue.add(request);
    }




    public void LoadSyllabus(String endPoint, Context context, final SyllabusCallback callback, final Syllabus syllabus)
    {
        common = new Common(context);
        common.ShowPleaseWait();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, Const.BASE_URL + endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                common.HidePleaseWait();
                try {
                    if(response!=null && response!="")
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        callback.onSuccess(jsonObject, syllabus);
                    }

                } catch (Exception ex) {
                    FirebaseCrash.report(ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                common.HidePleaseWait();
                callback.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Token.AuthenticationCode());
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void DoLikes(String endPoint, Context context, final VolleyJsonObjectCallback callback)
    {
        common = new Common(context);
        common.ShowPleaseWait();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Const.BASE_URL + endPoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                common.HidePleaseWait();
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                common.HidePleaseWait();
                callback.onError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Token.AuthenticationCode());
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void DoComment(String endPoint, final String comment, final String forumId, Context context, final VolleyJsonObjectCallback callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        common = new Common(context);
        common.ShowPleaseWait();

        Map<String, String> params = new HashMap<String, String>();
        params.put("pta_forum_id", forumId);
        params.put("comments", comment);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Const.BASE_URL + endPoint, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                common.HidePleaseWait();
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                common.HidePleaseWait();
                callback.onError(error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Token.AuthenticationCode());
                return params;
            }
        };

        requestQueue.add(request);
    }


    public void MakePostRequest(String endPoint,Map<String, String> params, Context context, final VolleyJsonObjectCallback callback){
        common = new Common(context);
        common.ShowPleaseWait();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Const.BASE_URL + endPoint, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                common.HidePleaseWait();
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                common.HidePleaseWait();
                callback.onError(error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Token.AuthenticationCode());
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void MakePostRequest(String endPoint, JSONObject parameters, Context context, final VolleyJsonObjectCallback callback){
        common = new Common(context);
        common.ShowPleaseWait();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Const.BASE_URL + endPoint, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                common.HidePleaseWait();
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseCrash.report(error);
                common.HidePleaseWait();
                callback.onError(error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Token.AuthenticationCode());
                return params;
            }
        };

        requestQueue.add(request);
    }
}
