package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/6.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.UserNotify;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/6
 * TODO:
 */
public class InputCodePop extends BasePopupwind {
    Context context;
    View view;
    EditText edtPhone;
    private Button btnGetCode;

    public InputCodePop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.input_code_pop, null);
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

        btnGetCode = (Button) view.findViewById(R.id.btn_get_code);
        btnGetCode.setOnClickListener(this);
        btnGetCode.setText("获取验证码");
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        this.setContentView(view);
    }


    public void showPop(View parent) {
        this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        countTime();
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
                    Tools.toastMsg((Activity) context, "验证码不能为空");
                    return;
                }

                commitData();
                break;
            case R.id.btn_get_code:
                popInterfacer.OnCancle(flag);
                countTime();
                break;
        }
    }

    private void countTime() {
        new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetCode.setClickable(false);
                btnGetCode.setText(millisUntilFinished / 1000 + "秒后重试");
                btnGetCode.setTextColor(Color.WHITE);
                btnGetCode.setTextSize(14);
            }

            @Override
            public void onFinish() {
                btnGetCode.setClickable(true);
                btnGetCode.setText("重新获取");
            }
        }.start();
    }

    private void commitData() {
        Bundle bundle = new Bundle();
        bundle.putString("code", phone);
        popInterfacer.OnConfirm(flag, bundle);
    }
}
