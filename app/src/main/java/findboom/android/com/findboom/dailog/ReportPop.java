package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:战绩中心
 */
public class ReportPop extends BasePopupwind {
    private Context context;
    private View view;
    private String adviceString;
    private EditText edtAdvice;
    private String reportId;

    public ReportPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void setId(String id){
        this.reportId=id;
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
                    Tools.toastMsg(context, "请输入您举报的原因");
                    return;
                }
                commitAdvice();
                break;
        }
    }

    private void commitAdvice() {
        Map<String, String> params = new HashMap<>();
        params.put("ReportContent", adviceString);
        params.put("BeReportedUserId", reportId);
        PostTools.postData(context, CommonUntilities.ADVICE_URL + "AddOneReport", params, new PostCallBack() {
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
                Tools.toastMsg(context, context.getResources().getString(R.string.report_toast));
            }
        });

    }


}
