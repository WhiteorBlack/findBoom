package findboom.android.com.findboom.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.transition.Transition;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.BaseFragmentActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_BoomDetial;
import findboom.android.com.findboom.fragment.GoldBoomFragment;
import findboom.android.com.findboom.fragment.NormalBoomFragment;
import findboom.android.com.findboom.fragment.RedBoomFragment;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2016/9/29.
 * 所有雷记录详情
 */
public class FindBoomDetial extends BaseFragmentActivity {
    private ImageView imgType;
    private TextView txtBoomType, txtBoomState;
    private TextView txtRemark, txtInfo;
    private ImageView imgPhoto;
    private String boomId;
    private int type = 0;
    private boolean isMine = true;
    private FragmentManager fragmentManager;

    private NormalBoomFragment normalBoomFragment;
    private RedBoomFragment redBoomFragment;
    private GoldBoomFragment goldBoomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_record_detial);
        initView();
//        getBoomInfo();
    }

    private void setChioce(int type) {
        FragmentTransaction transition = fragmentManager.beginTransaction();
        clearAll(transition);
        switch (type) {
            case 6:
                //基础雷：包含 普通雷、文字雷、图片雷
                if (normalBoomFragment == null) {
                    normalBoomFragment = new NormalBoomFragment();
                    normalBoomFragment.setInfo(boomId, type, isMine);
                    transition.add(R.id.fl_content, normalBoomFragment);
                } else transition.show(normalBoomFragment);

                break;
            case 3:
                //红包雷
                if (redBoomFragment == null) {
                    redBoomFragment = new RedBoomFragment();
                    redBoomFragment.setInfo(boomId, type, isMine);
                    transition.add(R.id.fl_content, redBoomFragment);
                } else transition.show(redBoomFragment);
                break;
            case 4:
                //寻宝雷
                if (goldBoomFragment == null) {
                    goldBoomFragment = new GoldBoomFragment();
                    goldBoomFragment.setInfo(boomId, type, isMine);
                    transition.add(R.id.fl_content, goldBoomFragment);
                } else transition.show(goldBoomFragment);
                break;
            default:
                if (normalBoomFragment == null) {
                    normalBoomFragment = new NormalBoomFragment();
                    normalBoomFragment.setInfo(boomId, type, isMine);
                    transition.add(R.id.fl_content, normalBoomFragment);
                } else transition.show(normalBoomFragment);
                break;
        }
        transition.commit();
    }

    private void clearAll(FragmentTransaction transition) {
        if (normalBoomFragment != null)
            transition.hide(normalBoomFragment);
        if (redBoomFragment != null)
            transition.hide(redBoomFragment);
        if (goldBoomFragment != null)
            transition.hide(goldBoomFragment);
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        boomId = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        isMine = getIntent().getBooleanExtra("isMine", true);
        imgType = (ImageView) findViewById(R.id.img_type);

        txtInfo = (TextView) findViewById(R.id.txt_boom_info);
        txtRemark = (TextView) findViewById(R.id.txt_boom_text);
        imgPhoto = (ImageView) findViewById(R.id.img_photo);
        setChioce(type);
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
            txtInfo.append("我在" + Tools.getSpanString(this, bean_BoomDetial.Data.Address, R.color.content_yellow) + " 踩到了 " + Tools.getSpanString(this, bean_BoomDetial.Data.UserNickName, R.color.content_yellow) + " 埋的雷,");
            if (bean_BoomDetial.Data.IsHaveBombSuit) {
                txtInfo.append("但是被我的防弹衣成功防御。");
            } else
                txtInfo.append("减少我 " + Tools.getSpanString(this, bean_BoomDetial.Data.MinusScore + "积分", R.color.content_yellow) + "为 " + Tools.getSpanString(this, bean_BoomDetial.Data.UserNickName, R.color.content_yellow) + " 增加" +
                        Tools.getSpanString(this, bean_BoomDetial.Data.PlusScore + "积分", R.color.content_yellow) + "。");
        } else {

        }
    }

    public void setState(String type, String state) {
        txtBoomState.setText(state);
        txtBoomType.setText(type);
    }
}
