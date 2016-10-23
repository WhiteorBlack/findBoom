package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_FriendList;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.CircleImageView;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class FriendAdapter extends BaseRecyAdapter {
    private OnClickInterface onClickInterface;

    public FriendAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public void setOnclick(OnClickInterface l) {
        this.onClickInterface = l;
    }


    public FriendAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        BaseRecyAdapter.ViewHolder mHolder = (BaseRecyAdapter.ViewHolder) holder;
        if (onClickInterface != null) {
            mHolder.itemView.findViewById(R.id.img_talk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickInterface.onClick(v, position);
                }
            });
        }

        Bean_FriendList.Friend friend = (Bean_FriendList.Friend) dataList.get(position);
        mHolder.setText(R.id.txt_name, friend.FriendNickName);
        mHolder.setRadiusImage(R.id.img_photo, friend.Avatar);

    }


    @Override
    public int getLayout() {
        return R.layout.friend_list_item;
    }

}
