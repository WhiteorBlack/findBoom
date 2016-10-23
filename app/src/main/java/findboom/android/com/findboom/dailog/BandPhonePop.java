package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/11.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.BaseBean;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import okhttp3.Call;

/**
 * author:${白曌勇} on 2016/9/11
 * TODO:绑定手机号码弹窗
 */
public class BandPhonePop extends BasePopupwind {
    private Context context;
    private View view;
    private EditText edtPhone, edtCode;
    private Button btnGetCode;

    public BandPhonePop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private String code, phone;

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.band_phone_pop, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        edtCode = (EditText) view.findViewById(R.id.edt_code);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone);
        btnGetCode = (Button) view.findViewById(R.id.btn_get_code);
        btnGetCode.setOnClickListener(this);
        this.setContentView(view);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_get_code:
                phone = edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Tools.toastMsg(context, "请输入手机号码");
                    return;
                }
                sendCode(phone);
                countTime();
                break;
            case R.id.btn_confirm:
                code = edtCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Tools.toastMsg(context, "请输入验证码");
                    return;
                }
                commitData();
                break;
        }
    }

    private void countTime() {
        new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetCode.setClickable(false);
                btnGetCode.setBackgroundResource(R.drawable.get_code_bg);
                btnGetCode.setText(millisUntilFinished / 1000 + "秒后重试");
                btnGetCode.setTextColor(Color.WHITE);
                btnGetCode.setTextSize(14);
            }

            @Override
            public void onFinish() {
                btnGetCode.setClickable(true);
                btnGetCode.setText("");
                btnGetCode.setBackgroundResource(R.mipmap.btn_get_code);
            }
        }.start();
    }

    private void commitData() {
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumber", phone);
        params.put("UserCode", code);
        PostTools.postData(context, CommonUntilities.USER_URL + "BindPhoneNumber", params, new PostCallBack() {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                Tools.debug(e.toString());
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response))
                    return;
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if (baseBean.Success) {
                    AppPrefrence.setUserPhone(context, phone);
                    Tools.toastMsg(context, baseBean.Msg);
                    if (isShowing())
                        dismiss();
                } else {
                    Tools.toastMsg(context, baseBean.Msg);
                }
            }
        });
    }

    private void sendCode(String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumber", phone);
        params.put("SmsType", "2");
        PostTools.postData(context, CommonUntilities.LOGIN_URL, params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
            }
        });
    }
}
