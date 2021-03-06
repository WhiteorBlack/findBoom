package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/11.
 */

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.StrokeTextView;

/**
 * author:${白曌勇} on 2016/9/11
 * TODO:
 */
public class ShopBuyPop extends BasePopupwind {
    private Context context;
    private View view;
    private ImageView imgGoods;
    private EditText txtCount;
    private TextView txtTitle;
    private StrokeTextView txtPrice;
    private int count = 1;

    public ShopBuyPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.shop_buy_pop, null);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        view.findViewById(R.id.fl_add).setOnClickListener(this);
        view.findViewById(R.id.fl_minute).setOnClickListener(this);
        view.findViewById(R.id.btn_buy).setOnClickListener(this);
        imgGoods = (ImageView) view.findViewById(R.id.img_goods_pic);
        txtCount = (EditText) view.findViewById(R.id.txt_count);
        txtCount.setText(count + "");
        txtTitle = (TextView) view.findViewById(R.id.txt_name);
        txtPrice = (StrokeTextView) view.findViewById(R.id.txt_price);
        this.setContentView(view);
    }

    float price = 0;

    public void setPrice(int price) {
        this.price = price;
        txtPrice.setText("￥ " + price);
    }

    public void setGoodPic(String ur) {

    }

    public void setGoodPic(int ur) {
        int id = 0;
        String name = "";
        switch (ur) {
            case 0:
                id = R.mipmap.icon_boom;
                name = "地雷";
                break;
            case 1:

                break;
            case 2:
                id = R.mipmap.icon_scan;
                name = "扫雷器";
                break;
            case 4:
                id = R.mipmap.defense_boom;
                name = "防爆衣";
                break;
        }
        txtTitle.setText(name);
        Glide.with(context).load(id).into(imgGoods);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fl_add:
                count++;
                txtCount.setText(count + "");
                txtPrice.setText("￥ " + price * count);
                break;
            case R.id.fl_minute:
                count--;
                if (count == 0)
                    count = 1;
                txtCount.setText(count + "");
                txtPrice.setText("￥ " + price * count);
                break;
            case R.id.btn_buy:
                String countString = txtCount.getText().toString();
                if (TextUtils.isEmpty(countString)) {
                    Tools.toastMsgCenter(context, "请输入要购买的个数");
                    return;
                }
                count = Integer.parseInt(countString);
                if (count < 1) {
                    Tools.toastMsgCenter(context, "购买个数最少为1");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putFloat("money", price * count);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
                break;
        }
    }
}
