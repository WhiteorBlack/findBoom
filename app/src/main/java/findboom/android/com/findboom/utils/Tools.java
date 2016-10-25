package findboom.android.com.findboom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import findboom.android.com.findboom.R;

/**
 * 常用方法公共类
 *
 * @author baizy
 * @date 2015-7-21 上午10:23:40
 * @package com.stly.common
 * @todo TODO
 */
public class Tools {
    public static boolean DEBUG_FLAG = true;
    public static String DEBUG_TAG = "--YEXIU--";
    private static SimpleDateFormat sf = null;

    public static void debug(String str) {
        if (DEBUG_FLAG) {
            str = str.replaceAll("\\r|\\n", "");
            Log.e(DEBUG_TAG, str + "");
        }
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    public static String readAssertResource(Context context, String strAssertFileName) {
        AssetManager assetManager = context.getAssets();
        String strResponse = "";
        try {
            InputStream ims = assetManager.open(strAssertFileName);
            strResponse = getStringFromInputStream(ims);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    private static String getStringFromInputStream(InputStream a_is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(a_is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }


    /**
     * 判断是否安装有该APP
     *
     * @param packageName
     * @return
     */
    public static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static SpannableString getSpanString(Context context, String content, int color) {
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;

    }

    /**
     * 拉起高德地图
     *
     * @param lon
     * @param lat
     * @param title
     * @param describle
     * @param context
     */
    public static void openGaode(double lon, double lat, String title, String describle, Context context) {
        // TODO Auto-generated method stub
        try {
            double[] gd_lat_lon = bdToGaoDe(lon, lat);
            StringBuilder loc = new StringBuilder();
            loc.append("androidamap://viewMap?sourceApplication=XX");
            loc.append("&poiname=");
            loc.append(describle);
            loc.append("&lat=");
            loc.append(gd_lat_lon[0]);
            loc.append("&lon=");
            loc.append(gd_lat_lon[1]);
            loc.append("&dev=0");
            Intent intent = Intent.getIntent(loc.toString());
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 拉起百度地图
     */
    public static void openBaidu(double lon, double lat, String title, String describle, Context context) {
        // TODO Auto-generated method stub
        try {
            StringBuilder loc = new StringBuilder();
            loc.append("intent://map/direction?origin=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append("我的位置");
            loc.append("&destination=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append(describle);
            loc.append("&mode=driving");
            loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = Intent.getIntent(loc.toString());
            if (isInstallPackage("com.baidu.BaiduMap")) {
                context.startActivity(intent); // 启动调用
                Log.e("GasStation", "百度地图客户端已经安装");
            } else {
                Log.e("GasStation", "没有安装百度地图客户端");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    private double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    // 添加大小月月份并将其转换为list,方便之后的判断
    static String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    static String[] months_little = {"4", "6", "9", "11"};

    static List<String> list_big = Arrays.asList(months_big);
    static List<String> list_little = Arrays.asList(months_little);

    /**
     * 没有action的snakbar
     *
     * @param contant
     * @param msg
     */

    public static void notifySnak(View contant, String msg) {

        Snackbar.make(contant, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 带有action的snakbar
     *
     * @param contant
     * @param msg
     */
    public static void notifySnak(View contant, String msg, String actionString, OnClickListener OnClickListener) {
        Snackbar.make(contant, msg, Snackbar.LENGTH_SHORT).setAction(actionString, OnClickListener).show();
    }


    // 获得今天日期
    public static String getTodayData() {
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "";
        String month = calendar.get(Calendar.MONTH) + 1 + "";
//        if (calendar.get(Calendar.MONTH) + 1 < 10) {
//            month = "0" + (calendar.get(Calendar.MONTH) + 1);
//        } else {
//            month = "" + calendar.get(Calendar.MONTH) + 1;
//        }
//
//        String day = calendar.get(Calendar.DATE) + "";

        String data = year + "年" + month + "月";
        return data;
    }

    // 获得明天日期
    public static String getTomoData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 31;
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }

            }
        } else if (day == 31) {
            day = 1;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }

        } else {
            day++;
        }
        String monthString;
        if (month < 10) {
            monthString = "0" + month;
        } else {
            monthString = month + "";
        }
        String dayString;
        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = day + "";
        }
        String data = year + "-" + monthString + "-" + dayString;
        return data;
    }

    // 获得后天日期
    public static String getTheDayData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 2;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
        } else if (day == 31) {
            day = 2;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }

        } else {
            day = day + 2;
        }
        String monthString;
        if (month < 10) {
            monthString = "0" + month;
        } else {
            monthString = month + "";
        }
        String dayString;
        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = day + "";
        }
        String data = year + "-" + monthString + "-" + dayString;
        return data;
    }

    /**
     * 将时间字符串转换成时间戳
     *
     * @param time
     * @return
     * @author baizy
     * @todo TODO
     */
    public static String dateStringToLong(String time) {
        String timeString = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm");
        Date date;
        try {
            date = sdf.parse(time);
            long timeLong = date.getTime();
            timeString = String.valueOf(timeLong / 1000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return timeString;
    }

    public static String timeStringToLong(String time) {
        String timeString = "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date;
        try {
            date = sdf.parse(time);
            long timeLong = date.getTime();
            timeString = String.valueOf(timeLong / 1000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return timeString;
    }

    /**
     * Map a value within a given range to another range.
     *
     * @param value    the value to map
     * @param fromLow  the low end of the range the value is within
     * @param fromHigh the high end of the range the value is within
     * @param toLow    the low end of the range to map to
     * @param toHigh   the high end of the range to map to
     * @return the mapped value
     */
    public static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh, double toLow,
                                                  double toHigh) {
        double fromRangeSize = fromHigh - fromLow;
        double toRangeSize = toHigh - toLow;
        double valueScale = (value - fromLow) / fromRangeSize;
        return toLow + (valueScale * toRangeSize);
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     * @author baizy
     * @todo TODO
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void closeInput(View v, Context c) {
        InputMethodManager imm = (InputMethodManager) c.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {

            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void openInput(View v, Context c) {
        InputMethodManager imm = (InputMethodManager) c.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @SuppressWarnings("deprecation")
    public static float getScreenWide(Context c) {
        WindowManager wmManager = (WindowManager) c.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        return wmManager.getDefaultDisplay().getWidth();
    }

    public static float getScreenHeight(Context c) {
        WindowManager wmManager = (WindowManager) c.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        return wmManager.getDefaultDisplay().getHeight();
    }

    /**
     * 判断是否为正确的手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        // System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * Bitmap to Drawable
     *
     * @param bitmap
     * @param mcontext
     * @return
     */
    public static Drawable bitmapToDrawble(Bitmap bitmap, Context mcontext) {
        Drawable drawable = new BitmapDrawable(mcontext.getApplicationContext().getResources(), bitmap);
        return drawable;
    }

    /* 将长时间格式时间转换为字符串 HH:mm:ss */
    public static String dateToStrLong(long dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(dateDate);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 将秒转换成HH:mm:ss
     *
     * @param second
     * @return
     * @author baizy
     * @todo TODO
     */
    public static String secondToDate(long second) {
        String date = "00:00:00";
        String dayString = "", hourString = "", minuteString = "", secondString = "";
        int day, hour, minute, sec;
        long hourTime, minuteTime;

        if (second > 0) {

            day = (int) (second / 86400);
            if (day > 0) {
                dayString = day + "天";
                hourTime = (int) (second % 86400);

            } else {
                hourTime = second;
            }
            hour = (int) (hourTime / (60 * 60));
            if (hour > 9) {
                hourString = hour + ":";
            } else {
                hourString = "0" + hour + ":";
            }
            if (hour > 0) {
                minuteTime = (hourTime % (60 * 60));
            } else {
                minuteTime = hourTime;
            }
            minute = (int) (minuteTime / 60);
            if (minute > 9) {
                minuteString = minute + ":";
            } else {
                minuteString = "0" + minute + ":";
            }
            if (minute > 0) {
                sec = (int) minuteTime % 60;
            } else {
                sec = (int) minuteTime;
            }
            if (sec > 9) {
                secondString = sec + "";
            } else {
                secondString = "0" + sec;
            }
        }
        date = dayString + hourString + minuteString + secondString;
        return date;
    }

    /* 将字符串转为时间戳 */
    public static long getStringToDate(long timeInt) {
        String time = String.valueOf(timeInt);
        sf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /* 将字符串转为时间戳 yy-MM-dd */
    public static long getDateToLong(String time) {
        // String time = String.valueOf(timeInt);
        sf = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /* 将字符串转为时间戳 */
    public static long getDateTimeToLong(String time) {
        // String time = String.valueOf(timeInt);
        sf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static boolean hasSdCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public final static String get32MD5Str(Context context, long time) {
        MessageDigest messageDigest = null;
        String result = CommonUntilities.MD5KEY + time;
        if (AppPrefrence.getIsLogin(context))
            result = AppPrefrence.getToken(context) + result;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(result.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        result = md5StrBuff.toString();

        return result.toLowerCase();
    }


    public final static String get32MD5Str(String time) {
        MessageDigest messageDigest = null;
        String result = CommonUntilities.MD5KEY + time;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(result.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        result = md5StrBuff.toString();

        return result.toLowerCase();
    }

    public final static String get32MD5StrWithOutKey(String time) {
        MessageDigest messageDigest = null;
        String result = time;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(result.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        result = md5StrBuff.toString();

        return result.toLowerCase();
    }

    public static String convertIconToString(String path) {
        if (TextUtils.isEmpty(path))
            return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        Bitmap bitmap = compImageByPath(path);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    public static Bitmap compImageByPath(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;// 这里设置高度�?800f
        float ww = 480f;// 这里设置宽度�?480f
        int be = 1;// be=1表示不缩�?
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩�?
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩�?
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        return compressImage(bitmap);// 压缩好比例大小后再进行质量压�?
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，100表示不压缩，把压缩后的数据存放到baos
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos
            options -= 10;// 每次都减�?10
        }

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 4;
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, bitmapOptions);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 根据手机的分辨率将dp的单位转成 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率将 px(像素) 的单位 转成 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static void toastMsg(Activity context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    // 隐藏软键盘
    public static void hideSoftKeyboard(Activity act) {
        InputMethodManager inputManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
    }

}
