package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/6.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/6
 * TODO:
 */
public class ConfrimPwdPop extends BasePopupwind {
    Context context;
    View view;
    EditText edtPhone;
    private Button btnGetCode;
    private TextView txtAccount, txtPay;

    public ConfrimPwdPop(Context context) {
        super(context);
        this.context = context;
        initView();
        setUserData();
    }

    private void setUserData() {
        Bean_UserInfo.GameUser userInfo = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
        if (userInfo != null) {
            if (TextUtils.isEmpty(userInfo.UserBalance))
                account = 0f;
            else account = Float.parseFloat(userInfo.UserBalance);
            txtAccount.setText("账户余额:"+account + " 金币");
        }
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.confirm_pwd_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone);

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {

                }
            }
        });
        txtAccount = (TextView) view.findViewById(R.id.txt_account_money);
        txtPay = (TextView) view.findViewById(R.id.txt_pay_money);
        btnGetCode = (Button) view.findViewById(R.id.btn_get_code);
        btnGetCode.setOnClickListener(this);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        this.setContentView(view);
    }


    public void showPop(View parent) {
        this.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }


    String phone;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_login:
                phone = edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Tools.toastMsg((Activity) context, "密码不能为空");
                    return;
                }

                commitData();
                break;
            case R.id.btn_get_code:
                popInterfacer.OnCancle(flag);
                dismiss();
                break;
        }
    }

    private void commitData() {
        Bundle bundle = new Bundle();
        bundle.putString("pwd", phone);
        if (money > account)
            popInterfacer.OnConfirm(flag, null);
        else
            popInterfacer.OnConfirm(flag, bundle);
    }

    private float money=0, account=1;

    public void setMoney(float money) {
        this.money = money;
        txtPay.setText("需支付:"+money + " 金币");
    }
}
