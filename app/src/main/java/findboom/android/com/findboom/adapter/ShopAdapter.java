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
import findboom.android.com.findboom.widget.StrokeTextView;

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
        if (position < 3) {
            Bean_ShopList.GoodsInfo goodsInfo = (Bean_ShopList.GoodsInfo) dataList.get(position);
            mHolder.setText(R.id.txt_name, goodsInfo.ArmTypeTxt);
            mHolder.setStrokeText(R.id.txt_price, (int) goodsInfo.Price + " 金币");
            int id = 0;
            switch (goodsInfo.ArmType) {
                case 0:
                    id = R.mipmap.boom;
                    break;
                case 1:

                    break;
                case 2:
                    id = R.mipmap.icon_scan;
                    break;
                case 4:
                    id = R.mipmap.defense;
                    break;
            }
            mHolder.setImage(R.id.img_photo, id);
            mHolder.setOnClick(R.id.btn_buy, position);
            ((StrokeTextView) mHolder.getView(R.id.txt_price)).setTextSize(16);
            mHolder.getView(R.id.btn_buy).setVisibility(View.VISIBLE);
            mHolder.getView(R.id.img_photo).setVisibility(View.VISIBLE);
            mHolder.getView(R.id.btn_buy).setBackgroundResource(R.mipmap.btn_buy);
        } else {
            if (position == dataList.size() - 1) {
                mHolder.setStrokeText(R.id.txt_price, "期待升级中...");
                ((StrokeTextView) mHolder.getView(R.id.txt_price)).setTextSize(12);
                mHolder.setText(R.id.txt_name, "");
                mHolder.getView(R.id.btn_buy).setVisibility(View.INVISIBLE);
                mHolder.getView(R.id.img_photo).setVisibility(View.INVISIBLE);
            } else {
                mHolder.setText(R.id.txt_name, "");
                mHolder.setStrokeText(R.id.txt_price, " . . . ");
                mHolder.setImage(R.id.img_photo, R.mipmap.icon_boom_other);
                mHolder.getView(R.id.btn_buy).setBackgroundResource(R.mipmap.btn_buy_gray);
                ((StrokeTextView) mHolder.getView(R.id.txt_price)).setTextSize(16);
                mHolder.getView(R.id.btn_buy).setVisibility(View.VISIBLE);
                mHolder.getView(R.id.img_photo).setVisibility(View.VISIBLE);
            }

        }


    }


    @Override
    public int getLayout() {
        return R.layout.shop_list_item;
    }

}
