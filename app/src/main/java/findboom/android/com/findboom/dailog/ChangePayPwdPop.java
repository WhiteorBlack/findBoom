package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * author:${白曌勇} on 2016/9/10
 * TODO:修改支付密码
 */
public class ChangePayPwdPop extends BasePopupwind {
    private Context context;
    private View view;
    EditText edtPhone;
    private Button btnGetCode;
    //个人资料
    EditText edtFirst, edtSecond;

    public ChangePayPwdPop(Context context) {
        super(context);
        this.context = context;
        initView();
        sendCode(AppPrefrence.getUserPhone(context));
    }


    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.change_pwd_pop, null);
        edtFirst = (EditText) view.findViewById(R.id.edt_first);
        edtSecond = (EditText) view.findViewById(R.id.edt_second);
        view.findViewById(R.id.img_first_clear).setOnClickListener(this);
        view.findViewById(R.id.img_clear).setOnClickListener(this);
        view.findViewById(R.id.btn_advice).setOnClickListener(this);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone);
        btnGetCode = (Button) view.findViewById(R.id.btn_get_code);
        btnGetCode.setOnClickListener(this);
        this.setContentView(view);
    }


    private String firstString, secondString, code;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.img_first_clear:
                edtFirst.setText("");
                break;
            case R.id.img_clear:
                edtSecond.setText("");
                break;
            case R.id.btn_advice:
                firstString = edtFirst.getText().toString();
                secondString = edtSecond.getText().toString();
                code = edtPhone.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Tools.toastMsg(context, "请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(firstString)) {
                    Tools.toastMsg(context, "请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(secondString)) {

                    Tools.toastMsg(context, "请输入确认密码");
                    return;
                }
                if (!TextUtils.equals(firstString, secondString)) {
                    Tools.toastMsg(context, "两次输入的密码不一致");
                    return;
                }
                commitData();
                break;
            case R.id.btn_get_code:
                countTime();
                sendCode(AppPrefrence.getUserPhone(context));
                break;
        }
    }

    @Override
    public void showPop(View parent) {
        super.showPop(parent);
        countTime();
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
        params.put("NewPayPassWord", firstString);
        params.put("ConfirmPassword", secondString);
        params.put("UserCode", code);
        PostTools.postData(context, CommonUntilities.USER_URL + "FindPayPassWord", params, new PostCallBack() {
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
                    AppPrefrence.setIsPayPwd(context, true);
                    Tools.toastMsg(context, baseBean.Msg);
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
        params.put("SmsType", "1");
        PostTools.postData(context, CommonUntilities.LOGIN_URL, params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
            }
        });
    }

}
