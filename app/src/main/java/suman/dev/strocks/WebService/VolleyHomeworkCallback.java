package suman.dev.strocks.WebService;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import suman.dev.strocks.Model.UserHomework;

/**
 * Created by suman on 25/7/17.
 */

public interface VolleyHomeworkCallback {
    void onSuccess(JSONArray result, UserHomework homework);
    void onError(VolleyError error);
}
