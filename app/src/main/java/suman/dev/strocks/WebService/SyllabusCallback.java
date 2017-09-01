package suman.dev.strocks.WebService;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import suman.dev.strocks.Model.Syllabus;

/**
 * Created by suman on 29/7/17.
 */

public interface SyllabusCallback {
    void onSuccess(JSONObject result, Syllabus syllabus);
    void onError(VolleyError error);
}
