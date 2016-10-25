package findboom.android.com.findboom.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private TextView imgType;
    private TextView txtBoomType, txtBoomState;
    private TextView txtRemark, txtInfo;
    private ImageView imgPhoto;
    private String boomId;
    private int type = 0;
    private boolean isMine = false; //标示是不是我埋的雷

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
        isMine = getIntent().getBooleanExtra("isMine", false);
        imgType = (TextView) findViewById(R.id.txt_type);

        txtInfo = (TextView) findViewById(R.id.txt_boom_info);
        txtRemark = (TextView) findViewById(R.id.txt_boom_text);
        imgPhoto = (ImageView) findViewById(R.id.img_photo);

        txtBoomState = (TextView) findViewById(R.id.txt_boom_state);
        txtBoomType = (TextView) findViewById(R.id.txt_boom_type);
    }

    @Override
    public void boomClick(View v) {
        super.boomClick(v);
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
        }
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
        imgType.setText(bean_BoomDetial.Data.MineTypeTxt);
        txtBoomState.setText(bean_BoomDetial.Data.StatusTxt);
        txtBoomType.setText(bean_BoomDetial.Data.BombTypeTxt);
        txtRemark.setText(TextUtils.isEmpty(bean_BoomDetial.Data.PicUrl) ? bean_BoomDetial.Data.Text : bean_BoomDetial.Data.PicTitle);
        if (!TextUtils.isEmpty(bean_BoomDetial.Data.PicUrl)) {
            Glide.with(this).load(bean_BoomDetial.Data.PicUrl).into(imgPhoto);
        }
        txtInfo.setText("");
        if (isMine) {
            if (bean_BoomDetial.Data.Status > 0) {
                if (bean_BoomDetial.Data.BombType == 0) {
                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.BombUserNickName, Color.rgb(240, 165, 9)));
                    txtInfo.append("在 ");
                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
                    txtInfo.append(" 踩到了我埋的雷,");
                    if (bean_BoomDetial.Data.IsHaveBombSuit) {
                        txtInfo.append("但是被他的防爆衣成功防御,不加积分。");
                    } else {
                        txtInfo.append("减少对方 ");
                        txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.MinusScore, Color.rgb(240, 165, 9)));
                        txtInfo.append("积分,");
                        txtInfo.append("为我增加 ");
                        txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.PlusScore, Color.rgb(240, 165, 9)));
                        txtInfo.append("积分。");
                    }
                } else {
                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.BombUserNickName, Color.rgb(240, 165, 9)));
                    txtInfo.append(" 使用扫雷器在 ");
                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
                    txtInfo.append(" 扫到到了我埋的雷,并成功排除");
                }

            } else {
                txtInfo.append("我在 ");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
                txtInfo.append(" 埋下了一颗 ");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.MineTypeTxt, Color.rgb(240, 165, 9)));
            }
        } else {
            if (bean_BoomDetial.Data.BombType == 0) {
                txtInfo.append(" 我在");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
                txtInfo.append(" 踩到了");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.UserNickName, Color.rgb(240, 165, 9)));
                txtInfo.append(" 埋的 ");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.MineTypeTxt, Color.rgb(240, 165, 9)));
                if (bean_BoomDetial.Data.IsHaveBombSuit) {
                    txtInfo.append("但是被我的防弹衣成功防御。");
                } else {
                    txtInfo.append("减少我 ");
                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.MinusScore, Color.rgb(240, 165, 9)));
                    txtInfo.append("积分,");
                    txtInfo.append("为对方增加 ");
                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.PlusScore, Color.rgb(240, 165, 9)));
                    txtInfo.append("积分。");
                }
            } else {
                txtInfo.append(" 我使用扫雷器在 ");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
                txtInfo.append(" 扫到到了");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.BombUserNickName, Color.rgb(240, 165, 9)));
                txtInfo.append(" 埋的雷,并成功排除");
            }

        }
    }
}
