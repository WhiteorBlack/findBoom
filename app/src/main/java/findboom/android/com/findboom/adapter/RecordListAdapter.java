package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_FriendList;
import findboom.android.com.findboom.bean.Bean_RecordList;
import findboom.android.com.findboom.interfacer.OnClickInterface;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class RecordListAdapter extends BaseRecyAdapter {

    public RecordListAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public RecordListAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        Bean_RecordList.RecordList recordList = (Bean_RecordList.RecordList) dataList.get(position);
        mHolder.setText(R.id.txt_date, recordList.GetTime);
        mHolder.setText(R.id.txt_content,recordList.Remark);

    }


    @Override
    public int getLayout() {
        return R.layout.record_list_item;
    }

}
