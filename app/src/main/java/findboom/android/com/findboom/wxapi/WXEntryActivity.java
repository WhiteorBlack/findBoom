package findboom.android.com.findboom.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.BaseActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.bean.Bean_WxLogin;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.wxpay.Constants;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_share_entry);
        try{
            Constants.iwxapi.handleIntent(getIntent(), this);
        }catch (Exception e){
            finish();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Constants.iwxapi.handleIntent(intent, WXEntryActivity.this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Tools.debug("hahhha");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Tools.debug(baseResp.errStr + baseResp.errCode + "--" + baseResp.errStr);
        SendAuth.Resp resp = (SendAuth.Resp) baseResp;
        switch (baseResp.errCode) {
            case 0://同意
                getToken(resp.code);
                break;
        }
    }

    private String openId = "", accessToken = "";

    private void getToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", Constants.APP_ID);
        params.put("secret", Constants.APP_SCECET);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        PostTools.getDataWithNone(this, "https://api.weixin.qq.com/sns/oauth2/access_token", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "网络错误,请重试");
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    openId = object.getString("openid");
                    accessToken = object.getString("access_token");
                    login();
                } catch (Exception e) {
                    Tools.toastMsg(context, "授权失败请重试");
                }

            }
        });
    }

    Bean_UserInfo userInfo;
    private void login() {
        Map<String, String> params = new HashMap<>();
        params.put("OpenId", openId);
        Tools.debug("openid" + openId);
        PostTools.postData(context, CommonUntilities.ACCOUNT_URL + "WechatLogin", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "网络错误,请重试");
                    return;
                }
                userInfo = new Gson().fromJson(response, Bean_UserInfo.class);

                if (userInfo.Success) {
                    AppPrefrence.setToken(context, userInfo.Data.Token);
                    AppPrefrence.setUserName(context, userInfo.Data.GameUserId);
                    AppPrefrence.setUserPhone(context, userInfo.Data.PhoneNumber);
                    EMClient.getInstance().login(userInfo.Data.EasemobId, userInfo.Data.EasemobPwd, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Tools.debug("ease login succes");
                        }

                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                    FindBoomApplication.getInstance().setCurrentUserName(userInfo.Data.EasemobId);
                    AppPrefrence.setIsPayPwd(context, !TextUtils.isEmpty(userInfo.Data.PayPassWord));
//                    BoomDBManager.getInstance().setUserData(userInfo.Data);
                    AppPrefrence.setIsLogin(context, true);
                    AppPrefrence.setUserName(context, userInfo.Data.GameUserId);
                    if (TextUtils.isEmpty(userInfo.Data.NickName) && TextUtils.isEmpty(userInfo.Data.Avatar)) {
                        //首次登陆
                        getUserInfoFromWx();
                    } else {
                        BoomDBManager.getInstance().updateUserData(userInfo.Data);
                        AppPrefrence.setIsLogin(context, true);
                        finish();
                    }
                }
            }
        });
    }

    Bean_WxLogin wxLogin;

    private void getUserInfoFromWx() {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openId);
        PostTools.getDataWithNone(context, "https://api.weixin.qq.com/sns/userinfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "网络错误,请重试");
                    return;
                }
                wxLogin = new Gson().fromJson(response, Bean_WxLogin.class);
                Bean_UserInfo.GameUser user = new Bean_UserInfo.GameUser();
                user=userInfo.Data;
                user.Avatar = wxLogin.headimgurl;
                user.NickName = wxLogin.nickname;
                user.City = wxLogin.city;
                user.OpenId = wxLogin.openid;
                user.GameUserId = userInfo.Data.GameUserId;
                BoomDBManager.getInstance().setUserData(user);
                saveUserInfo();

            }
        });
    }

    private void saveUserInfo() {
        Map<String, String> params = new HashMap<>();
        int ageInt = 0;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.YEAR;
        params.put("NickName", TextUtils.isEmpty(wxLogin.nickname) ? "" : wxLogin.nickname);
        params.put("City", wxLogin.city);
        params.put("Province", wxLogin.province);
        params.put("Area", "");
        params.put("Profession", "");
        params.put("UserProfile", "");
        params.put("PhoneBrand", "");
        params.put("Avatar", wxLogin.headimgurl);
        params.put("BirthDay", year - ageInt + "-01-01");
        PostTools.postData(context, CommonUntilities.USER_URL + "UpdateUserInfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                finish();
            }
        });
    }
}