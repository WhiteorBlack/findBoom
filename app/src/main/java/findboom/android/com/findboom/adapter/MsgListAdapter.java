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

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class MsgListAdapter extends BaseRecyAdapter {
    private OnClickInterface onClickInterface;

    public MsgListAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public void setOnclick(OnClickInterface l) {
        this.onClickInterface = l;
    }


    public MsgListAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        if (onClickInterface != null) {
            mHolder.itemView.findViewById(R.id.img_talk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickInterface.onClick(v, position);
                }
            });
        }

        Bean_MsgList.MsgList msgList= (Bean_MsgList.MsgList) dataList.get(position);
        mHolder.setText(R.id.txt_date, msgList.SendTime);
        mHolder.setRadiusImage(R.id.txt_content,msgList.MsgContent);

    }


    @Override
    public int getLayout() {
        return R.layout.record_list_item;
    }

}
