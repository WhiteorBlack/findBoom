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
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;

import findboom.android.com.findboom.BaseActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_AllConfig;
import findboom.android.com.findboom.dailog.SelectBoomPic;
import findboom.android.com.findboom.dailog.SelectBoomText;
import findboom.android.com.findboom.dailog.SelectBoomType;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:
 */
public class PutCommenBoom extends Activity implements PopInterfacer, View.OnClickListener {
    private ImageView imgBoom;
    private TextView txtType, txtIntro, txtInfo, txtDistance, txtTime, txtReduceRecord, txtAddRecord;
    private String[] typeList = new String[]{"普通雷", "文字雷", "图片雷"};
    private int type = 0;
    private SelectBoomType selectBoomType;
    private SelectBoomText selectBoomText;
    private SelectBoomPic selectBoomPic;
    private Context context;
    private Bean_AllConfig.CommonBoom commonBoom;
    private Bean_AllConfig.TextBoom textBoom;
    private Bean_AllConfig.PicBoom picBoom;
    private String configString;
    private String picUrl, picInfo, textInfo;
    private int rang=50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.put_common_boom_pop);
        context = this;
        initView();
        initData();
    }

    public void setChoice(int type) {
        switch (type) {
            case 0:
                txtInfo.setText(commonBoom.ArmDesc);
                txtTime.setText(commonBoom.ValidDays + " 天");
                txtReduceRecord.setText(commonBoom.MinusScore + "积分");
                txtAddRecord.setText(commonBoom.PlusScore + "积分");
                txtDistance.setText(commonBoom.BombRange + " m");
                txtIntro.setText("");
                Glide.with(context).load(R.mipmap.boom_detial).into(imgBoom);
                rang=commonBoom.BombRange;
                break;
            case 1:
                rang=textBoom.BombRange;
                txtInfo.setText(textBoom.ArmDesc);
                txtTime.setText(textBoom.ValidDays + " 天");
                txtReduceRecord.setText(textBoom.MinusScore + "积分");
                txtAddRecord.setText(textBoom.PlusScore + "积分");
                txtDistance.setText(textBoom.BombRange + " m");
                if (textBoom.Texts != null && textBoom.Texts.length > 0) {
                    textInfo = textBoom.Texts[0];
                    txtIntro.setText(textInfo);
                    Glide.with(context).load(R.mipmap.boom_detial).into(imgBoom);
                }
                break;
            case 2:
                rang=picBoom.BombRange;
                txtInfo.setText(picBoom.ArmDesc);
                txtTime.setText(picBoom.ValidDays + " 天");
                txtReduceRecord.setText(picBoom.MinusScore + "积分");
                txtAddRecord.setText(picBoom.PlusScore + "积分");
                txtDistance.setText(picBoom.BombRange + " m");
                if (picBoom.MinePics != null && picBoom.MinePics.size() > 0) {
                    picInfo = picBoom.MinePics.get(0).PicTitle;
                    picUrl = picBoom.MinePics.get(0).PicUrl;
                    txtIntro.setText(picInfo);
                    Glide.with(context).load(picUrl).into(imgBoom);

                }
                break;
        }
    }

    private Bean_AllConfig bean_config;

    private void initData() {
        configString = getIntent().getStringExtra("config");
        if (!TextUtils.isEmpty(configString)) {
            bean_config = new Gson().fromJson(configString, Bean_AllConfig.class);
            commonBoom = bean_config.Data.CommonMineConfig;
            textBoom = bean_config.Data.TextMineConfig;
            picBoom = bean_config.Data.PicMineConfig;
            setChoice(0);

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
        imgBoom.setOnClickListener(this);
        txtAddRecord = (TextView) findViewById(R.id.txt_add_record);
        txtDistance = (TextView) findViewById(R.id.txt_boom_distance);
        txtInfo = (TextView) findViewById(R.id.txt_info);
        txtIntro = (TextView) findViewById(R.id.txt_boom_intro);
        txtIntro.setOnClickListener(this);
        txtReduceRecord = (TextView) findViewById(R.id.txt_reduce_record);
        txtTime = (TextView) findViewById(R.id.txt_boom_time);
        txtType = (TextView) findViewById(R.id.txt_type);
        txtType.setOnClickListener(this);
    }


    public void boomClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.btn_confirm:
                Bundle bundle = new Bundle();
                bundle.putString("type", type + "");
                bundle.putString("imgUrl", picUrl);
                bundle.putString("imgInfo", picInfo);
                bundle.putString("text", textInfo);
                bundle.putInt("rang",rang);
                setResult(RESULT_OK, new Intent().putExtra("data", bundle));
                finish();
                break;

        }
    }

    @Override
    public void OnDismiss(int flag) {

    }

    @Override
    public void OnConfirm(int flag, Bundle bundle) {

        switch (flag) {
            case 0:
                type = bundle.getInt("pos");
                txtType.setText(typeList[type]);
                setChoice(type);
                break;
            case 1:
                textInfo = textBoom.Texts[bundle.getInt("pos")];
                txtIntro.setText(textInfo);
                break;
            case 2:
                picInfo = picBoom.MinePics.get(bundle.getInt("pos")).PicTitle;
                txtIntro.setText(picInfo);
                picUrl = picBoom.MinePics.get(bundle.getInt("pos")).PicUrl;
                Glide.with(context).load(picUrl).into(imgBoom);
                break;
        }


    }

    @Override
    public void OnCancle(int flag) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_type:
                if (selectBoomType == null)
                    selectBoomType = new SelectBoomType(context);
                selectBoomType.showAsDropDown(txtType, -Tools.dip2px(context, 30), 0);
                selectBoomType.setPopInterfacer(PutCommenBoom.this);
                break;
            case R.id.txt_boom_intro:
                if (type == 1) {
                    if (selectBoomText == null)
                        selectBoomText = new SelectBoomText(context);
                    selectBoomText.setStrings(textBoom.Texts);
                    selectBoomText.showAsDropDown(txtIntro, -Tools.dip2px(context, 30), 0);
                    selectBoomText.setPopInterfacer(PutCommenBoom.this);
                }
                break;
            case R.id.img_boom:
                if (type == 2) {
                    Tools.debug("pic");
                    if (selectBoomPic == null)
                        selectBoomPic = new SelectBoomPic(context);
                    selectBoomPic.setStrings(picBoom.MinePics);
                    selectBoomPic.showAsDropDown(imgBoom, -Tools.dip2px(context, 58), 0);
                    selectBoomPic.setPopInterfacer(PutCommenBoom.this);
                }
                break;
        }
    }
}
