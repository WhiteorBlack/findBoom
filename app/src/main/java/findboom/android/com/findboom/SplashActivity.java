package findboom.android.com.findboom;/**
 * Created by Administrator on 2016/8/8.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.activity.LoginActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ImageView imgSplash=(ImageView)findViewById(R.id.img_splash);
        Calendar calendar=Calendar.getInstance();
        Tools.debug(calendar.get(Calendar.HOUR_OF_DAY)+"");
        if (calendar.get(Calendar.HOUR_OF_DAY)<17)
            Glide.with(this).load(R.mipmap.splash_day).into(imgSplash);
        else Glide.with(this).load(R.mipmap.splash_night).into(imgSplash);
        if (AppPrefrence.getIsLogin(this)) {
            getUserData();
        }
        countDown();
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
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }

    private void countDown() {
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
}
