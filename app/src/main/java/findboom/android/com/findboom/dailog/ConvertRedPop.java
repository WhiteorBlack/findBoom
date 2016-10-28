package findboom.android.com.findboom.dailog;
/**
 * Created by Administrator on 2016/9/6.
 */

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/6
 * TODO:
 */
public class ConvertRedPop extends BasePopupwind {
    Context context;
    View view;
    private EditText edtMoney, edtName, edtAlipay;
    private String money;

    public ConvertRedPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.convert_red_pop, null);
        edtMoney = (EditText) view.findViewById(R.id.editText);
        edtAlipay = (EditText) view.findViewById(R.id.edt_alipay);
        edtName = (EditText) view.findViewById(R.id.edt_name);
        view.findViewById(R.id.btn_pay).setOnClickListener(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_pay:
                String name = edtName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Tools.toastMsg(context, "请输入支付宝户名");
                    return;
                }
                bundle.putString("name", name);
                String money = edtMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    Tools.toastMsg(context, "请输入提现的金额");
                    return;
                }
                bundle.putString("money", money);
                String alipay = edtAlipay.getText().toString();
                if (TextUtils.isEmpty(alipay)) {
                    Tools.toastMsg(context, "请输入支付宝账号");
                    return;
                }
                bundle.putString("alipay", alipay);
                bundle.putInt("type", 0);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
                break;
        }
    }

}
