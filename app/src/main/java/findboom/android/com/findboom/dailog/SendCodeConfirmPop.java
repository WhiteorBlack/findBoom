package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/7.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import findboom.android.com.findboom.R;

/**
 * author:${白曌勇} on 2016/9/7
 * TODO:
 */
public class SendCodeConfirmPop extends BasePopupwind implements View.OnClickListener {
    private Context context;
    private TextView txtPhone;
    private View view;

    public SendCodeConfirmPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.login_send_pop, null);
        txtPhone = (TextView) view.findViewById(R.id.txt_phone);
        view.findViewById(R.id.btn_cancle).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        this.setContentView(view);
    }

    String phone;
    public void setPhone(String phone){
        this.phone=phone;
        txtPhone.setText(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle:
                dismiss();
                break;
            case R.id.btn_confirm:
                popInterfacer.OnConfirm(flag,null);
                break;
        }
    }
}
