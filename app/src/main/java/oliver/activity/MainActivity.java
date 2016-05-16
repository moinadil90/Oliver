package oliver.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import oliver.R;
import oliver.adapter.PagerAdapter;
import oliver.api.HttpCalls;
import oliver.callback.OnTaskCompleted;
import oliver.model.Exams;
import oliver.util.RestConstants;
import oliver.util.Utility;

public class MainActivity extends AppCompatActivity  implements OnTaskCompleted {
    private ViewPager  pager;
    private TabLayout tabLayout;
    public static ArrayList<Exams>mExamsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getExamsApi();
    }
    private void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pager               = (ViewPager) findViewById(R.id.view_pager);
        tabLayout           = (TabLayout) findViewById(R.id.tab_layout);
    }
    private void getExamsApi(){
            if(Utility.isNetworkAvailable(MainActivity.this)) {
                HttpCalls lHttpCalls = new HttpCalls(this, MainActivity.this);
                lHttpCalls.getExamsApi(RestConstants.GET_METHOD, RestConstants.EXAMS_API, "exams_api");
                Log.i("URL", RestConstants.EXAMS_API);
            }else {
                Utility.showInternetError(MainActivity.this);
            }
    }
    @Override
    public void onTaskCompleted(String response, String lPurpose) {
        if (lPurpose.equalsIgnoreCase("exams_api")) {
            JSONObject lJsonObject =null;
            JSONArray lJsonArray = null ;
            mExamsArrayList = new ArrayList<>();
            try {
                lJsonObject = new JSONObject(response);
                lJsonArray  = lJsonObject.getJSONArray("exams");
                for (int i =0;i<lJsonArray.length();i++){
                    JSONArray llJsonArray = lJsonArray.getJSONArray(i);
                    {
                        Exams lExams = new Exams();
                        lExams.setOne(llJsonArray.getString(0));
                        lExams.setTwo(llJsonArray.getString(1));
                        mExamsArrayList.add(lExams);
                    }
                }
                Log.i("Size = ",mExamsArrayList.size()+"");
                tabLayout.removeAllTabs();
                for (int j = 0;j<mExamsArrayList.size();j++){
                    tabLayout.addTab(tabLayout.newTab().setText(mExamsArrayList.get(j).getOne()));
                }
                Log.i("Size = ",mExamsArrayList.size()+"   :  "+tabLayout.getTabCount());
                PagerAdapter adapter      =   new PagerAdapter(MainActivity.this,tabLayout.getTabCount());
                pager.setAdapter(adapter);
                pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        pager.setCurrentItem(tab.getPosition());
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTaskError(String response, String lPurpose) {
        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onResume(){
        super.onResume();
        getExamsApi();
    }
}
