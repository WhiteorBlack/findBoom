package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2017/1/6.
 */

public class RegisterPop extends BasePopupwind {
    private View view;
    private EditText edtCode, edtPwd, edtPwdTwo, edtPhone;
    private Button btnGetCode;
    private String phone, pwd, pwdTwo, code;

    public RegisterPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.register_pop, null);
        edtCode = (EditText) view.findViewById(R.id.edt_code);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone);
        edtPwd = (EditText) view.findViewById(R.id.edt_pwd);
        edtPwdTwo = (EditText) view.findViewById(R.id.edt_pwd_two);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        view.findViewById(R.id.txt_notify).setOnClickListener(this);
        btnGetCode = (Button) view.findViewById(R.id.btn_get_code);
        btnGetCode.setOnClickListener(this);
        btnGetCode.setText("获取验证码");
        this.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_login:
                code = edtCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Tools.toastMsgCenter(context, "请输入验证码");
                    return;
                }
                bundle.putString("code", code);
                phone = edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Tools.toastMsgCenter(context, "请输入手机号码");
                    return;
                }
                if (!Tools.isMobileNum(phone)) {
                    Tools.toastMsgCenter(context, "请输入正确的手机号码");
                    return;
                }

                bundle.putString("phone", phone);
                pwd = edtPwd.getText().toString();
                pwdTwo = edtPwdTwo.getText().toString();
                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdTwo)) {
                    Tools.toastMsgCenter(context, "请输入密码");
                    return;
                }
                if (!TextUtils.equals(pwd, pwdTwo)) {
                    Tools.toastMsgCenter(context, "两次输入的密码不一致");
                    return;
                }
                bundle.putString("pwd", Tools.get32MD5Str(pwd));
                bundle.putInt("type", 0);
                break;
            case R.id.txt_notify:
                bundle.putInt("type", 1);
                break;

            case R.id.btn_get_code:
                bundle.putInt("type",-1);
                phone=edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Tools.toastMsgCenter(context, "请输入手机号码");
                    return;
                }
                if (!Tools.isMobileNum(phone)) {
                    Tools.toastMsgCenter(context, "请输入正确的手机号码");
                    return;
                }
                sendCode(phone);
                countTime();
                break;

        }
        if (popInterfacer != null)
            popInterfacer.OnConfirm(flag, bundle);
    }

    private void countTime() {
        new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetCode.setClickable(false);
                btnGetCode.setText(millisUntilFinished / 1000 + "秒后重试");
                btnGetCode.setTextSize(14);
            }

            @Override
            public void onFinish() {
                btnGetCode.setClickable(true);
                btnGetCode.setText("重新获取");
            }
        }.start();
    }

    private void sendCode(String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumber", phone);
        params.put("SmsType", "0");
        PostTools.postData(context, CommonUntilities.LOGIN_URL, params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
            }
        });
    }
}
