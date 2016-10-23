package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import findboom.android.com.findboom.widget.CircleImageView;
import okhttp3.Call;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:编辑个人资料
 */
public class CreatePayPwdPop extends BasePopupwind {
    private Context context;
    private View view;

    //个人资料
    EditText edtFirst, edtSecond;
    private ImageView imgTitle;

    public CreatePayPwdPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }


    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.create_pwd_pop, null);
        edtFirst = (EditText) view.findViewById(R.id.edt_first);
        edtSecond = (EditText) view.findViewById(R.id.edt_second);
        view.findViewById(R.id.img_first_clear).setOnClickListener(this);
        view.findViewById(R.id.img_clear).setOnClickListener(this);
        view.findViewById(R.id.btn_advice).setOnClickListener(this);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        imgTitle=(ImageView)view.findViewById(R.id.img_title);
        this.setContentView(view);
    }


    private String firstString, secondString;

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
        }
    }

    private void commitData() {
        Map<String, String> params = new HashMap<>();
        params.put("NewPayPassWord", firstString);
        params.put("ConfirmPassword", secondString);
        PostTools.postData(context, CommonUntilities.USER_URL+"SetPayPassWord", params, new PostCallBack() {
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
                    if (popInterfacer!=null){
                        popInterfacer.OnConfirm(flag,null);
                    }
                    dismiss();
                } else {
                    AppPrefrence.setIsPayPwd(context, false);
                    Tools.toastMsg(context, baseBean.Msg);
                }
            }
        });
    }

}
