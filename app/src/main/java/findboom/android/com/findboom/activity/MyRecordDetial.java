package findboom.android.com.findboom.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.BaseActivity;
import findboom.android.com.findboom.BaseFragmentActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_BoomDetial;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2016/9/29.
 * 我埋雷记录详情
 */
public class MyRecordDetial extends BaseFragmentActivity {
    private ImageView imgType;
    private TextView txtBoomType, txtBoomState;
    private TextView txtRemark, txtInfo;
    private ImageView imgPhoto;
    private String boomId;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_record_detial);
        initView();
        getBoomInfo();
    }

    private void initView() {
        boomId = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        imgType = (ImageView) findViewById(R.id.img_type);

        txtInfo = (TextView) findViewById(R.id.txt_boom_info);
        txtRemark = (TextView) findViewById(R.id.txt_boom_text);
        imgPhoto = (ImageView) findViewById(R.id.img_photo);
    }

    private Bean_BoomDetial bean_BoomDetial;

    private void getBoomInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("mineRecordId", boomId);
        PostTools.getData(this, CommonUntilities.USER_URL + "GetMineInfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                bean_BoomDetial = new Gson().fromJson(response, Bean_BoomDetial.class);
                if (bean_BoomDetial != null && bean_BoomDetial.Success) {
                    setData();
                }
            }
        });
    }

    private void setData() {
        txtBoomState.setText(bean_BoomDetial.Data.StatusTxt);
        txtBoomType.setText(bean_BoomDetial.Data.BombTypeTxt);
        txtRemark.setText(TextUtils.isEmpty(bean_BoomDetial.Data.PicUrl) ? bean_BoomDetial.Data.Text : bean_BoomDetial.Data.PicTitle);
        txtInfo.setText("");
        txtInfo.append(bean_BoomDetial.Data.BombTime);
        if (type < 0) {

            txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.BombUserNickName, R.color.content_yellow));
            txtInfo.append(" 在" + Tools.getSpanString(this, bean_BoomDetial.Data.Address, R.color.content_yellow) + " 踩到了我埋的雷,");
            if (bean_BoomDetial.Data.IsHaveBombSuit) {
                txtInfo.append("但是被他的防弹衣成功防御,不加积分。");
            } else
                txtInfo.append("减少对方 " + Tools.getSpanString(this, bean_BoomDetial.Data.MinusScore + "积分", R.color.content_yellow) + "为我增加" +
                        Tools.getSpanString(this, bean_BoomDetial.Data.PlusScore + "积分", R.color.content_yellow) + "。");
        }else {

        }
    }
}
