package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_GoldList;
import findboom.android.com.findboom.bean.Bean_ShopList;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class GoldAdapter extends BaseRecyAdapter {

    public GoldAdapter(Context context, List dataList) {
        super(context, dataList);
    }


    public GoldAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        Bean_GoldList.GoldList goodsInfo= (Bean_GoldList.GoldList) dataList.get(position);
        mHolder.setText(R.id.txt_gold, goodsInfo.GoldAmount+"");
        mHolder.setText(R.id.txt_price, "￥" + (int)goodsInfo.Price);
        if (TextUtils.equals(goodsInfo.Status, "0")) {
            mHolder.setText(R.id.txt_name, "在售");
        } else mHolder.setText(R.id.txt_name, "已售罄");
        mHolder.setOnClick(R.id.btn_buy, position);
    }


    @Override
    public int getLayout() {
        return R.layout.gold_list_item;
    }

}
