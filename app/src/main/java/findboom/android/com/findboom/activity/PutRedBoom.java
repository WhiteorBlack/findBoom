package findboom.android.com.findboom.activity;/**
 * Created by Administrator on 2016/10/22.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_AllConfig;
import findboom.android.com.findboom.dailog.BandPhonePop;
import findboom.android.com.findboom.dailog.ChangePayPwdPop;
import findboom.android.com.findboom.dailog.ConfrimPwdPop;
import findboom.android.com.findboom.dailog.SelectBoomPic;
import findboom.android.com.findboom.dailog.SelectBoomText;
import findboom.android.com.findboom.dailog.SelectBoomType;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:
 */
public class PutRedBoom extends Activity implements PopInterfacer {
    private ImageView imgBoom;
    private TextView txtType, txtIntro, txtDistance, txtTime, txtReduceRecord, txtAddRecord;
    private int type = 3;
    private Context context;
    private String configString;
    private Bean_AllConfig.RedBoom redBoom;
    private float money;
    int count;
    private String remark;
    private EditText edtMoney, edtCount, edtRemark;
    private int rang = 50;
//    private ConfrimPwdPop confrimPwdPop;
//    private BandPhonePop bandPhonePop;
//    private ChangePayPwdPop changePayPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.put_red_boom_pop);
        context = this;
        initView();
        initData();
    }


    private Bean_AllConfig bean_config;

    private void initData() {
        configString = getIntent().getStringExtra("config");
        if (!TextUtils.isEmpty(configString)) {
            bean_config = new Gson().fromJson(configString, Bean_AllConfig.class);
            redBoom = bean_config.Data.RedPackMineConfig;
            rang = redBoom.CanRecRange;
            txtDistance.setText(redBoom.VisibleRange + "m");
            txtReduceRecord.setText(rang + "m");
        } else getAllConfig();

    }

    /**
     * 获取所有军火配置信息
     */
    private void getAllConfig() {
        PostTools.getData(context, CommonUntilities.CONFIG_URL + "GetArmsConfig", new HashMap<String, String>(), new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                configString = response;
                initData();
            }
        });
    }

    private void initView() {
        imgBoom = (ImageView) findViewById(R.id.img_boom);
        txtAddRecord = (TextView) findViewById(R.id.txt_add_record);
        txtDistance = (TextView) findViewById(R.id.txt_boom_distance);
        txtIntro = (TextView) findViewById(R.id.txt_boom_intro);
        txtReduceRecord = (TextView) findViewById(R.id.txt_reduce_record);
        txtTime = (TextView) findViewById(R.id.txt_boom_time);
        txtType = (TextView) findViewById(R.id.txt_type);
        edtCount = (EditText) findViewById(R.id.edt_count);
        edtMoney = (EditText) findViewById(R.id.edt_money);
        edtRemark = (EditText) findViewById(R.id.edt_remark);
    }


    public void boomClick(View v) {
        FindBoomApplication.getInstance().playClickSound();
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.btn_confirm:
                String moneyS = edtMoney.getText().toString();
                if (TextUtils.isEmpty(moneyS)) {
                    Tools.toastMsg(context, "请输入红包金额");
                    return;
                }
                money = Float.parseFloat(moneyS);
                if (money > redBoom.MaxAmount) {
                    Tools.toastMsg(context, "红包金额不能大于" + redBoom.MaxAmount + "元");
                    return;
                }
                if (money < redBoom.MinAmount) {
                    Tools.toastMsg(context, "红包金额不能大于" + redBoom.MinAmount + "元");
                    return;
                }
                String countS = edtCount.getText().toString();
                if (TextUtils.isEmpty(countS)) {
                    Tools.toastMsg(context, "请输入红包个数");
                    return;
                }
                count = Integer.parseInt(countS);
                if (count > redBoom.MaxCount) {
                    Tools.toastMsg(context, "红包数量不能大于" + redBoom.MaxCount + "个");
                    return;
                }
                if (count < 1) {
                    Tools.toastMsg(context, "红包数量不能小于1个");
                    return;
                }
                remark = edtRemark.getText().toString();
                if (TextUtils.isEmpty(remark))
                    remark = edtRemark.getHint().toString();

//                if (confrimPwdPop == null)
//                    confrimPwdPop = new ConfrimPwdPop(context);
//                confrimPwdPop.showPop(edtCount);
//                confrimPwdPop.setPopInterfacer(PutRedBoom.this, 0);
                Bundle bundle = new Bundle();
                bundle.putString("type", type + "");
                bundle.putString("count", count + "");
                bundle.putString("money", money + "");
                bundle.putString("remark", remark);
                bundle.putInt("rang", rang);
                bundle.putString("pwd", pwd);
                setResult(RESULT_OK, new Intent().putExtra("data", bundle));
                finish();
                break;

        }
    }

    @Override
    public void OnDismiss(int flag) {

    }

    private String pwd;

    @Override
    public void OnConfirm(int flag, Bundle b) {
        pwd = b.getString("pwd");
        Bundle bundle = new Bundle();
        bundle.putString("type", type + "");
        bundle.putString("count", count + "");
        bundle.putString("money", money + "");
        bundle.putString("remark", remark);
        bundle.putInt("rang", rang);
        bundle.putString("pwd", pwd);
        setResult(RESULT_OK, new Intent().putExtra("data", bundle));
        finish();

    }

    @Override
    public void OnCancle(int flag) {
        if (flag == 0) {
            //忘记密码
//            if (TextUtils.isEmpty(AppPrefrence.getUserPhone(context))) {
//                //忘记密码-->绑定手机号码
//                if (bandPhonePop == null)
//                    bandPhonePop = new BandPhonePop(context);
//                bandPhonePop.showPop(txtAddRecord);
//                bandPhonePop.setPopInterfacer(this, 22);
//            } else {
//                if (changePayPwd == null)
//                    changePayPwd = new ChangePayPwdPop(context);
//                changePayPwd.showPop(txtAddRecord);
//                changePayPwd.setPopInterfacer(this, 13);
//            }
        }
    }

}
