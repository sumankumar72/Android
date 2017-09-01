package suman.dev.strocks.WebService;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;


public interface VolleyJsonObjectCallback {
    void onSuccess(JSONObject result);
    void onError(VolleyError error);
}
