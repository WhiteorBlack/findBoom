package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.Home;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.LoginActivity;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/1/6.
 */

public class ChangePwdPop extends BasePopupwind {
    private View view;
    private EditText edtOldPwd, edtNewPwd;
    private TextView edtPhone;
    private String phone, pwd, pwdTwo, code;

    public ChangePwdPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.change_login_pwd_pop, null);
        edtPhone = (TextView) view.findViewById(R.id.edt_phone);
        edtPhone.setText(AppPrefrence.getUserPhone(context));
        edtOldPwd = (EditText) view.findViewById(R.id.edt_pwd);
        edtNewPwd = (EditText) view.findViewById(R.id.edt_pwd_two);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        view.findViewById(R.id.txt_notify).setOnClickListener(this);
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
                pwd = edtOldPwd.getText().toString();
                pwdTwo = edtNewPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    Tools.toastMsgCenter(context, "请输入旧密码");
                    return;
                }
                if (TextUtils.isEmpty(pwdTwo)) {
                    Tools.toastMsgCenter(context, "请输入新密码");
                    return;
                }
                changePwd();
                break;


        }
        if (popInterfacer != null)
            popInterfacer.OnConfirm(flag, bundle);
    }

    private void changePwd() {
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumber", phone);
        params.put("OldPassWord", pwd);
        params.put("NewPassWord", pwdTwo);
        PostTools.postData(context, CommonUntilities.ACCOUNT_URL + "ModifyPassWord", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("login" + response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "请检查网络后重试");
                    return;
                }

                Bean_UserInfo bean_userInfo = new Gson().fromJson(response, Bean_UserInfo.class);
                if (bean_userInfo != null && bean_userInfo.Success)
                    dismiss();
                Tools.toastMsg(context, bean_userInfo.Msg);

            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }

}
