package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_InviteList;
import findboom.android.com.findboom.bean.Bean_MsgList;
import findboom.android.com.findboom.interfacer.OnClickInterface;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class InviteListAdapter extends BaseRecyAdapter {
    private OnClickInterface onClickInterface;

    public InviteListAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public void setOnclick(OnClickInterface l) {
        this.onClickInterface = l;
    }


    public InviteListAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;

        Bean_InviteList.InviteList msgList = (Bean_InviteList.InviteList) dataList.get(position);
        mHolder.setText(R.id.txt_date, msgList.SendTime);
        mHolder.setText(R.id.txt_name, msgList.FromUserINickName);
//        mHolder.setText(R.id.txt_content, TextUtils.isEmpty(msgList.ApplyMsg) ? "请求加你为好友" : msgList.ApplyMsg);
        mHolder.setImage(R.id.avatar,msgList.Avatar);
    }


    @Override
    public int getLayout() {
        return R.layout.invite_msg_item;
    }

}
