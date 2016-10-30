package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.MyRecordDetial;
import findboom.android.com.findboom.adapter.BaseRecyAdapter;
import findboom.android.com.findboom.adapter.MyBoomRecordAdapter;
import findboom.android.com.findboom.adapter.MyGetBoomAdapter;
import findboom.android.com.findboom.adapter.RankAdapter;
import findboom.android.com.findboom.adapter.ViewPagerAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_MyBoomRecord;
import findboom.android.com.findboom.bean.Bean_Rank;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.xrecycleview.XRecyclerView;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:战绩中心
 */
public class AdvicePop extends BasePopupwind {
    private Context context;
    private View view;
    private String adviceString;
    private EditText edtAdvice;

    public AdvicePop(Context context) {
        super(context);
        this.context = context;
        initView();
    }


    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.advice_pop, null);
        view.findViewById(R.id.btn_advice).setOnClickListener(this);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        edtAdvice = (EditText) view.findViewById(R.id.edt_advice);
        this.setContentView(view);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_advice:
                //提交
                adviceString = edtAdvice.getText().toString();
                if (TextUtils.isEmpty(adviceString)) {
                    Tools.toastMsg(context, "请输入您对我们的意见或建议");
                    return;
                }
                commitAdvice();
                break;
        }
    }

    private void commitAdvice() {
        Map<String, String> params = new HashMap<>();
        params.put("AdviceContent", adviceString);
        PostTools.postData(context, CommonUntilities.ADVICE_URL + "AddOneAdvice", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }

            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismiss();
                Tools.toastMsg(context, context.getResources().getString(R.string.advice_toast));
            }
        });

    }


}
