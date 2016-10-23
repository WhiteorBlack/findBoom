package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/10/19.
 */

/**
 * author:${白曌勇} on 2016/10/19
 * TODO:
 */
public class Bean_WXpay extends BaseBean {
    public WxPay Data;
    public class WxPay{
        public String appid;
        public String partnerid;
        public String prepayid;
        public String packageValue;
        public String noncestr;
        public String timestamp;
        public String sign;
    }
}
