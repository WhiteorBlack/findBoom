package findboom.android.com.findboom.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AliPayHelper {

    // 商户PID
    public static final String PARTNER = "2088521054312960";
    // 商户收款账号
    public static final String SELLER = "saoleikeji@126.com";

    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALKAz55egELDAYFWANNLJ0Hls2kOY7oddpbAmLrEdb8/YDoHIdw9igZBPHgNh4A/L7nEJHZ5bu1MX9bKme9QwYIBHSUKaISOvOwiwyzvWWZPPoh3VmL/KxNIrzwRnb71AGbSVXbpxewodfsR2M5QOBjHJsjHWufwfiKwWnRf9gIXAgMBAAECgYBq5abwEWnAEWYCXU2rO3NsDDIP9PUWDPTqvLX6CxL0j/CBb6oAab4Rltmuj3OZAr4DAopPhNNAOp+eUsAyx+rJfbAeuGoePpNfdVtv2mPTmrHMWvu7qOZVFTiHKE8IcvLMqFbhamiMvdG1W5yABz+vTmezZiLOuj0BbDNKGowQEQJBAOwvOHnbfVnuJSsTMvkB0BTuHSKeLM3t3wZdNs8WABt9pgdPCwEb0a0QzipFKrfSgNPsTNdZB9WICEFGTyk5uwkCQQDBerWX9jreaMlgTqD/wbFv/nx0ZnNpQBZa3+2M9D2dG6tiZWAZyAQX6BszHI+yJpRrsqX1WsuE4v5icigIYHwfAkEA6xE4aXf986KlFZEfO1wFiL9Q+iJpv2+PeJsPsN5ZrwLIWx/dePYCIvjF6KY8lQYKuGHIPNDcjja82JKsuQA/WQJBALS/FfYoqUYtTjMSQYWsRT8C1vFG5HpmzHuIxjrv0L98odcCLYzoNiq/Qum0vOkI8Y7LDg7wC0DRbiQ50PxCZrECQQCTW9eWvzhmlIld8JWOn5UEEGYkGMYHRK9ZutK8mNnieIS71b6HmDwaKQvTPw2DwGKHqivuoM6w1U/9sxWR79Ll";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCv6XL5sInsJ0HAmEbQsNlAbGA9O4JOGjzuohNGRkQ8/c4uZrERuYS+V3h+TjukFs5kJBbVMilZt3fkEOck+V//wuctX2brrgGHMzMu63KNoOEkxoDurnY8iBMngvFRHM06Lzp1I0mZQ0axL8/btGH6c3sCkf5itDo3F8tpsyUDCQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    private Activity c;
    private Handler handler;

    public AliPayHelper(Activity c, Handler handler) {
        this.c = c;
        this.handler = handler;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        handler.sendEmptyMessage(0);
                        Toast.makeText(c, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(c, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.equals(resultStatus, "6001")) {//订单取消
                            Toast.makeText(c, "支付取消", Toast.LENGTH_SHORT).show();
                            handler.sendEmptyMessage(2);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(c, "支付失败", Toast.LENGTH_SHORT).show();
                            handler.sendEmptyMessage(1);
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    // Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
                    // Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(String name, String info, String price) {
        // 订单
        String orderInfo = getOrderInfo(name, info, price);
        // Tools.debug(orderInfo);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        // Tools.debug(sign);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
            // Tools.debug(sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(c);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(String orderInfo) {

        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(c);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(c);
                // 调用查询接口，获取查询结果
                boolean isExist = true;

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(c);
        String version = payTask.getVersion();
        Toast.makeText(c, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
