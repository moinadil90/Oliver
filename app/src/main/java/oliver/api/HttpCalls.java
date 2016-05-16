package oliver.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import oliver.callback.OnTaskCompleted;


/**
 * Created by Moin Adil on 5/15/2016.
 */
public class HttpCalls {
    private OnTaskCompleted mOnTaskCompleted;
    private Context mContext;
    public HttpCalls(OnTaskCompleted mOnTaskCompleted, Context mContext) {
        this.mOnTaskCompleted = mOnTaskCompleted;
        this.mContext = mContext;
    }

    public void getExamsApi(int lMethod, String lUrl, final String lPurpose) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(lMethod, lUrl, null, new Response.Listener<JSONObject>() {
            //JsonArrayRequest jsObjRequest = new JsonArrayRequest(lMethod, lUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONObject response)  {
                mOnTaskCompleted.onTaskCompleted(response.toString(), lPurpose);
                Log.i("response, check", response.toString());
                String string_response = response.toString();
                Log.i("response, check",string_response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("response", "error");
                JSONObject json = null;
                String err = "err";
                NetworkResponse response = error.networkResponse;
                if (lPurpose.equals("exams_api")) {

                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 400:
                                try {
                                    String responsestring = new String(response.data);
                                    json = new JSONObject(responsestring);
                                    err = json.getJSONObject("error").getString("message");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        //Additional cases
                    }
                    mOnTaskCompleted.onTaskError(err, lPurpose);

                }

            }
        });
        queue.add(jsObjRequest);
    }
}
