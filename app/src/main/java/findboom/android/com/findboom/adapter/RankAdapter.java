package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_Rank;
import findboom.android.com.findboom.bean.Bean_ShopList;
import findboom.android.com.findboom.interfacer.OnClickInterface;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class RankAdapter extends BaseRecyAdapter {

    public RankAdapter(Context context, List dataList) {
        super(context, dataList);
    }


    public RankAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;

        Bean_Rank.RankUser rankUser = (Bean_Rank.RankUser) dataList.get(position);
        mHolder.setText(R.id.txt_name, rankUser.UserNickName);
        mHolder.setText(R.id.txt_rank, position == 9 ? "10" : "0" + (1 + position));
        mHolder.setRadiusImage(R.id.img_photo, rankUser.Avatar);
    }


    @Override
    public int getLayout() {
        return R.layout.record_rank_item;
    }

}
