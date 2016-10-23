package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/6.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.UserNotify;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/6
 * TODO:
 */
public class InputPhonePop extends BasePopupwind {
    Context context;
    View view;
    EditText edtPhone;
    ImageView imgClear;

    public InputPhonePop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.input_phone_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        imgClear = (ImageView) view.findViewById(R.id.img_clear);
        imgClear.setOnClickListener(this);
        imgClear.setVisibility(View.GONE);
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
                    imgClear.setVisibility(View.VISIBLE);

                }
            }
        });

        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.txt_notify).setOnClickListener(this);
        this.setContentView(view);
    }


    public void showPop(View parent) {
        this.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }


    String phone;
    private SendCodeConfirmPop sendCodeConfirmPop;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();

                break;
            case R.id.img_clear:
                edtPhone.setText("");
                imgClear.setVisibility(View.GONE);
                break;
            case R.id.txt_notify:
                context.startActivity(new Intent(context, UserNotify.class));
                break;
            case R.id.btn_login:
                phone = edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Tools.toastMsg((Activity) context, "手机号码不能为空");
                    return;
                }
                if (!Tools.isMobileNum(phone)) {
                    Tools.toastMsg((Activity) context, "请输入正确的手机号码");
                    return;
                }

                commitData();
                break;
        }
    }

    private void commitData() {
        Bundle bundle=new Bundle();
        bundle.putString("phone",phone);
        popInterfacer.OnConfirm(flag,bundle);
    }
}
