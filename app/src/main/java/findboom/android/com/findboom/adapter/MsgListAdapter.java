package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_MsgList;
import findboom.android.com.findboom.bean.Bean_RecordList;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class MsgListAdapter extends BaseRecyAdapter {

    public MsgListAdapter(Context context, List dataList) {
        super(context, dataList);
    }


    public MsgListAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        Bean_MsgList.MsgList msgList = (Bean_MsgList.MsgList) dataList.get(position);
        mHolder.setText(R.id.txt_date, msgList.SendTime);
        mHolder.setText(R.id.txt_content, msgList.MsgContent);
        mHolder.setText(R.id.txt_title, msgList.MsgTitle);
        if (msgList.MsgContent.length() * Tools.dip2px(((ViewHolder) holder).itemView.getContext(), 14) >= (Tools.getScreenWide(((ViewHolder) holder).itemView.getContext()) - Tools.dip2px(((ViewHolder) holder).itemView.getContext(), 120))) {
            mHolder.getView(R.id.img_more).setVisibility(View.VISIBLE);
        } else mHolder.getView(R.id.img_more).setVisibility(View.INVISIBLE);

        mHolder.setOnClick(R.id.img_more, position);
    }

    @Override
    public int getLayout() {
        return R.layout.system_msg_item;
    }

}
