package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/11.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import findboom.android.com.findboom.R;

/**
 * author:${白曌勇} on 2016/9/11
 * TODO:
 */
public class GoldBuyPop extends BasePopupwind {
    private Context context;
    private View view;
    private TextView txtCount, txtPrice;
    private int count = 1;
    private TextView txtMoney;

    public GoldBuyPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.gold_buy_pop, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.fl_add).setOnClickListener(this);
        view.findViewById(R.id.fl_minute).setOnClickListener(this);
        view.findViewById(R.id.btn_buy).setOnClickListener(this);
        txtCount = (TextView) view.findViewById(R.id.txt_count);
        txtCount.setText(count + "");
        txtMoney = (TextView) view.findViewById(R.id.txt_money);
        txtPrice = (TextView) view.findViewById(R.id.txt_price);
        this.setContentView(view);
    }

    float price = 0;

    public void setPrice(float price) {
        this.price = price;
        txtPrice.setText("￥ " + price);
    }

    public void setMoney(String money) {
        txtMoney.setText(money);
    }

    public void setGoodPic(String ur) {

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
                if (count < 0)
                    count = 1;
                txtCount.setText(count + "");
                txtPrice.setText("￥ " + price * count);
                break;
            case R.id.btn_buy:
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putString("money", txtPrice.getText().toString());
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
                break;
        }
    }
}
