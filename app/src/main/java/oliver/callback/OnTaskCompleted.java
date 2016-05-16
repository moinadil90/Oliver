package oliver.callback;

/**
 * Created by Moin Adil on 5/15/2016.
 */
public interface OnTaskCompleted {
    void onTaskCompleted(String response, String lPurpose);
    void onTaskError(String response, String lPurpose);
}
