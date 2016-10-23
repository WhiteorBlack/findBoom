package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class BaseAdapter extends android.widget.BaseAdapter {
    private Context context;
    private List dataList;

    public BaseAdapter(Context context, List dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
