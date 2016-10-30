package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/10/30.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.BaseBean;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/10/30
 * TODO:
 */
public class AddFriendPop extends BasePopupwind {
    private EditText edtContent;
    private View view;

    public AddFriendPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.add_friend_pop, null);
        view.findViewById(R.id.btn_cancle).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        edtContent = (EditText) view.findViewById(R.id.edt_content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
    }

    private String userId = "";
    String content = "";

    public void setId(String id) {
        this.userId = id;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_cancle:
                dismiss();
                break;
            case R.id.btn_confirm:
                content = edtContent.getText().toString();
                sendRequest(content);
                break;
        }
    }

    private void sendRequest(final String content) {
        Map<String, String> params = new HashMap<>();
        params.put("ToUserId", userId);
        params.put("ApplyMsg", content);
        PostTools.postData(context, CommonUntilities.FRIEND_URL + "CreateApply", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "网络错误请重试");
                    return;
                }
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if (baseBean != null && baseBean.Success) {
                    Tools.toastMsg(context, "发送成功,等待对方处理");
                    dismiss();
                } else Tools.toastMsg(context, baseBean.Msg);
            }
        });
    }
}
