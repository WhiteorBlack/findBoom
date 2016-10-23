package findboom.android.com.findboom.application;/**
 * Created by Administrator on 2016/9/1.
 */

import android.app.Application;
import android.os.CountDownTimer;

import com.baidu.mapapi.SDKInitializer;

import cn.jpush.android.api.JPushInterface;

/**
 * author:${白曌勇} on 2016/9/1
 * TODO:
 */
public class FindBoomApplication extends Application {
    private static FindBoomApplication instance;

    public static FindBoomApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SDKInitializer.initialize(instance);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
