package oliver.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import oliver.R;
import oliver.activity.MainActivity;

public class PagerAdapter extends android.support.v4.view.PagerAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private int mNumberOftabs;
    public PagerAdapter(Context context,int lNumberOftabs){
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mNumberOftabs = lNumberOftabs;
    }
    @Override
    public int getCount() {
        return mNumberOftabs;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View page = inflater.inflate(R.layout.fragment_exam, null);
        TextView t = (TextView) page.findViewById(R.id.text_general);
        t.setText(MainActivity.mExamsArrayList.get(position).getTwo());
        ((ViewPager) container).addView(page, 0);
        return page;
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (View) arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
        object = null;
    }
}