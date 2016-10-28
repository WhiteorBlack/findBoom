package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_FriendList;
import findboom.android.com.findboom.bean.Bean_ShopList;
import findboom.android.com.findboom.interfacer.OnClickInterface;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class ShopAdapter extends BaseRecyAdapter {

    public ShopAdapter(Context context, List dataList) {
        super(context, dataList);
    }


    public ShopAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        Bean_ShopList.GoodsInfo goodsInfo = (Bean_ShopList.GoodsInfo) dataList.get(position);
        mHolder.setText(R.id.txt_name, goodsInfo.ArmTypeTxt);
        mHolder.setStrokeText(R.id.txt_price, goodsInfo.Price + " 金币");
        int id = 0;
        switch (goodsInfo.ArmType) {
            case 0:
                id = R.mipmap.icon_boom;
                break;
            case 1:

                break;
            case 2:
                id = R.mipmap.icon_scan;
                break;
            case 4:
                id = R.mipmap.icon_defense;
                break;
        }
        mHolder.setImage(R.id.img_photo, id);
        mHolder.setOnClick(R.id.btn_buy, position);

    }


    @Override
    public int getLayout() {
        return R.layout.shop_list_item;
    }

}
