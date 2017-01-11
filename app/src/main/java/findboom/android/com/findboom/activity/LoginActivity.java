package findboom.android.com.findboom.activity;/**
 * Created by Administrator on 2016/9/1.
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.BaseActivity;
import findboom.android.com.findboom.Home;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.dailog.FindPwdPop;
import findboom.android.com.findboom.dailog.InputCodePop;
import findboom.android.com.findboom.dailog.InputPhonePop;
import findboom.android.com.findboom.dailog.LoginPop;
import findboom.android.com.findboom.dailog.RegisterPop;
import findboom.android.com.findboom.dailog.SendCodeConfirmPop;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.wxpay.Constants;
import okhttp3.Call;

/**
 * author:${白曌勇} on 2016/9/1
 * TODO:
 */
public class LoginActivity extends BaseActivity implements PopInterfacer {
    private InputPhonePop inputPhonePop;
    private SendCodeConfirmPop sendCodeConfirmPop;
    private InputCodePop inputCodePop;
    private FindPwdPop findPwdPop;
    private LoginPop loginPop;
    private RegisterPop registerPop;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
        registerWx();
    }

    private void registerWx() {
        Constants.iwxapi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, true);
        Tools.debug("wxregister" + Constants.iwxapi.registerApp(Constants.APP_ID));
        Constants.iwxapi.registerApp(Constants.APP_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppPrefrence.getIsLogin(context)) {
            startActivity(new Intent(context, Home.class));
            finish();
        }
    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.btn_phone);
    }

    @Override
    public void boomClick(View v) {
        super.boomClick(v);
        switch (v.getId()) {
            case R.id.btn_chat:
                //微信登录
                if (!Constants.iwxapi.isWXAppInstalled()) {
                    Tools.toastMsg(context, "请安装微信");
                    return;
                }
                loginByChat();
                break;
            case R.id.btn_phone:
                //手机登录 直接使用验证码登录
//                if (inputPhonePop == null)
//                    inputPhonePop = new InputPhonePop(this);
//                inputPhonePop.setPopInterfacer(this, 1);
//                inputPhonePop.showPop(btnLogin);
                //现在改为使用密码登陆
                if (loginPop == null)
                    loginPop = new LoginPop(context);
                loginPop.showPop(btnLogin);
                loginPop.setPopInterfacer(this, 3);
                break;
        }
    }

    private void loginByChat() {
        // send oauth request
        try {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_boom";
            Constants.iwxapi.sendReq(req);
        } catch (Exception e) {
            Tools.debug(e.toString());
        }

    }

    @Override
    public void OnDismiss(int flag) {
        switch (flag) {
            case 1:
                inputPhonePop = null;
                break;
            case 2:
                sendCodeConfirmPop = null;
                break;
            case 3:
                loginPop = null;
                break;
            case 4:
                registerPop = null;
                break;
        }
    }

    String phone;

    @Override
    public void OnConfirm(int flag, Bundle bundle) {
        switch (flag) {
            case 1:
                if (sendCodeConfirmPop == null)
                    sendCodeConfirmPop = new SendCodeConfirmPop(context);
                sendCodeConfirmPop.setPopInterfacer(LoginActivity.this, 2);
                phone = bundle.getString("phone");
                sendCodeConfirmPop.setPhone(phone);
                sendCodeConfirmPop.showAtLocation(btnLogin, Gravity.CENTER, 0, 0);
                break;
            case 2:

                if (inputCodePop == null)
                    inputCodePop = new InputCodePop(context);
                inputCodePop.setPopInterfacer(LoginActivity.this, 3);
                inputCodePop.showPop(btnLogin);
                sendCodeConfirmPop.dismiss();
                sendCode(phone);
                break;
            case 3:
                if (bundle == null)
                    return;
                if (bundle.getInt("type") == 0) {
                    login(bundle.getString("pwd"), bundle.getString("phone"));
                }
                if (bundle.getInt("type") == 1) {
                    if (registerPop == null)
                        registerPop = new RegisterPop(context);
                    registerPop.showPop(btnLogin);
                    registerPop.setPopInterfacer(this, 4);
                    loginPop.dismiss();
                }

                if (bundle.getInt("type") == 2) {
                    if (findPwdPop == null)
                        findPwdPop = new FindPwdPop(context);
                    findPwdPop.showPop(btnLogin);
                    findPwdPop.setPopInterfacer(this, 5);
                    loginPop.dismiss();
                }
                break;
            case 4:
                if (bundle == null)
                    return;
                if (bundle.getInt("type") == 0) {
                    register(bundle.getString("phone"), bundle.getString("code"), bundle.getString("pwd"));
                }

                if (bundle.getInt("type") == 1)
                    startActivity(new Intent(context, UserNotify.class));
                break;
            case 5:
                if (bundle == null)
                    return;
                if (bundle.getInt("type") == 0)
                    changePwd(bundle.getString("phone"), bundle.getString("code"), bundle.getString("pwd"));
                break;
        }
    }

    private void changePwd(String phone, String code, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumber", phone);
        params.put("UserCode", code);
        params.put("NewPassWord", pwd);
        PostTools.postData(this, CommonUntilities.ACCOUNT_URL + "FindPassword", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("login" + response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "请检查网络后重试");
                    return;
                }

                bean_userInfo = new Gson().fromJson(response, Bean_UserInfo.class);
                if (bean_userInfo != null && bean_userInfo.Success) {
                    AppPrefrence.setIsLogin(context, true);
                    AppPrefrence.setToken(context, bean_userInfo.Data.Token);
                    AppPrefrence.setUserName(context, bean_userInfo.Data.GameUserId);
                    BoomDBManager.getInstance().setUserData(bean_userInfo.Data);
                    AppPrefrence.setIsPayPwd(context, !TextUtils.isEmpty(bean_userInfo.Data.PayPassWord));
                    AppPrefrence.setUserPhone(context, bean_userInfo.Data.PhoneNumber);
                    AppPrefrence.setEaseId(context, bean_userInfo.Data.EasemobId);
                    EMClient.getInstance().login(bean_userInfo.Data.EasemobId, bean_userInfo.Data.EasemobPwd, new EMCallBack() {
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
                    FindBoomApplication.getInstance().setCurrentUserName(bean_userInfo.Data.EasemobId);
                    if (loginPop != null)
                        loginPop.dismiss();
                    startActivity(new Intent(context, Home.class));
                    LoginActivity.this.finish();
                } else
                    Tools.toastMsg(context, bean_userInfo.Msg);

            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }

    private void register(String phone, String code, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumber", phone);
        params.put("UserCode", code);
        params.put("PassWord", pwd);
        PostTools.postData(this, CommonUntilities.ACCOUNT_URL + "Register", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("login" + response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "请检查网络后重试");
                    return;
                }

                bean_userInfo = new Gson().fromJson(response, Bean_UserInfo.class);
                if (bean_userInfo != null && bean_userInfo.Success) {
                    AppPrefrence.setIsLogin(context, true);
                    AppPrefrence.setToken(context, bean_userInfo.Data.Token);
                    AppPrefrence.setUserName(context, bean_userInfo.Data.GameUserId);
                    BoomDBManager.getInstance().setUserData(bean_userInfo.Data);
                    AppPrefrence.setIsPayPwd(context, !TextUtils.isEmpty(bean_userInfo.Data.PayPassWord));
                    AppPrefrence.setUserPhone(context, bean_userInfo.Data.PhoneNumber);
                    AppPrefrence.setEaseId(context, bean_userInfo.Data.EasemobId);
                    EMClient.getInstance().login(bean_userInfo.Data.EasemobId, bean_userInfo.Data.EasemobPwd, new EMCallBack() {
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
                    FindBoomApplication.getInstance().setCurrentUserName(bean_userInfo.Data.EasemobId);
                    if (loginPop != null)
                        loginPop.dismiss();
                    startActivity(new Intent(context, Home.class));
                    LoginActivity.this.finish();
                } else
                    Tools.toastMsg(context, bean_userInfo.Msg);

            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }

    Bean_UserInfo bean_userInfo;

    private void login(String code, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumber", phone);
        params.put("password", code);

        PostTools.postData(this, CommonUntilities.ACCOUNT_URL + "Login", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("login" + response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "请检查网络后重试");
                    return;
                }

                bean_userInfo = new Gson().fromJson(response, Bean_UserInfo.class);
                if (bean_userInfo != null && bean_userInfo.Success) {
                    AppPrefrence.setIsLogin(context, true);
                    AppPrefrence.setToken(context, bean_userInfo.Data.Token);
                    AppPrefrence.setUserName(context, bean_userInfo.Data.GameUserId);
                    BoomDBManager.getInstance().setUserData(bean_userInfo.Data);
                    AppPrefrence.setIsPayPwd(context, !TextUtils.isEmpty(bean_userInfo.Data.PayPassWord));
                    AppPrefrence.setUserPhone(context, bean_userInfo.Data.PhoneNumber);
                    AppPrefrence.setEaseId(context, bean_userInfo.Data.EasemobId);
                    EMClient.getInstance().login(bean_userInfo.Data.EasemobId, bean_userInfo.Data.EasemobPwd, new EMCallBack() {
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
                    FindBoomApplication.getInstance().setCurrentUserName(bean_userInfo.Data.EasemobId);
                    if (loginPop != null)
                        loginPop.dismiss();
                    startActivity(new Intent(context, Home.class));
                    LoginActivity.this.finish();
                } else
                    Tools.toastMsg(context, bean_userInfo.Msg);

            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }

    @Override
    public void OnCancle(int flag) {
        if (flag == 3)
            sendCode(phone);
    }

    private void sendCode(String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumber", phone);
        params.put("SmsType", "0");
        PostTools.postData(this, CommonUntilities.LOGIN_URL, params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
            }
        });
    }
}
