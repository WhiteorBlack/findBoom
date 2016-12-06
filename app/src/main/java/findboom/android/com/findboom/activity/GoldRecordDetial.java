package findboom.android.com.findboom.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.BaseFragmentActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_BoomDetial;
import findboom.android.com.findboom.dailog.AddFriendPop;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.RadiusImageView;

/**
 * Created by Administrator on 2016/9/29.
 * 我埋雷记录详情
 */
public class GoldRecordDetial extends BaseFragmentActivity implements PopInterfacer {
    private AddFriendPop addFriendPop;
    private TextView txtInfo;
    private RadiusImageView imgPhoto;
    private String boomId;
    private boolean isMine = false; //标示是不是我埋的雷
    private ImageView imgClose, imgDetial;
    private TextView txtCount, txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gold_record_detial);
        initView();
        getBoomInfo();
    }

    private void initView() {
        boomId = getIntent().getStringExtra("id");
        isMine = getIntent().getBooleanExtra("isMine", false);
        imgClose = (ImageView) findViewById(R.id.img_close);
        imgDetial = (ImageView) findViewById(R.id.img_detial);
//        if (isMine) {
//            imgDetial.setVisibility(View.VISIBLE);
//        } else {
//            imgDetial.setVisibility(View.GONE);
//        }
        txtContent = (TextView) findViewById(R.id.txt_content);
        int wide = (int) (Tools.getScreenWide(this) * 0.5);
        txtCount = (TextView) findViewById(R.id.txt_count);
        txtInfo = (TextView) findViewById(R.id.txt_info);
        imgPhoto = (RadiusImageView) findViewById(R.id.img_pic);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imgPhoto.getLayoutParams();
        params.width = wide;
        params.height = (int) (wide * 4 / 3);
        imgPhoto.setLayoutParams(params);
        imgPhoto.setType(RadiusImageView.TYPE_ROUND);
        imgPhoto.setBorderRadius(20);
    }

    @Override
    public void boomClick(View v) {
        super.boomClick(v);
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_detial:
                startActivity(new Intent(this, MyGoldRecordDetial.class).putExtra("id", boomId));
                break;
        }
    }

    private Bean_BoomDetial bean_BoomDetial;

    private void getBoomInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("mineRecordId", boomId);
        PostTools.getData(this, CommonUntilities.USER_URL + "GetGoldMineInfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response))
                    return;
                bean_BoomDetial = new Gson().fromJson(response, Bean_BoomDetial.class);
                if (bean_BoomDetial != null && bean_BoomDetial.Success) {
                    try {
                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void setData() {
        if (!TextUtils.isEmpty(bean_BoomDetial.Data.PicUrl)) {
            Glide.with(this).load(bean_BoomDetial.Data.PicUrl).into(imgPhoto);
        }
        txtCount.setText("已领取 " + (bean_BoomDetial.Data.Count - bean_BoomDetial.Data.LeftCount) + "/" + bean_BoomDetial.Data.Count);
        txtInfo.setText("");
        txtContent.setText(bean_BoomDetial.Data.PicTitle);
        if (isMine) {
            if (bean_BoomDetial.Data.Status > 0) {
                if (bean_BoomDetial.Data.BombType == 0) {
                    txtInfo.append(getSpanString(TextUtils.isEmpty(bean_BoomDetial.Data.BombUserNickName) ? bean_BoomDetial.Data.BombUserId : bean_BoomDetial.Data.BombUserNickName, Color.rgb(240, 165, 9)));
                    txtInfo.append(" 在 ");
                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
                    txtInfo.append(" 领取了我的寻宝雷");
//                    if (bean_BoomDetial.Data.IsHaveBombSuit) {
//                        txtInfo.append("但是被他的防爆衣成功防御,不加积分。");
//                    } else {
//                        txtInfo.append("减少对方 ");
//                        txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.MinusScore, Color.rgb(240, 165, 9)));
//                        txtInfo.append(" 积分,");
//                        txtInfo.append("为我增加 ");
//                        txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.PlusScore, Color.rgb(240, 165, 9)));
//                        txtInfo.append(" 积分。");
//                    }
                }
//                else {
//                    txtInfo.append(getSpanString(TextUtils.isEmpty(bean_BoomDetial.Data.BombUserNickName) ? bean_BoomDetial.Data.BombUserId : bean_BoomDetial.Data.BombUserNickName, Color.rgb(240, 165, 9)));
//                    txtInfo.append(" 使用扫雷器在 ");
//                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
//                    txtInfo.append(" 扫到到了我埋的雷,并成功排除");
//                }

            } else {
                txtInfo.append("我在 ");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
                txtInfo.append(" 埋下了一颗 ");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.MineTypeTxt, Color.rgb(240, 165, 9)));
            }
        } else {
            if (bean_BoomDetial.Data.BombType == 0) {
                txtInfo.append(" 我在 ");
                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
                txtInfo.append(" 领取了");
                txtInfo.append(getSpanString(TextUtils.isEmpty(bean_BoomDetial.Data.UserNickName) ? bean_BoomDetial.Data.UserId : bean_BoomDetial.Data.UserNickName, Color.rgb(240, 165, 9)));
                txtInfo.append(" 的" + bean_BoomDetial.Data.MineTypeTxt);
//                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.MineTypeTxt, Color.rgb(240, 165, 9)));
//                if (bean_BoomDetial.Data.IsHaveBombSuit) {
//                    txtInfo.append("但是被我的防弹衣成功防御。");
//                } else {
//                    txtInfo.append("减少我 ");
//                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.MinusScore, Color.rgb(240, 165, 9)));
//                    txtInfo.append(" 积分,");
//                    txtInfo.append("为对方增加 ");
//                    txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.PlusScore, Color.rgb(240, 165, 9)));
//                    txtInfo.append(" 积分。");
//                }
            }
//            else {
//                txtInfo.append(" 我使用扫雷器在 ");
//                txtInfo.append(Tools.getSpanString(this, bean_BoomDetial.Data.Address, Color.rgb(240, 165, 9)));
//                txtInfo.append(" 扫到到了");
//                txtInfo.append(getSpanString(TextUtils.isEmpty(bean_BoomDetial.Data.UserNickName) ? bean_BoomDetial.Data.UserId : bean_BoomDetial.Data.UserNickName, Color.rgb(240, 165, 9)));
//                txtInfo.append(" 埋的" + bean_BoomDetial.Data.MineTypeTxt + ",并成功排除");
//            }

        }
        txtInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private SpannableString getSpanString(String content, int color) {
        if (TextUtils.isEmpty(content)) {
            content = "玩儿家";
        }
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                createPop();
            }
        }, 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void createPop() {
        if (addFriendPop == null)
            addFriendPop = new AddFriendPop(this);
        String id = "";
        if (isMine)
            id = bean_BoomDetial.Data.BombUserId;
//            addFriendPop.setId();
        else id = bean_BoomDetial.Data.UserId;
//            addFriendPop.setId();
        if (TextUtils.equals(id, AppPrefrence.getUserName(context))) {
            return;
        }
        addFriendPop.setId(id);
        addFriendPop.showPop(txtInfo);
        addFriendPop.setPopInterfacer(this, 0);
    }

    @Override
    public void OnDismiss(int flag) {

    }

    @Override
    public void OnConfirm(int flag, Bundle bundle) {

    }

    @Override
    public void OnCancle(int flag) {

    }
}
