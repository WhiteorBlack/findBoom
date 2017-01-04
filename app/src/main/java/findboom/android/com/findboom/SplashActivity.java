package findboom.android.com.findboom;/**
 * Created by Administrator on 2016/8/8.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tencent.ysdk.api.YSDKApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.jpush.android.api.JPushInterface;
import findboom.android.com.findboom.activity.GuideActivity;
import findboom.android.com.findboom.activity.LoginActivity;
import findboom.android.com.findboom.adapter.ViewPagerAdapter;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import okhttp3.Call;

/**
 * author:${白曌勇} on 2016/8/8
 * TODO:
 */
public class SplashActivity extends BaseActivity {

    private ViewPager viewPager;
    private LinearLayout llParent;
    private List<View> views;
    private Button btnGetIn;
    private ViewPagerAdapter pagerAdapter;

    private int[] splashRes = new int[]{R.mipmap.splash_rose, R.mipmap.splash_girl, R.mipmap.splash_day, R.mipmap.splash_night};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        YSDKApi.onCreate(this);
        initView();
        setData();
        if (AppPrefrence.getIsFirst(this)) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
        } else {
            ImageView imgSplash = (ImageView) findViewById(R.id.img_splash);
            Glide.with(this).load(splashRes[new Random().nextInt(3)]).into(imgSplash);
            if (AppPrefrence.getIsLogin(this)) {
                getUserData();
            }
            JPushInterface.init(getApplicationContext());
//            countDown();
        }

    }

    private void setData() {
        for (int i = 0; i < splashRes.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.guide_imageview, null);
            ImageView imageview = (ImageView) view.findViewById(R.id.img_guide);
            Glide.with(this).load(splashRes[i]).into(imageview);
            views.add(view);
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Tools.dip2px(this, 10), Tools.dip2px(this, 10));
            params.leftMargin = 15;
            params.rightMargin = 15;
            point.setLayoutParams(params);
            if (i == 0)
                point.setBackgroundResource(R.drawable.guide_point_selected);
            else point.setBackgroundResource(R.drawable.guide_point_unselect);
            llParent.addView(point);
        }
        pagerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        views = new ArrayList<>();
        btnGetIn = (Button) findViewById(R.id.btn_get_in);
        btnGetIn.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        llParent = (LinearLayout) findViewById(R.id.ll_point_content);
        pagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == splashRes.length-1)
                    btnGetIn.setVisibility(View.VISIBLE);
                else btnGetIn.setVisibility(View.GONE);
                for (int i = 0; i < splashRes.length; i++) {
                    if (position==i){
                        llParent.getChildAt(i).setBackgroundResource(R.drawable.guide_point_selected);
                    }else llParent.getChildAt(i).setBackgroundResource(R.drawable.guide_point_unselect);
                }
            }
        });
    }

    Bean_UserInfo bean_UserInfo;

    private void getUserData() {
        Map<String, String> params = new HashMap<>();
        params.put("Token", AppPrefrence.getToken(context));
        PostTools.getData(context, CommonUntilities.USER_URL + "GetUserInfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("splash" + response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                bean_UserInfo = new Gson().fromJson(response, Bean_UserInfo.class);
                if (bean_UserInfo != null && bean_UserInfo.Success) {
                    BoomDBManager.getInstance().updateUserData(bean_UserInfo.Data);
                    AppPrefrence.setToken(context, bean_UserInfo.Data.Token);
                    AppPrefrence.setUserName(context, bean_UserInfo.Data.GameUserId);
                    AppPrefrence.setUserPhone(context, bean_UserInfo.Data.PhoneNumber);
                    AppPrefrence.setEaseId(context, bean_UserInfo.Data.EasemobId);
                    EMClient.getInstance().login(bean_UserInfo.Data.EasemobId, bean_UserInfo.Data.EasemobPwd, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Tools.debug("ease login succes");
                        }

                        @Override
                        public void onError(int i, String s) {
                            Tools.debug("error" + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                    FindBoomApplication.getInstance().setCurrentUserName(bean_UserInfo.Data.EasemobId);
                    AppPrefrence.setIsPayPwd(context, !TextUtils.isEmpty(bean_UserInfo.Data.PayPassWord));
                } else AppPrefrence.setIsLogin(context, false);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }

    private void countDown() {
        Tools.debug("countDown");
        new CountDownTimer(3 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!AppPrefrence.getIsLogin(SplashActivity.this))
                    startActivity(new Intent(context, LoginActivity.class));
                else
                    startActivity(new Intent(context, Home.class));
                finish();
            }
        }.start();
    }

    @Override
    public void boomClick(View v) {
        super.boomClick(v);
        switch (v.getId()){
            case R.id.btn_get_in:
                if (!AppPrefrence.getIsLogin(SplashActivity.this))
                    startActivity(new Intent(context, LoginActivity.class));
                else
                    startActivity(new Intent(context, Home.class));
                finish();
                break;
            case R.id.btn_skipe:
                if (!AppPrefrence.getIsLogin(SplashActivity.this))
                    startActivity(new Intent(context, LoginActivity.class));
                else
                    startActivity(new Intent(context, Home.class));
                finish();
                break;
        }
    }
}
