package findboom.android.com.findboom.dailog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2017/1/6.
 */

public class LoginPop extends BasePopupwind {
    private View view;
    private EditText edtPhone, edtPwd;

    public LoginPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.login_pop, null);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone);
        edtPwd = (EditText) view.findViewById(R.id.edt_pwd);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        view.findViewById(R.id.txt_register).setOnClickListener(this);
        view.findViewById(R.id.img_clear).setOnClickListener(this);
        view.findViewById(R.id.txt_forget).setOnClickListener(this);
        this.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.img_close:
                bundle.putInt("type", -1);
                dismiss();
                break;
            case R.id.btn_login:
                String phone = edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Tools.toastMsgCenter(context, "手机号码不能为空");
                    return;
                }
                if (!Tools.isMobileNum(phone)) {
                    Tools.toastMsgCenter(context, "请输入正确的手机号码");
                    return;
                }

                bundle.putString("phone", phone);
                String pwd = edtPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    Tools.toastMsgCenter(context, "请输入密码");
                    return;
                }
                bundle.putString("pwd", pwd);
                bundle.putInt("type", 0);
                break;
            case R.id.txt_register:
                bundle.putInt("type", 1);
                break;
            case R.id.txt_forget:
                bundle.putInt("type", 2);
                break;
            case R.id.img_clear:
                bundle.putInt("type", -1);
                edtPhone.setText("");
                break;
        }
        if (popInterfacer != null)
            popInterfacer.OnConfirm(flag, bundle);
    }
}
