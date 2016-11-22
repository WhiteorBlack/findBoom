package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.MyRedRecordDetial;

/**
 * Created by Administrator on 2016/11/22.
 * 红包雷打开
 */

public class OpenRedPop extends BasePopupwind {
    private View view;
    private TextView txtMoney, txtType, txtNotify;

    public OpenRedPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.open_red_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        view.findViewById(R.id.btn_check).setOnClickListener(this);
        txtMoney = (TextView) view.findViewById(R.id.txt_money);
        txtNotify = (TextView) view.findViewById(R.id.txt_notify);
        txtType = (TextView) view.findViewById(R.id.txt_money_type);
        this.setContentView(view);
    }

    public void setMoney(String money) {
        txtMoney.setVisibility(View.VISIBLE);
        txtMoney.setText(money);
    }

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_check:
                context.startActivity(new Intent(context, MyRedRecordDetial.class).putExtra("id", id));
                break;
        }
        dismiss();
    }
}
