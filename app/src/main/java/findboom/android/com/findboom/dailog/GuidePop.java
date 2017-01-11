package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/12/30.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_OtherConfig;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import okhttp3.Call;

/**
 * author:${白曌勇} on 2016/12/30
 * TODO:
 */
public class GuidePop extends BasePopupwind {
    private View view;
    private TextView txtContent;
    private LinearLayout llParent;

    public GuidePop(Context context) {
        super(context);
        initView();
        getGuideData();
    }

    private void getGuideData() {
        Map<String, String> params = new HashMap<>();
//        params.put("", "");
        PostTools.getData(context, CommonUntilities.CONFIG_URL + "GetOtherConfig", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("guide--"+response);
                if (TextUtils.isEmpty(response))
                    return;
                Bean_OtherConfig otherConfig = new Gson().fromJson(response, Bean_OtherConfig.class);
                if (otherConfig.Success) {
                    setHelp(otherConfig.Data.GameHelp);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                Tools.debug("guide error--"+e.toString());
            }
        });
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.guide_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        txtContent = (TextView) view.findViewById(R.id.txt_help);
        llParent=(LinearLayout)view.findViewById(R.id.ll_parent);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) llParent.getLayoutParams();
        params.width= (int) (Tools.getScreenWide(context)*0.95);
        params.height= (int) (Tools.getScreenHeight(context)*0.65);
        this.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        dismiss();
    }

    public void setHelp(String text) {
        if (TextUtils.isEmpty(text))
            return;
        txtContent.setText(text);
    }
}
