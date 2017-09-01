package suman.dev.strocks.WebService;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by Node1 on 7/20/2017.
 */

public interface VolleyJsonArrayCallback {
    void onSuccess(JSONArray result);
    void onError(VolleyError error);
}
