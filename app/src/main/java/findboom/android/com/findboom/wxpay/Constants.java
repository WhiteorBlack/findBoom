package findboom.android.com.findboom.wxpay;

import com.tencent.mm.sdk.openapi.IWXAPI;

public class Constants {
    //public static final String DESCRIPTOR = "com.umeng.share";
    // appid
    // 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data
    // android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static final String APP_ID = "wxd54740ca347dcebb";

    // 商户号
    public static final String MCH_ID = "";

    // API密钥，在商户平台设置
    public static final String API_KEY = "";

    public static final String APP_SCECET = "3e63d9844fd26c0d6a56c74af3673187";

    public static IWXAPI iwxapi;
}
