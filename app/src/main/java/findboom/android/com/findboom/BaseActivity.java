package findboom.android.com.findboom;/**
 * Created by Administrator on 2016/8/8.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jph.takephoto.app.TakePhotoActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * author:${白曌勇} on 2016/8/8
 * TODO:
 */
public class BaseActivity extends TakePhotoActivity {

    public Context context;
    public Activity activity;
    public void boomClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        activity=this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
