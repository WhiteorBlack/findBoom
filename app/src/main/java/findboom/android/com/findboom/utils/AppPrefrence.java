package findboom.android.com.findboom.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefrence {

    private static SharedPreferences setting;

    private static SharedPreferences getSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences("carSchool",
                context.MODE_PRIVATE);
        return sp;
    }

    private static final String ISLOGIN = "isLogin";
    private static final String USERNAME = "userName";
    private static final String USERPWD = "userPwd";
    private static final String ISPAYPWD = "isPayPwd";
    private static final String TOKEN = "token";
    private static final String ISREDSHOW = "isRedShow";
    private static final String ISBOOMSHOW = "isBoomShow";
    private static final String USERPHONE = "userPhone";


    public static void setUserPhone(Context context,String phone){
        setting=getSp(context.getApplicationContext());
        setting.edit().putString(USERPHONE,phone).apply();
    }
    public static String getUserPhone(Context context){
        setting=getSp(context.getApplicationContext());
        return setting.getString(USERPHONE,"");
    }

    public static void setIsRedShow(Context context, boolean isShow) {
        setting = getSp(context.getApplicationContext());
        setting.edit().putBoolean(ISREDSHOW, isShow).apply();
    }

    public static boolean getIsRedShow(Context context) {
        setting = getSp(context.getApplicationContext());
        return setting.getBoolean(ISREDSHOW, false);
    }

    public static void setIsBoomShow(Context context, boolean isShow) {
        setting = getSp(context.getApplicationContext());
        setting.edit().putBoolean(ISBOOMSHOW, isShow).apply();
    }

    public static boolean getIsBoomShow(Context context) {
        setting = getSp(context.getApplicationContext());
        return setting.getBoolean(ISBOOMSHOW, false);
    }


    public static void setToken(Context context, String token) {
        setting = getSp(context.getApplicationContext());
        setting.edit().putString(TOKEN, token).apply();
    }

    public static String getToken(Context context) {
        setting = getSp(context.getApplicationContext());
        return setting.getString(TOKEN, "");
    }

    /**
     * 是否创建了支付密码
     *
     * @param context
     * @param isCreate
     */
    public static void setIsPayPwd(Context context, boolean isCreate) {
        setting = getSp(context.getApplicationContext());
        setting.edit().putBoolean(ISPAYPWD, isCreate).apply();
    }

    public static boolean getIsPayPwd(Context context) {
        setting = getSp(context.getApplicationContext());
        return setting.getBoolean(ISPAYPWD, false);
    }


    /**
     * 用户密码
     *
     * @param context
     */
    public static void setUserPwd(Context context, String pwd) {
        setting = getSp(context);
        setting.edit().putString(USERPWD, pwd).commit();
    }

    public static String getUserPwd(Context context) {
        setting = getSp(context);
        return setting.getString(USERPWD, "");
    }

    /**
     * 用户登录名
     *
     * @param context
     * @param name
     */
    public static void setUserName(Context context, String name) {
        setting = getSp(context);
        setting.edit().putString(USERNAME, name).commit();
    }

    public static String getUserName(Context context) {
        setting = getSp(context);
        return setting.getString(USERNAME, "");
    }

    public static void setIsLogin(Context context, boolean isLogin) {
        setting = getSp(context);
        setting.edit().putBoolean(ISLOGIN, isLogin).commit();
    }

    public static boolean getIsLogin(Context context) {
        setting = getSp(context);
        return setting.getBoolean(ISLOGIN, false);
    }
}
