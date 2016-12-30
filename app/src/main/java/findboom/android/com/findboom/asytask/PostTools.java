package findboom.android.com.findboom.asytask;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.Tools;
import okhttp.OkHttpUtils;

public class PostTools {

    public static void postData(Context context, String url, Map<String, String> params, PostCallBack postCallBack) {
        Map<String, String> paramsList = new HashMap<>();
        if (params != null)
            paramsList.putAll(params);
        long time = Tools.getCurrentTime();
        paramsList.put("Sign", Tools.get32MD5Str(context, time));
        paramsList.put("Timestamp", time + "");
        if (AppPrefrence.getIsLogin(context))
            paramsList.put("Token", AppPrefrence.getToken(context));
        OkHttpUtils.getInstance().setConnectTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        OkHttpUtils.post().url(url).params(paramsList).tag(context).build().execute(postCallBack);


    }

    public static void postFile(String url, String uri, Map<String, String> params, PostCallBack postCallBack) {
        File file = new File(uri);
        if (file.exists()) {
            OkHttpUtils.postFile().url(url).file(file).params(params).build().execute(postCallBack);
        }
    }

    public static void getData(Context context, String url, Map<String, String> params, PostCallBack postCallBack) {
        Map<String, String> paramsList = new HashMap<>();
        if (params != null)
            paramsList.putAll(params);
        long time = Tools.getCurrentTime();
        paramsList.put("Sign", Tools.get32MD5Str(context, time));
        paramsList.put("Timestamp", time + "");
        if (AppPrefrence.getIsLogin(context))
            paramsList.put("Token", AppPrefrence.getToken(context));
        OkHttpUtils.getInstance().setConnectTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        OkHttpUtils.get().url(url).params(paramsList).build().execute(postCallBack);
    }

    public static void getDataWithNone(Context context, String url, Map<String, String> params, PostCallBack postCallBack) {
        OkHttpUtils.getInstance().setConnectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        OkHttpUtils.get().url(url).params(params).build().execute(postCallBack);
    }
}
