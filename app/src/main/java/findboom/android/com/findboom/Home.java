package findboom.android.com.findboom;/**
 * Created by Administrator on 2016/8/8.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.adapter.EMAChatManager;
import com.hyphenate.exceptions.HyphenateException;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import findboom.android.com.findboom.activity.FriendMessage;
import findboom.android.com.findboom.activity.LoginActivity;
import findboom.android.com.findboom.activity.PutCommenBoom;
import findboom.android.com.findboom.activity.PutGoldBoom;
import findboom.android.com.findboom.activity.PutPicBoom;
import findboom.android.com.findboom.activity.PutRedBoom;
import findboom.android.com.findboom.alipay.AliPayHelper;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.BaseBean;
import findboom.android.com.findboom.bean.Bean_AllConfig;
import findboom.android.com.findboom.bean.Bean_BoomBoom;
import findboom.android.com.findboom.bean.Bean_GoldBoom;
import findboom.android.com.findboom.bean.Bean_MapBoom;
import findboom.android.com.findboom.bean.Bean_RedBoom;
import findboom.android.com.findboom.bean.Bean_UseScan;
import findboom.android.com.findboom.bean.Bean_UserArm;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.bean.Bean_Version;
import findboom.android.com.findboom.bean.Bean_WXpay;
import findboom.android.com.findboom.chat.Constant;
import findboom.android.com.findboom.chat.EaseNotifier;
import findboom.android.com.findboom.chat.activity.ChatActivity;
import findboom.android.com.findboom.chat.db.EaseUser;
import findboom.android.com.findboom.chat.db.InviteMessgeDao;
import findboom.android.com.findboom.chat.db.UserDao;
import findboom.android.com.findboom.dailog.AddFriendPop;
import findboom.android.com.findboom.dailog.AdvicePop;
import findboom.android.com.findboom.dailog.ArsenalPop;
import findboom.android.com.findboom.dailog.BandPhonePop;
import findboom.android.com.findboom.dailog.BoomDefensePop;
import findboom.android.com.findboom.dailog.BoomGold;
import findboom.android.com.findboom.dailog.BoomPic;
import findboom.android.com.findboom.dailog.BoomPop;
import findboom.android.com.findboom.dailog.BoomText;
import findboom.android.com.findboom.dailog.ChangePwdPop;
import findboom.android.com.findboom.dailog.ChatPop;
import findboom.android.com.findboom.dailog.ConvertRedPop;
import findboom.android.com.findboom.dailog.DefensePop;
import findboom.android.com.findboom.dailog.FriendListPop;
import findboom.android.com.findboom.dailog.GetRecordPop;
import findboom.android.com.findboom.dailog.GuidePop;
import findboom.android.com.findboom.dailog.MessageDialog;
import findboom.android.com.findboom.dailog.NewShopPop;
import findboom.android.com.findboom.dailog.NotifyPop;
import findboom.android.com.findboom.dailog.OpenRedPop;
import findboom.android.com.findboom.dailog.PersonalCenterPop;
import findboom.android.com.findboom.dailog.PickerCityDialog;
import findboom.android.com.findboom.dailog.PickerDialog;
import findboom.android.com.findboom.dailog.PostResultPop;
import findboom.android.com.findboom.dailog.PutBoomTypePop;
import findboom.android.com.findboom.dailog.RecordListPop;
import findboom.android.com.findboom.dailog.RecordPop;
import findboom.android.com.findboom.dailog.ReportPop;
import findboom.android.com.findboom.dailog.ScanBoomPop;
import findboom.android.com.findboom.dailog.ScanPop;
import findboom.android.com.findboom.dailog.SelectPayTypeAllPop;
import findboom.android.com.findboom.dailog.SelectPayTypePop;
import findboom.android.com.findboom.dailog.SettingPop;
import findboom.android.com.findboom.dailog.ShopBuyPop;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.interfacer.LocationListener;
import findboom.android.com.findboom.interfacer.MyLocationListener;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.receiver.MyReceiver;
import findboom.android.com.findboom.service.BackgroundService;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.RotateAnimation;
import findboom.android.com.findboom.widget.explosion.ExplosionField;
import findboom.android.com.findboom.wxpay.WxPayHelper;
import okhttp3.Call;

/**
 * author:${白曌勇} on 2016/8/8
 * TODO:
 */
public class Home extends BaseActivity implements PopInterfacer, LocationListener, BaiduMap.OnMapClickListener, BaiduMap.OnMapLongClickListener, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapStatusChangeListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ImageView imgDefense, imgScan, imgRecord, imgArsenal, imgMsg, imgLocation;
    //    private ImageView imgRedPoint;
    private TextView txtDefense, txtScan, txtRecord, txtArsenal, txtMsg, txtMsgCount;
    private CheckBox chbMoney, chbBoom;
    private LinearLayout llRightContent;

    private SettingPop settingPop;
    private NewShopPop shopPop;
    private DefensePop defensePop;
    private PersonalCenterPop personalCenterPop;
    private ScanPop scanPop;
    private ArsenalPop arsenalPop;
    private FriendListPop friendListPop;
    private RecordPop recordPop;
    private AdvicePop advicePop;
    private ReportPop reportPop;
    private GetRecordPop getRecordPop;
    private RecordListPop recordListPop;
    private ShopBuyPop shopBuyPop;
    private SelectPayTypePop selectPayPop;
    private BoomPop boomPop;
    private SelectPayTypeAllPop selectPayTypeAllPop;
    private BandPhonePop bandPhonePop;
    private MessageDialog messageDialog;
    private ConvertRedPop convertRedPop;
    private AddFriendPop addFriendPop;
    private NotifyPop notifyPop;
    private PutBoomTypePop putBoomTypePop;
    private ChatPop chatPop;
    private BoomDefensePop boomDefensePop;
    private ScanBoomPop scanBoomPop;
    private OpenRedPop openRedPop;
    private BoomPic boomPic;
    private BoomText boomText;
    private BoomGold boomGold;
    private GuidePop guidePop;
    private PickerCityDialog pickerCityDialog;
    private ChangePwdPop changePwdPop;

    private List<Bean_UserArm.UserArm> defenseList;
    private List<Bean_UserArm.UserArm> boomList;
    private List<Bean_UserArm.UserArm> scanList;
    private List<Marker> redMarkers; //红包marker,动态隐藏展示区域红包雷
    private List<Marker> goldMarkers; //寻宝雷marker,动态展示隐藏寻宝雷
    private List<Bean_MapBoom.MapBoom> redBooms;
    private List<Bean_MapBoom.MapBoom> goldBooms;

    public static boolean isForeground;
    private boolean isPutBoom = false; //标识现在是否有放雷的动作
    private boolean isScan = false; //标识现在是否使用扫雷器
    private boolean isDrag = false; //标识用户是否操作地图,如果操作地图则取消地图跟随

    //百度地图定位相关
    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    private int radius = 1000;//获取一千米范围内的雷

    private List<Bean_MapBoom.MapBoom> mapBoomList;

    //购买商品相关
    private String goodsId = "", goodsCount;

    private int boomCount = 0, scanCount = 0, defenseCount = 0;
    private PickerDialog ageDialog, workDialog;
    private List<String> workStrings = new ArrayList<>();
    private List<String> ageStrings = new ArrayList<>();

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    private Intent backIntent;
    private Vibrator vibrator;

    private int redGetRange = 100; //红包雷可以领取距离
    private int redVisRange = 1000; //红包雷可以显示距离

    private int goldGetRange = 100; //红包雷可以领取距离
    private int goldVisRange = 1000; //红包雷可以显示距离
    private float mapZoom = 17f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        backIntent = new Intent(context, BackgroundService.class);
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initLocation();
        }

        initView();
        getUserArm();
        getAllConfig();
        getMyBoom();
        JPushInterface.setAlias(getApplicationContext(), AppPrefrence.getEaseId(this), new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                String logs;
                switch (code) {
                    case 0:
                        logs = "Set tag and alias success";

                        // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                        break;
                    case 6002:
                        logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                        // 延迟 60 秒来调用 Handler 设置别名
                        break;
                    default:
                        logs = "Failed with errorCode = " + code;
                }
                Tools.debug(logs);
            }
        });
        Tools.debug(JPushInterface.isPushStopped(this) + "------");
        registerMessageReceiver();
        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        //注册联系人变动监听
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        initNotifier();
        checkUpdate();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            showGuide();
    }

    private void showGuide() {
        if (AppPrefrence.getIsFirst(this)) {
            if (guidePop == null)
                guidePop = new GuidePop(this);
            guidePop.showPop(txtArsenal);
            guidePop.setPopInterfacer(this, 38);
        }
        AppPrefrence.setIsFirst(this, false);
    }

    private EaseNotifier notifier;

    void initNotifier() {
        notifier = new EaseNotifier();
        notifier.init(this);
    }

    /**
     * 获取地图上我埋的雷
     */
    private void getMyBoom() {
        Map<String, String> params = new HashMap<>();
        params.put("PageIndex", "1");
        params.put("PageSize", "1000");
        PostTools.getData(context, CommonUntilities.USER_URL + "GetMyNormalMines", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("myBoom" + response);
                if (TextUtils.isEmpty(response))
                    return;
                Bean_MapBoom beanMapBoom = new Gson().fromJson(response, Bean_MapBoom.class);
                if (beanMapBoom != null && beanMapBoom.Success && beanMapBoom.Data != null && beanMapBoom.Data.size() > 0) {
                    for (int i = 0; i < beanMapBoom.Data.size(); i++) {
                        Bean_MapBoom.MapBoom mapBoom = beanMapBoom.Data.get(i);
                        boomRang = mapBoom.BombRange;

                        addMarker(new LatLng(mapBoom.Latitude, mapBoom.Longitude), mapBoom.MineType);
                        if (mapBoom.MineType != 3 && mapBoom.MineType != 4)
                            addOverLay(new LatLng(mapBoom.Latitude, mapBoom.Longitude));
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                Tools.debug("myBoom" + e.toString());
            }
        });
    }

    private String configString = "";

    /**
     * 获取所有军火配置信息
     */
    private void getAllConfig() {
        PostTools.getData(context, CommonUntilities.CONFIG_URL + "GetArmsConfig", new HashMap<String, String>(), new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                configString = response;
                if (!TextUtils.isEmpty(response)) {
                    Bean_AllConfig bean_allConfig = new Gson().fromJson(response, Bean_AllConfig.class);
                    if (bean_allConfig.Success) {
                        redGetRange = bean_allConfig.Data.RedPackMineConfig.CanRecRange;
                        redVisRange = bean_allConfig.Data.RedPackMineConfig.VisibleRange;
                        goldGetRange = bean_allConfig.Data.GoldMineConfig.CanRecRange;
                        goldVisRange = bean_allConfig.Data.GoldMineConfig.VisibleRange;
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initLocation();
        }

    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        myListener.setLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000 * 2;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    Bean_UserArm bean_userArm;

    private void getUserArm() {
        Map<String, String> params = new HashMap<>();
        PostTools.getData(this, CommonUntilities.USER_URL + "GetUserArms", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                bean_userArm = new Gson().fromJson(response, Bean_UserArm.class);
                if (bean_userArm != null && bean_userArm.Success) {
                    dealData();
                }
            }
        });
    }

    private void dealData() {
        scanList.clear();
        boomList.clear();
        defenseList.clear();
        if (bean_userArm.Data != null && bean_userArm.Data.size() > 0) {
            for (int i = 0; i < bean_userArm.Data.size(); i++) {
                Bean_UserArm.UserArm userArm = bean_userArm.Data.get(i);
                if (userArm.ArmType == 2 || userArm.ArmType == 3) {
                    scanList.add(userArm);
                }
                if (userArm.ArmType == 0 || userArm.ArmType == 1) {
                    boomList.add(userArm);
                }
                if (userArm.ArmType == 5 || userArm.ArmType == 4) {
                    defenseList.add(userArm);
                }
            }
            scanCount = 0;
            for (int i = 0; i < scanList.size(); i++) {
                scanCount += scanList.get(i).Count;
            }
            if (scanCount > 0) {
                imgScan.setEnabled(true);
                txtScan.setVisibility(View.VISIBLE);
                txtScan.setText(scanCount + "");
            } else {
                imgScan.setEnabled(false);
                txtScan.setVisibility(View.INVISIBLE);
            }
            defenseCount = 0;
            for (int i = 0; i < defenseList.size(); i++) {
                defenseCount += defenseList.get(i).Count;
            }
            if (defenseCount > 0) {
                imgDefense.setEnabled(true);
                txtDefense.setVisibility(View.VISIBLE);
                txtDefense.setText(defenseCount + "");
            } else {
                imgDefense.setEnabled(false);
                txtDefense.setVisibility(View.INVISIBLE);
            }

            boomCount = 0;
            for (int i = 0; i < boomList.size(); i++) {
                boomCount += boomList.get(i).Count;
            }
            if (boomCount > 0) {
                imgArsenal.setEnabled(true);
                txtArsenal.setVisibility(View.VISIBLE);
                txtArsenal.setText(boomCount + "");
            } else {
                imgArsenal.setEnabled(false);
                txtArsenal.setVisibility(View.INVISIBLE);
            }

        }
    }

    BitmapDescriptor mCurrentMarker;
//    private ExpandableSelector expandableSelector;

    private void initView() {
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        mapBoomList = new ArrayList<>();
        boomList = new ArrayList<>();
        defenseList = new ArrayList<>();
        scanList = new ArrayList<>();
        redBooms = new ArrayList<>();
        goldBooms = new ArrayList<>();
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate status = MapStatusUpdateFactory.zoomTo(mapZoom);
        mBaiduMap.setMapStatus(status);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）

        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.man_one);

        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(config);

        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMapLongClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setOnMapStatusChangeListener(this);

        imgDefense = (ImageView) findViewById(R.id.img_defense);
        imgDefense.setEnabled(false);
        imgArsenal = (ImageView) findViewById(R.id.img_arsenal);
        imgArsenal.setEnabled(false);
        imgRecord = (ImageView) findViewById(R.id.img_record);
        imgScan = (ImageView) findViewById(R.id.img_scan);
        imgScan.setEnabled(false);

        imgMsg = (ImageView) findViewById(R.id.img_msg);

        txtArsenal = (TextView) findViewById(R.id.txt_arsenal_count);
        txtArsenal.setVisibility(View.GONE);
        txtDefense = (TextView) findViewById(R.id.txt_defense_count);
        txtDefense.setVisibility(View.GONE);
        txtRecord = (TextView) findViewById(R.id.txt_record_count);
        txtRecord.setVisibility(View.GONE);
        txtScan = (TextView) findViewById(R.id.txt_scan_count);
        txtScan.setVisibility(View.GONE);

        imgLocation = (ImageView) findViewById(R.id.img_location);
        imgLocation.setVisibility(View.GONE);

        chbBoom = (CheckBox) findViewById(R.id.chb_set_treasure);
        chbBoom.setChecked(AppPrefrence.getIsBoomShow(context));
        chbBoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //红包雷
                AppPrefrence.setIsBoomShow(context, isChecked);
                if ((redMarkers == null || redMarkers.size() == 0) && (redBooms == null || redBooms.size() == 0))
                    return;
                else if ((redMarkers == null || redMarkers.size() == 0) && redBooms.size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < redBooms.size(); i++) {
                                Bean_MapBoom.MapBoom boom = redBooms.get(i);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", boom.MineRecordId);
                                bundle.putInt("type", boom.MineType);
                                bundle.putInt("boomRange", boom.BombRange);
                                bundle.putString("userId", boom.UserId);
                                addRedMarker(new LatLng(boom.Latitude, boom.Longitude), bundle);
                            }
                        }
                    }).start();
                    return;
                }
                if (isChecked) {
                    List<Marker> tempMarkers = new ArrayList<Marker>();
                    for (int i = 0; i < redMarkers.size(); i++) {
                        Marker marker = redMarkers.get(i);
                        OverlayOptions options = new MarkerOptions().draggable(false).extraInfo(marker.getExtraInfo())
                                .icon(marker.getIcon()).position(marker.getPosition()).zIndex(marker.getZIndex());
                        tempMarkers.add((Marker) mBaiduMap.addOverlay(options));
                    }
                    redMarkers.clear();
                    redMarkers.addAll(tempMarkers);
                } else {
                    for (int i = 0; i < redMarkers.size(); i++) {
                        redMarkers.get(i).remove();
                    }
                }
            }
        });

        chbMoney = (CheckBox) findViewById(R.id.chb_set_red_packet);
        chbMoney.setChecked(AppPrefrence.getIsRedShow(context));
        chbMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //寻宝雷
                AppPrefrence.setIsRedShow(context, isChecked);
                if ((goldMarkers == null || goldMarkers.size() == 0) && (goldBooms == null || goldBooms.size() == 0))
                    return;
                else if (isChecked && (goldMarkers == null || goldMarkers.size() == 0) && goldBooms.size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < goldBooms.size(); i++) {
                                Bean_MapBoom.MapBoom boom = goldBooms.get(i);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", boom.MineRecordId);
                                bundle.putInt("type", boom.MineType);
                                bundle.putInt("boomRange", boom.BombRange);
                                bundle.putString("userId", boom.UserId);
                                addGoldMarker(new LatLng(boom.Latitude, boom.Longitude), bundle);
                            }
                        }
                    }).start();
                    return;
                }

                if (isChecked) {
                    List<Marker> tempMarkers = new ArrayList<Marker>();
                    for (int i = 0; i < goldMarkers.size(); i++) {
                        Marker marker = goldMarkers.get(i);
                        OverlayOptions options = new MarkerOptions().draggable(false).extraInfo(marker.getExtraInfo())
                                .icon(marker.getIcon()).position(marker.getPosition()).zIndex(marker.getZIndex());
                        tempMarkers.add((Marker) mBaiduMap.addOverlay(options));
                    }
                    goldMarkers.clear();
                    goldMarkers.addAll(tempMarkers);
                } else {
                    for (int i = 0; i < goldMarkers.size(); i++) {
                        goldMarkers.get(i).remove();
                    }

                }
            }
        });

        txtMsg = (TextView) findViewById(R.id.txt_home_msg);
        txtMsg.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) txtMsg.getLayoutParams();
        params.width = (int) (Tools.getScreenWide(context) * 0.6);
        txtMsg.setLayoutParams(params);
        txtMsgCount = (TextView) findViewById(R.id.txt_msg_count);
        txtMsgCount.setVisibility(View.GONE);

        llRightContent = (LinearLayout) findViewById(R.id.ll_right_content);
        RelativeLayout.LayoutParams paramsContent = (RelativeLayout.LayoutParams) llRightContent.getLayoutParams();
        paramsContent.width = (int) (Tools.getScreenWide(this) / 8 + Tools.dip2px(this, 50) / 2);
        llRightContent.setLayoutParams(paramsContent);
    }

    /**
     * 隐藏msgTextView
     */
    private void txtMsgGone() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation goneAim = new TranslateAnimation(type, 0f, type, 1f, type, 0f, type, 0f);
        goneAim.setFillAfter(true);
        goneAim.setDuration(200);
        goneAim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMsg.setVisibility(View.GONE);
                        txtMsg.clearAnimation();
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        txtMsg.setAnimation(goneAim);
        txtMsg.startAnimation(goneAim);
        txtMsg.setVisibility(View.GONE);

    }

//    private void txtMsgVis() {
//        txtMsg.setVisibility(View.VISIBLE);
//        int type = TranslateAnimation.RELATIVE_TO_SELF;
//        TranslateAnimation visAnim = new TranslateAnimation(type, 1f, type, 0f, type, 0f, type, 0f);
//        visAnim.setDuration(200);
//        visAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        txtMsg.setAnimation(visAnim);
//        txtMsg.startAnimation(visAnim);
//        new CountDownTimer(2000, 2000) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                txtMsg.clearAnimation();
//                txtMsgGone();
//            }
//        }.start();
//    }

    @Override
    public void boomClick(View v) {
        super.boomClick(v);
        switch (v.getId()) {
            case R.id.rel_defense:
                //防爆衣
                if (defensePop == null)
                    defensePop = new DefensePop(context);
                defensePop.showPop(imgArsenal);
                defensePop.setData(defenseList);
                defensePop.setPopInterfacer(this, 2);
                break;
            case R.id.rel_arsenal:
                //军火库
                if (arsenalPop == null)
                    arsenalPop = new ArsenalPop(context);
                arsenalPop.showPop(imgArsenal);
                arsenalPop.setData(boomList);
                arsenalPop.setPopInterfacer(this, 5);
                isScan = false;
                break;
            case R.id.rel_friend_center:
                //好友中心
                if (friendListPop == null)
                    friendListPop = new FriendListPop(context);
                friendListPop.showPop(imgArsenal);
                friendListPop.setPopInterfacer(this, 6);
                break;
            case R.id.rel_personal_center:
                //个人中心
                if (personalCenterPop == null)
                    personalCenterPop = new PersonalCenterPop(context);
                personalCenterPop.showPop(imgArsenal);
                personalCenterPop.setPopInterfacer(this, 3);
                break;
            case R.id.rel_record:
                //战绩
                if (recordPop == null)
                    recordPop = new RecordPop(context);
                recordPop.showPop(imgArsenal);
                recordPop.setPopInterfacer(this, 7);
                break;
            case R.id.rel_scan:
                //扫雷器
                if (scanPop == null)
                    scanPop = new ScanPop(context);
                scanPop.showPop(imgArsenal);
                scanPop.setData(scanList);
                scanPop.setPopInterfacer(this, 4);
                isPutBoom = false;
                break;
            case R.id.rel_settings:
                //设置中心
                if (settingPop == null)
                    settingPop = new SettingPop(context);
                settingPop.setPopInterfacer(Home.this, 0);
                settingPop.showPop(imgArsenal);
                break;
            case R.id.rel_shop:
                //商店
                if (shopPop == null)
                    shopPop = new NewShopPop(context);
                shopPop.setPopInterfacer(Home.this, 1);
                shopPop.showPop(imgArsenal);
                break;
            case R.id.img_msg:
//                if (!expandableSelector.isExpanded()) {
//                    expandableSelector.setVisibility(View.VISIBLE);
//                    expandableSelector.expand();
//                } else {
//                    expandableSelector.setVisibility(View.INVISIBLE);
//                    expandableSelector.collapse();
//                }
                startActivity(new Intent(this, FriendMessage.class));
                break;
            case R.id.img_location:
                MyLocationData locData = new MyLocationData.Builder()
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(walkLat.latitude)
                        .longitude(walkLat.longitude).build();
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(walkLat, mapZoom);
                mBaiduMap.animateMapStatus(update);
                imgLocation.setVisibility(View.GONE);
                isDrag = false;
                break;
            case R.id.img_put_boom:
                if (walkLat != null) {
                    initGeoCoder(walkLat);
                    mineType = "0";
                    latItude = walkLat.latitude + "";
                    longItude = walkLat.longitude + "";
                    putCommentBoom();
                }
                break;
        }
    }


    @Override
    public void OnDismiss(int flag) {
        switch (flag) {
            case 0:
                //设置
                settingPop = null;
                break;
            case 1:
                //商店
                shopPop = null;
                break;
            case 2:
                //防弹衣
                defensePop = null;
                break;
            case 3:
                //个人中心
                personalCenterPop = null;
                break;
            case 4:
                //扫雷器
                scanPop = null;
                break;
            case 5:
                arsenalPop = null;
                break;
            case 6:
                friendListPop = null;
                break;
            case 7:
                recordPop = null;
                break;
            case 8:
                advicePop = null;
                break;
            case 9:
                reportPop = null;
                break;
            case 10:
                getRecordPop = null;
                break;
            case 11:
                recordListPop = null;
                break;
            case 12:
//                createPayPwd = null;
                break;
            case 13:
//                changePayPwd = null;
                break;
            case 14:
//                confirmPwdPop = null;
                break;
            case 15:
                shopBuyPop = null;
                break;
            case 16:
                selectPayPop = null;
                break;
            case 17:
//                personalInfo = null;
                break;
            case 18:
                boomPop = null;
                break;
            case 19:

                break;
            case 20:
//                goldBuyPop = null;
                break;
            case 21:
                selectPayTypeAllPop = null;
                break;
            case 22:
                bandPhonePop = null;
                break;
            case 23:
                messageDialog = null;
                break;
            case 24:
                convertRedPop = null;
                break;
            case 25:
//                confirmPwdPop = null;
                break;
            case 26:
                addFriendPop = null;
                break;
            case 27:
                notifyPop = null;
                break;
            case 28:
                putBoomTypePop = null;
                isPutBoom = false;
                break;
            case 29:
                chatPop = null;
                addMsgListener();
                break;
            case 30:
                notifyPop = null;
                break;
            case 31:
                notifyPop = null;
                break;
            case 32:
                boomDefensePop = null;
                break;
            case 33:
                scanBoomPop = null;
                isScan = false;
                break;
            case 34:
                openRedPop = null;
                break;
            case 35:
                boomPic = null;
                break;
            case 36:
                boomText = null;
                break;
            case 37:
                boomGold = null;
                break;
            case 38:
                guidePop = null;
                break;
            case 39:
                pickerCityDialog = null;
                break;
            case 40:
                changePwdPop = null;
                break;
        }
    }

    private int chargeTyp = -1;
    private float money = 0.00f;
    private int armType = 0;
    private int goldAmout = 0;//金币数量
    private float goldPrice = 0.00f;//金幣價格

    @Override
    public void OnConfirm(int flag, Bundle bundle) {
        switch (flag) {
            case 0: //设置窗口
                if (bundle != null && bundle.getInt("type", -1) == 0) {//建议
                    if (advicePop == null)
                        advicePop = new AdvicePop(context);
                    advicePop.showPop(imgArsenal);
                    advicePop.setPopInterfacer(this, 8);
                }
                if (bundle != null && bundle.getInt("type", 0) == 1) { //邀请好友
                    showShare(CommonUntilities.SHARE_TITLE, CommonUntilities.SHARE_REGISTER + AppPrefrence.getUserName(context), CommonUntilities.SHARE_CONTENT, "");
                }
                if (bundle != null && bundle.getInt("type", 0) == 2) { //音效

                }
                if (bundle != null && bundle.getInt("type", 0) == 3) { //推送

                }
                if (bundle != null && bundle.getInt("type", 0) == 4) { //背景音
                    if (!AppPrefrence.getIsBack(context)) {
                        startService(backIntent);
                    } else stopService(backIntent);
                }
                if (bundle != null && bundle.getInt("type", 0) == 5) {
                    if (guidePop == null)
                        guidePop = new GuidePop(context);
                    guidePop.showPop(txtArsenal);
                    guidePop.setPopInterfacer(this, 38);
                }
                break;
            case 1: //商店窗口
                if (bundle == null)
                    return;
                goodsId = bundle.getString("id");
                money = bundle.getFloat("money");
                if (bundle.getInt("type", -1) == 0) {//购买工具弹窗
                    if (shopBuyPop == null)
                        shopBuyPop = new ShopBuyPop(context);
                    shopBuyPop.showPop(imgArsenal);
                    shopBuyPop.setPopInterfacer(this, 15);
                    shopBuyPop.setPrice((int) money);
                    armType = bundle.getInt("armType");
                    shopBuyPop.setGoodPic(armType);
                }
                if (bundle.getInt("type", -1) == 1) {//金币直接购买
                    goldPrice = bundle.getFloat("money", 0.00f);
                    goldAmout = bundle.getInt("amount");
                    if (selectPayTypeAllPop == null)
                        selectPayTypeAllPop = new SelectPayTypeAllPop(context);
                    selectPayTypeAllPop.setPopInterfacer(this, 21);
                    selectPayTypeAllPop.showPop(txtArsenal);
                }
                break;
            case 3:  //个人中心
                if (bundle != null && bundle.getInt("type", -1) == 0) {
                    if (recordListPop == null)
                        recordListPop = new RecordListPop(context);
                    recordListPop.showPop(imgArsenal);
                    recordListPop.setPopInterfacer(this, 11);
                }
                if (bundle != null && bundle.getInt("type", -1) == 7) {
                    if (workStrings.size() == 0) {
                        workStrings.add("学生");
                        workStrings.add("老师");
                        workStrings.add("白领");
                        workStrings.add("蓝领");
                        workStrings.add("司机");
                        workStrings.add("高管");
                        workStrings.add("其他");
                    }
                    if (workDialog == null)
                        workDialog = new PickerDialog(context, workStrings, txtArsenal);
                    workDialog.showPop();
                    workDialog.setOnSelectItem(new PickerDialog.OnSelectItem() {
                        @Override
                        public void onItemSelect(String selectString) {
                            if (personalCenterPop != null)
                                personalCenterPop.setWorkd(selectString);
                        }
                    });
                }
                if (bundle != null && bundle.getInt("type", -1) == 8) {
                    if (ageStrings == null || ageStrings.size() == 0) {
                        for (int i = 16; i < 65; i++) {
                            ageStrings.add(i + "");
                        }
                    }

                    if (ageDialog == null)
                        ageDialog = new PickerDialog(context, ageStrings, txtArsenal);
                    ageDialog.showPop();
                    ageDialog.setOnSelectItem(new PickerDialog.OnSelectItem() {
                        @Override
                        public void onItemSelect(String selectString) {
                            if (personalCenterPop != null)
                                personalCenterPop.setAge(selectString);
                        }
                    });
                }
                if (bundle != null && bundle.getInt("type", -1) == 6) {
                    //编辑个人信息
                    if (bundle == null)
                        return;
                    saveUserInfo(bundle);
                }
//                if (bundle != null && bundle.getInt("type", -1) == 2) {
//                    //2创建,3修改
//                    if (createPayPwd == null)
//                        createPayPwd = new CreatePayPwdPop(context);
//                    createPayPwd.showPop(imgArsenal);
//                    createPayPwd.setPopInterfacer(this, 12);
//                }
//                if (bundle != null && bundle.getInt("type", -1) == 3) {
//                    if (TextUtils.isEmpty(AppPrefrence.getUserPhone(context))) {
//                        //提示绑定手机号码
//                        dissAllPop();
//                        if (bandPhonePop == null)
//                            bandPhonePop = new BandPhonePop(context);
//                        bandPhonePop.showPop(txtArsenal);
//                        bandPhonePop.setPopInterfacer(this, 22);
//
//                    } else {
//                        if (changePayPwd == null)
//                            changePayPwd = new ChangePayPwdPop(context);
//                        changePayPwd.showPop(imgArsenal);
//                        changePayPwd.setPopInterfacer(this, 13);
//                    }
//
//                }
                if (bundle != null && bundle.getInt("type", -1) == 5 || bundle.getInt("type", -1) == 4) {
                    if (selectPayPop == null)
                        selectPayPop = new SelectPayTypePop(context);
                    selectPayPop.showPop(txtArsenal);
                    selectPayPop.setPopInterfacer(this, 16);
                    chargeTyp = bundle.getInt("type", -1);
                }
                if (bundle != null && bundle.getInt("type", 9) == 9) {
                    if (convertRedPop == null)
                        convertRedPop = new ConvertRedPop(context);
                    convertRedPop.showPop(txtArsenal);
                    convertRedPop.setPopInterfacer(this, 24);
                }
                if (bundle != null && bundle.getInt("type", -1) == 10) {
                    File file = new File(Environment.getExternalStorageDirectory(), "/findBoom/" + System.currentTimeMillis() + ".jpg");
                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                    final Uri imageUri = Uri.fromFile(file);
                    CharSequence[] items = {"手机相册", "手机拍照"};
                    final TakePhoto takePhoto = getTakePhoto();
                    final CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                    new AlertDialog.Builder(this).setTitle("选择照片").setCancelable(true).setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                                    break;
                                case 1:
                                    takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                                    break;
                                case 2:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }

                if (bundle != null && bundle.getInt("type") == 11) {
                    if (pickerCityDialog == null)
                        pickerCityDialog = new PickerCityDialog(context);
                    pickerCityDialog.showPop(txtArsenal);
                    pickerCityDialog.setPopInterfacer(this, 39);
                }
                if (bundle != null && bundle.getInt("type") == 12) {
                    //修改密码
                    if (changePwdPop == null)
                        changePwdPop = new ChangePwdPop(context);
                    changePwdPop.setPopInterfacer(this, 40);
                    changePwdPop.showPop(txtArsenal);
                }
                break;
            case 4:

                if (scanCount <= 0) {
                    if (notifyPop == null)
                        notifyPop = new NotifyPop(context);
                    notifyPop.invisiableChb();
                    notifyPop.setNotify("扫雷器已用完,请购买后使用");
                    notifyPop.showPop(txtArsenal);
                    isScan = false;
                    return;
                }
                int pos = bundle.getInt("pos");
                if (scanList.get(pos).ArmType == 3 && scanList.get(pos).Count == 0) {
                    //没有临时扫雷器
                    if (notifyPop == null)
                        notifyPop = new NotifyPop(context);
                    notifyPop.invisiableChb();
                    notifyPop.visiableClose();
                    notifyPop.setNotify("临时扫雷器已用完,继续将使用永久扫雷器");
                    notifyPop.setPopInterfacer(this, 30);
                    notifyPop.showPop(txtArsenal);
                    return;
                }
                isPutBoom = false;
                isScan = true;
                if (!AppPrefrence.getIsNotify(this)) {
                    if (notifyPop == null)
                        notifyPop = new NotifyPop(context);
                    notifyPop.setNotify(R.string.scan_notify);
                    notifyPop.showPop(txtArsenal);
                    notifyPop.setPopInterfacer(this, 27);
                }

                break;
            case 5: //放置地雷
                if (boomCount <= 0) {
                    if (notifyPop == null)
                        notifyPop = new NotifyPop(context);
                    notifyPop.invisiableChb();
                    notifyPop.setNotify(R.string.boom_empty);
//                    notifyPop.setPopInterfacer(this,30);
                    notifyPop.showPop(txtArsenal);
                    isPutBoom = false;
                    return;
                }
                int posB = bundle.getInt("pos");
                if (boomList.get(posB).ArmType == 1 && boomList.get(posB).Count == 0) {
                    //没有临时雷
                    if (notifyPop == null)
                        notifyPop = new NotifyPop(context);
                    notifyPop.invisiableChb();
                    notifyPop.visiableClose();
                    notifyPop.setNotify("临时雷已用完,继续将使用永久雷");
                    notifyPop.setPopInterfacer(this, 31);
                    notifyPop.showPop(txtArsenal);
                    return;
                }
                isPutBoom = true;
                isScan = false;
                if (!AppPrefrence.getIsNotify(this)) {
                    if (notifyPop == null)
                        notifyPop = new NotifyPop(context);
                    notifyPop.setNotify(R.string.boom_notify);
                    notifyPop.showPop(txtArsenal);
                    notifyPop.setPopInterfacer(this, 27);
                }
                break;
            case 6:
                if (bundle == null)
                    return;
                if (chatPop == null)
                    chatPop = new ChatPop(context);
                chatPop.showPop(txtArsenal);
                chatPop.setChatId(bundle.getString("id"));
                chatPop.setChatName(bundle.getString("name"));
                chatPop.setPopInterfacer(this, 29);
                if (friendListPop != null && friendListPop.isShowing())
                    friendListPop.dismiss();
                removeMsgListener();
                break;
            case 7:   //战绩-->举报
                if (bundle != null && bundle.getInt("type", 0) == 0) {
                    if (reportPop == null)
                        reportPop = new ReportPop(context);
                    reportPop.showPop(imgArsenal);
                    reportPop.setId(bundle.getString("userId"));
                    reportPop.setPopInterfacer(this, 9);
                }
                if (bundle != null && bundle.getInt("type", -1) == 1) {
                    //添加好友
                    if (addFriendPop == null)
                        addFriendPop = new AddFriendPop(context);
                    addFriendPop.showPop(txtArsenal);
                    addFriendPop.setId(bundle.getString("id"));
                    addFriendPop.setPopInterfacer(this, 26);
                }
                if (bundle != null && bundle.getInt("type", -1) == 2) {
                    showShare(CommonUntilities.SHARE_TITLE, CommonUntilities.SHARE_RECORD + AppPrefrence.getUserName(context), CommonUntilities.SHARE_CONTENT, "");
                }
                break;
            case 12: //个人中心-->创建支付密码
                if (personalCenterPop != null)
                    personalCenterPop.setBottomStatu(0);
                break;
            case 14: //确认支付密码-->确认结果
                //账户余额购买军火
                if (bundle == null) {
                    //余额不足
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "余额不足,请充值", "").showPop();
                    return;
                }
                if (isBuyGold)
                    redBuyGold();
                else
                    accountBuy();
                break;
            case 15:
                //余额支付,确认支付密码
                if (bundle == null)
                    return;
                goodsCount = bundle.getInt("count") + "";
                money = bundle.getFloat("money", 0.00f);
//                if (confirmPwdPop == null)
//                    confirmPwdPop = new ConfrimPwdPop(context);
//                confirmPwdPop.showPop(txtArsenal);
//                confirmPwdPop.setMoney(money);
//                confirmPwdPop.setPopInterfacer(this, 14);
                isBuyGold = false;
                float account = 0f;
                Bean_UserInfo.GameUser userInfo = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                if (userInfo != null) {
                    if (TextUtils.isEmpty(userInfo.UserBalance))
                        account = 0f;
                    else account = Float.parseFloat(userInfo.UserBalance);
                }
                if (money > account) {
                    //余额不足
                    Tools.toastMsgCenter(context, "余额不足,请充值");
                    return;
                }
                if (isBuyGold)
                    redBuyGold();
                else
                    accountBuy();
                break;
            case 16:
                //充值红包选择支付方式
                if (bundle == null)
                    return;
                String type = "" + bundle.getInt("type");
                money = bundle.getFloat("money");
                if (chargeTyp == 5) {
                    isBuyGold = false;
                    recahrgeRed(type);
                }
                break;
            case 17:
                //编辑个人信息
                if (bundle == null)
                    return;
                saveUserInfo(bundle);
                break;
            case 19:
                if (bundle == null) {
                    //余额不足
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "余额不足,请充值", "").showPop();
                } else {

                }
                break;
            case 20:
                break;
            case 21:
                //购买金币选择支付方式
                if (bundle == null)
                    return;
                int typeG = bundle.getInt("type");
                if (typeG != 2) {
                    isBuyGold = true;
                    buyGold(typeG);
                } else {
                    //余额支付
                    isBuyGold = true;
                    float account1 = 0f;
                    Bean_UserInfo.GameUser userInfo1 = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                    if (userInfo1 != null) {
                        if (TextUtils.isEmpty(userInfo1.RedPackBalance))
                            account1 = 0f;
                        else account1 = Float.parseFloat(userInfo1.RedPackBalance);
                    }
                    if (money > account1) {
                        //余额不足
                        Tools.toastMsgCenter(context, "余额不足,请充值");
                        return;
                    }
                    if (isBuyGold)
                        redBuyGold();
                    else
                        accountBuy();
                }
                break;
            case 23:
                if (bundle == null)
                    return;
                if (bundle.getInt("type") == 1) {

                }
                if (bundle.getInt("type") == 2) {

                }
                break;
            case 24:
                if (bundle == null)
                    return;
                money = Float.parseFloat(bundle.getString("money"));
                alipayNo = bundle.getString("alipay");
                alipayName = bundle.getString("name");
                convertRed(bundle.getString("pwd"));
//                if (confirmPwdPop == null)
//                    confirmPwdPop = new ConfrimPwdPop(context);
//                confirmPwdPop.showPop(txtArsenal);
//                confirmPwdPop.setPopInterfacer(this, 25);
                break;
            case 25:
                if (bundle == null)
                    return;
                convertRed(bundle.getString("pwd"));
//                confirmPwdPop.dismiss();
                break;
            case 28:
                if (bundle == null)
                    return;
                int typeBoom = bundle.getInt("type");
                mineType = typeBoom + "";
                switch (typeBoom) {
                    case 4:
                        //寻宝雷
//                        mineType = "4";
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("pro", provice);
                        bundle1.putString("city", city);
                        bundle1.putString("lat", latItude);
                        bundle1.putString("lng", longItude);
                        bundle1.putString("street", street);
                        bundle1.putString("area", area);
                        bundle1.putString("config", configString);
                        startActivityForResult(new Intent(context, PutGoldBoom.class).putExtra("data", bundle1), 2);
                        isPutBoom = false;
                        break;
                    case 2:
                        //图片雷
//                        mineType = "2";
                        isPutBoom = false;
                        startActivityForResult(new Intent(context, PutPicBoom.class).putExtra("config", configString).putExtra("type", 2), 0);
                        break;
                    case 0:
                        //普通雷
//                        mineType = "0";
                        putCommentBoom();
                        isPutBoom = false;
                        break;
                    case 1:
                        //文字雷
//                        mineType = "1";
                        startActivityForResult(new Intent(context, PutCommenBoom.class).putExtra("config", configString).putExtra("type", 1), 0);
                        isPutBoom = false;
                        break;
                    case 3:
                        //红包雷
//                        mineType = "3";
                        startActivityForResult(new Intent(context, PutRedBoom.class).putExtra("config", configString), 1);
                        isPutBoom = false;
                        break;
                }

                break;
            case 30:
                isScan = true;
                isPutBoom = false;
                break;
            case 31:
                isPutBoom = true;
                isScan = false;
                break;
            case 39:
                if (bundle != null)
                    if (personalCenterPop != null) {
                        personalCenterPop.setCity(bundle.getString("city"));
                        personalCenterPop.setProvince(bundle.getString("pro"));
                    }
                break;
        }
    }

    String alipayName = "", alipayNo = "";

    /**
     * 提现
     *
     * @param pwd
     */
    private void convertRed(String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("AlipayName", alipayName);
        params.put("AlipayAccount", alipayNo);
        params.put("Amount", money + "");
//        params.put("PayPassWord", Tools.get32MD5StrWithOutKey(pwd));
        PostTools.postData(context, CommonUntilities.WITH_URL + "ApplyWithdraw", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("convertRed" + response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsgCenter(context, "网络错误,请检查后重试");
                    return;
                }
                Bean_UserArm baseBean = new Gson().fromJson(response, Bean_UserArm.class);
                if (baseBean != null && baseBean.Success) {
                    Tools.toastMsgCenter(context, baseBean.Msg);
//                    String balance = "";
//                    recharMoney = "";
//                    float moneyF = 0.00f;
//                    Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
//                    if (user != null)
//                        balance = user.RedPackBalance;
//                    if (!TextUtils.isEmpty(balance))
//                        moneyF = Float.parseFloat(balance) - money;
//                    recharMoney = decentFloat(moneyF);
//                    if (personalCenterPop != null)
//                        personalCenterPop.setRed(recharMoney);
//                    user.RedPackBalance = recharMoney;
//                    BoomDBManager.getInstance().setUserData(user);
                    if (convertRedPop != null && convertRedPop.isShowing())
                        convertRedPop.dismiss();
                } else
                    Tools.toastMsgCenter(context, baseBean.Msg);
            }
        });
    }

    @Override
    public void OnCancle(int flag) {
        switch (flag) {
            case 0:
                //退出登录
                //设置
                logOut();
                settingPop = null;
                break;
            case 3:
                File file = new File(Environment.getExternalStorageDirectory(), "/findBoom/" + System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                final Uri imageUri = Uri.fromFile(file);
                CharSequence[] items = {"手机相册", "手机拍照"};
                final TakePhoto takePhoto = getTakePhoto();
                final CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                new AlertDialog.Builder(this).setTitle("选择照片").setCancelable(true).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                                break;
                            case 1:
                                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
            case 14:
                //forget pwd
//                if (TextUtils.isEmpty(AppPrefrence.getUserPhone(context))) {
//                    //忘记密码-->绑定手机号码
//                    dissAllPop();
//                    if (bandPhonePop == null)
//                        bandPhonePop = new BandPhonePop(context);
//                    bandPhonePop.showPop(txtArsenal);
//                    bandPhonePop.setPopInterfacer(this, 22);
//                } else {
//                    if (changePayPwd == null)
//                        changePayPwd = new ChangePayPwdPop(context);
//                    changePayPwd.showPop(imgArsenal);
//                    changePayPwd.setPopInterfacer(this, 13);
//                }
                break;
            case 17:
                break;
            case 25:
                //forget pwd
//                if (TextUtils.isEmpty(AppPrefrence.getUserPhone(context))) {
//                    //忘记密码-->绑定手机号码
//                    dissAllPop();
//                    if (bandPhonePop == null)
//                        bandPhonePop = new BandPhonePop(context);
//                    bandPhonePop.showPop(txtArsenal);
//                    bandPhonePop.setPopInterfacer(this, 22);
//                } else {
//                    if (changePayPwd == null)
//                        changePayPwd = new ChangePayPwdPop(context);
//                    changePayPwd.showPop(imgArsenal);
//                    changePayPwd.setPopInterfacer(this, 13);
//                }
                break;
            case 30:
                isScan = false;
                isPutBoom = false;
                break;
            case 31:
                isScan = false;
                isPutBoom = false;
                break;
        }
    }

    /**
     * 红包购买金币
     */
    private void redBuyGold() {
        Map<String, String> params = new HashMap<>();
        params.put("GoldId", goodsId);
//        params.put("PayPassWord", Tools.get32MD5StrWithOutKey(pwd));
        PostTools.postData(context, CommonUntilities.ORDER_URL + "BuyGold", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "网络错误", "检查网络后重试").showPop();
                    return;
                }
                Bean_UserArm baseBean = new Gson().fromJson(response, Bean_UserArm.class);
                if (baseBean != null && baseBean.Success) {
                    toastSuccess();
                    FindBoomApplication.getInstance().playMoneySound();
                    String balance = "";
                    String account = "";
                    recharMoney = "";
                    float moneyF = 0.00f;
                    float accountF = 0.00f;
                    Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                    if (user != null) {
                        balance = user.RedPackBalance;
                        account = user.UserBalance;
                    }
                    if (!TextUtils.isEmpty(balance))
                        moneyF = Float.parseFloat(balance) - goldPrice;
                    if (!TextUtils.isEmpty(account))
                        accountF = Float.parseFloat(account) + goldAmout;

                    recharMoney = decentFloat(moneyF);
                    user.RedPackBalance = recharMoney;
                    user.UserBalance = decentFloat(accountF);
                    BoomDBManager.getInstance().setUserData(user);
                    recharMoney = "";
                    goldAmout = 0;
                } else Tools.toastMsgCenter(context, baseBean.Msg);
            }
        });
    }

    private boolean isBuyGold = false;

    /**
     * 微信支付宝购买金币
     *
     * @param type
     */
    private void buyGold(final int type) {
        Map<String, String> params = new HashMap<>();
        params.put("GoldId", goodsId);
        params.put("PayMentType", type + "");
        PostTools.postData(context, CommonUntilities.RECHARGE_URL + "RechargeGold", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsgCenter(context, "网络错误检查网络后重试");
                    return;
                }

                if (type == 1) {
                    //支付宝支付
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object != null && object.getBoolean("Success")) {
                            String orderInfo = object.getString("Data");
                            if (TextUtils.isEmpty(orderInfo)) {
                                Tools.toastMsgCenter(context, "支付失败,请重试");
                                return;
                            }

                            new AliPayHelper(Home.this, payHandler).pay(orderInfo);
                        }

                    } catch (Exception e) {
                        Tools.toastMsgCenter(context, "支付失败,请重试");
                    }
                } else {
                    //微信支付
                }

                if (type == 0) {
                    Bean_WXpay bean_wXpay = new Gson().fromJson(response, Bean_WXpay.class);
                    if (bean_wXpay.Success) {
                        new WxPayHelper(context).genPayReq(bean_wXpay.Data.appid, bean_wXpay.Data.partnerid, bean_wXpay.Data.prepayid, bean_wXpay.Data.packageValue, bean_wXpay.Data.noncestr,
                                bean_wXpay.Data.timestamp, bean_wXpay.Data.sign);
                    } else
                        Tools.toastMsgCenter(context, "支付失败,请重试");
                }
            }
        });
    }

    String recharMoney = "";
    private float aliMoney = 0.00f;
    Handler payHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Tools.debug("money--" + money);
            if (msg.what == 0) {
                String balance = "";
                recharMoney = "";
                float moneyF = 0.00f;
                Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                if (!isBuyGold) {
                    if (user != null)
                        balance = user.RedPackBalance;
                    if (!TextUtils.isEmpty(balance))
                        moneyF = Float.parseFloat(balance) + aliMoney;
                    recharMoney = decentFloat(moneyF);
                    if (personalCenterPop != null) {
                        if (chargeTyp == 4) {
                            personalCenterPop.setMoney(recharMoney);
                            user.UserBalance = recharMoney;
                        } else {
                            personalCenterPop.setRed(recharMoney);
                            user.RedPackBalance = recharMoney;
                        }
                    }
                } else {
                    toastSuccess();
                    FindBoomApplication.getInstance().playMoneySound();
                    if (user != null)
                        balance = user.UserBalance;
                    if (!TextUtils.isEmpty(balance))
                        moneyF = Float.parseFloat(balance) + goldAmout;
                    recharMoney = decentFloat(moneyF);
                    user.UserBalance = recharMoney;
                }
                BoomDBManager.getInstance().setUserData(user);
            } else {
                recharMoney = "";
                money = 0.00f;
            }
        }
    };

    public String decentFloat(float money) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(money);
    }

    /**
     * 充值红包
     *
     * @param type
     */
    private void recahrgeRed(final String type) {
        aliMoney = money;
        Map<String, String> params = new HashMap<>();
        params.put("RechargeAmount", money + "");
        params.put("PayMentType", type);
        PostTools.postData(context, CommonUntilities.RECHARGE_URL + "RechargeRedPack", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "网络错误", "检查网络后重试").showPop();
                    return;
                }
                if (TextUtils.equals("1", type)) {
                    //支付宝支付
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object != null && object.getBoolean("Success")) {
                            String orderInfo = object.getString("Data");
                            if (TextUtils.isEmpty(orderInfo)) {
                                new PostResultPop(context, txtArsenal, R.drawable.icon_error, "支付失败,请重试", "").showPop();
                                return;
                            }

                            if (TextUtils.equals(type, "1")) {
                                if (!TextUtils.isEmpty(orderInfo))
                                    orderInfo = orderInfo.replace("\\", "");
                                new AliPayHelper(Home.this, payHandler).pay(orderInfo);
                            }
                        }

                    } catch (Exception e) {
                        new PostResultPop(context, txtArsenal, R.drawable.icon_error, "支付失败,请重试", "").showPop();
                    }
                } else {
                    //微信支付
                    Bean_WXpay bean_wXpay = new Gson().fromJson(response, Bean_WXpay.class);
                    if (bean_wXpay.Success) {
                        new WxPayHelper(context).genPayReq(bean_wXpay.Data.appid, bean_wXpay.Data.partnerid, bean_wXpay.Data.prepayid, bean_wXpay.Data.packageValue, bean_wXpay.Data.noncestr,
                                bean_wXpay.Data.timestamp, bean_wXpay.Data.sign);
                    } else
                        new PostResultPop(context, txtArsenal, R.drawable.icon_error, "支付失败,请重试", "").showPop();
                }
                Tools.debug("money--" + money);
            }

        });
    }

    private void saveUserInfo(Bundle bundle) {
        Map<String, String> params = new HashMap<>();
        String name = bundle.getString("name");
        String pro = bundle.getString("pro");
        String age = bundle.getString("age");
        String cityString = bundle.getString("city");
        String proString = bundle.getString("province");
        int ageInt = TextUtils.isEmpty(age) ? 0 : Integer.parseInt(age);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        params.put("NickName", TextUtils.isEmpty(name) ? "" : name);
        params.put("City", TextUtils.isEmpty(cityString) ? city : cityString);
        params.put("Province", TextUtils.isEmpty(proString) ? provice : proString);
        params.put("Area", area);
        params.put("Profession", TextUtils.isEmpty(pro) ? "" : pro);
        params.put("UserProfile", "");
        params.put("Avatar", Tools.convertIconToString(photoPath));
        params.put("PhoneBrand", "");
        params.put("BirthDay", (year - ageInt) + "-01-01");

        PostTools.postData(context, CommonUntilities.USER_URL + "UpdateUserInfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "网络错误,请重试");
                    return;
                }
                Bean_UserInfo userInfo = new Gson().fromJson(response, Bean_UserInfo.class);
                if (userInfo != null && userInfo.Success) {
                    BoomDBManager.getInstance().updateUserData(userInfo.Data);
                    if (personalCenterPop != null)
                        personalCenterPop.setUserData(userInfo.Data);
                    Tools.toastMsgCenter(context, "修改成功");
                } else Tools.toastMsgCenter(context, userInfo.Msg);
            }
        });
    }

    /**
     * 余额支付
     */
    private void accountBuy() {
        Map<String, String> params = new HashMap<>();
        params.put("ArmInfoId", goodsId);
        params.put("Count", goodsCount);
//        params.put("PayPassWord", Tools.get32MD5StrWithOutKey(pwd));
        PostTools.postData(context, CommonUntilities.ORDER_URL + "CreateOrder", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "网络错误", "检查网络后重试").showPop();
                    return;
                }
                Bean_UserArm baseBean = new Gson().fromJson(response, Bean_UserArm.class);
                if (baseBean != null && baseBean.Success) {
                    goodsCount = "0";
                    Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                    float balance = Float.parseFloat(user.UserBalance);
                    balance -= money;
                    user.UserBalance = decentFloat(balance) + "";
                    BoomDBManager.getInstance().updateUserData(user);
                    money = 0.00f;
//                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, "购买成功", "").showPop();
                    toastSuccess();
//                    confirmPwdPop.dismiss();
                    shopBuyPop.dismiss();
                    defenseList.clear();
                    boomList.clear();
                    scanList.clear();
                    for (int i = 0; i < baseBean.Data.size(); i++) {
                        switch (baseBean.Data.get(i).ArmType) {
                            case 0:
                                boomList.add(baseBean.Data.get(i));
                                break;
                            case 1:
                                boomList.add(0, baseBean.Data.get(i));
                                break;
                            case 2:
                                scanList.add(baseBean.Data.get(i));

                                break;
                            case 3:
                                scanList.add(0, baseBean.Data.get(i));
                                break;
                            case 4:
                                defenseList.add(baseBean.Data.get(i));

                                break;
                            case 5:
                                defenseList.add(0, baseBean.Data.get(i));
                                break;
                        }
                    }
                    countData();
                } else
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, baseBean.Msg, "").showPop();
            }
        });
    }

    private void toastSuccess() {
        Toast toast = new Toast(this);
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.mipmap.buy_success);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private void countData() {
        boomCount = 0;
        for (int i = 0; i < boomList.size(); i++) {
            boomCount += boomList.get(i).Count;
        }
        if (boomCount > 0) {
            imgArsenal.setEnabled(true);
            txtArsenal.setVisibility(View.VISIBLE);
        } else {
            imgArsenal.setEnabled(false);
            txtArsenal.setVisibility(View.INVISIBLE);
        }

        txtArsenal.setText(boomCount + "");

        defenseCount = 0;
        for (int i = 0; i < defenseList.size(); i++) {
            defenseCount += defenseList.get(i).Count;
        }
        if (defenseCount > 0) {
            imgDefense.setEnabled(true);
            txtDefense.setVisibility(View.VISIBLE);
        } else {
            imgDefense.setEnabled(false);
            txtDefense.setVisibility(View.INVISIBLE);
        }
        txtDefense.setText(defenseCount + "");

        scanCount = 0;
        for (int i = 0; i < scanList.size(); i++) {
            scanCount += scanList.get(i).Count;
        }
        if (scanCount > 0) {
            imgScan.setEnabled(true);
            txtScan.setVisibility(View.VISIBLE);
        } else {
            imgScan.setEnabled(false);
            txtScan.setVisibility(View.INVISIBLE);
        }
        txtScan.setText(scanCount + "");
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    private String photoPath = "";

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        Tools.debug(result.getImage().getPath());
        photoPath = result.getImage().getPath();
        if (!TextUtils.isEmpty(photoPath)) {
            if (personalCenterPop != null && personalCenterPop.isShowing())
                personalCenterPop.setPhoto(photoPath);
        }
    }

    private void logOut() {
        AppPrefrence.setIsLogin(context, false);
        AppPrefrence.setUserName(context, "");
        AppPrefrence.setUserPhone(context, "");
        AppPrefrence.setUserPwd(context, "");
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        FindBoomApplication.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        finish();
                        startActivity(new Intent(context, LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();


                    }
                });
            }
        });
        finish();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        if (CommonUntilities.WXPAY) {
            CommonUntilities.WXPAY = false;
            String balance = "";
            recharMoney = "";
            float moneyF = 0.00f;

            Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
            if (!isBuyGold) {
                if (user != null)
                    balance = user.RedPackBalance;
                if (!TextUtils.isEmpty(balance))
                    moneyF = Float.parseFloat(balance) + money;
                recharMoney = decentFloat(moneyF);
                if (personalCenterPop != null) {
                    if (chargeTyp == 4) {
                        personalCenterPop.setMoney(recharMoney);
                        user.UserBalance = recharMoney;
                    } else {
                        personalCenterPop.setRed(recharMoney);
                        user.RedPackBalance = recharMoney;
                    }
                }
            } else {
                toastSuccess();
                FindBoomApplication.getInstance().playMoneySound();
                if (user != null)
                    balance = user.UserBalance;
                if (!TextUtils.isEmpty(balance))
                    moneyF = Float.parseFloat(balance) + goldAmout;
                recharMoney = decentFloat(moneyF);
                user.UserBalance = recharMoney;
            }

            BoomDBManager.getInstance().setUserData(user);
        } else {
            recharMoney = "";
            money = 0.00f;
        }
        if (!AppPrefrence.getIsBack(this))
            startService(backIntent);
        addMsgListener();
        try {
            refreshUI(EMClient.getInstance().chatManager().getUnreadMsgsCount());
        } catch (Exception e) {
            e.printStackTrace();
            Tools.debug(e.toString());
        }

    }

    private void checkUpdate() {
        Map<String, String> params = new HashMap<>();
        PostTools.postData(context, CommonUntilities.UNTILS + "CheckUpdate", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                final Bean_Version beanVersioin = new Gson().fromJson(response, Bean_Version.class);
                if (beanVersioin.Success) {
                    int version = 0;
                    try {
                        version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                    } catch (PackageManager.NameNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    if (version < beanVersioin.Data.VisionName) {
                        new AlertDialog.Builder(context).setTitle("发现新版本").setMessage(beanVersioin.Data.VisionDesc).setCancelable(true)
                                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!TextUtils.isEmpty(beanVersioin.Data.DownUrl)) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            intent.setData(Uri.parse(beanVersioin.Data.DownUrl));
                                            startActivity(intent);
                                        }
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }
            }
        });
    }

    private void addMsgListener() {
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    public void removeMsgListener() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            EMMessage msg = list.get(0);
            if (!Constant.isRuning && (chatPop == null || !chatPop.isShowing()) && !TextUtils.equals(Constant.CHAT_USER, msg.getFrom())) //如果当前在聊天页面则不推送消息
            {
                notifier.onNewMesg(list);

                refreshUI(EMClient.getInstance().chatManager().getUnreadMsgsCount());
            } else refreshUI(EMClient.getInstance().chatManager().getUnreadMsgsCount());
            String title = "";
            if (msg != null) {
                try {
                    title = msg.getStringAttribute("name");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    title = "玩儿家" + new Random().nextInt();
                }
            }
            title += ": " + ((EMTextMessageBody) msg.getBody()).getMessage();
            txtMsg.setText(title);

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    txtMsgVis();
//                }
//            });

            notifier.setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
                @Override
                public String getDisplayedText(EMMessage message) {
                    String msg = "";
                    if (message != null) {
                        msg = ((EMTextMessageBody) message.getBody()).getMessage();
                    }
                    return msg;
                }

                @Override
                public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                    return null;
                }

                @Override
                public String getTitle(EMMessage message) {
                    String title = "";
                    if (message != null) {
                        try {
                            title = message.getStringAttribute("name");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            title = "玩儿家" + new Random().nextInt();
                        }
                    }
                    title += "发来消息";
                    return title;
                }

                @Override
                public int getSmallIcon(EMMessage message) {
                    return 0;
                }

                @Override
                public Intent getLaunchIntent(EMMessage message) {
                    String title = "";
                    if (message != null) {
                        try {
                            title = message.getStringAttribute("name");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            title = "玩儿家" + new Random().nextInt();
                        }
                    }
                    Tools.debug(title);
                    return new Intent(context, ChatActivity.class).putExtra("userId", message.getFrom()).putExtra("username", title);
                }
            });

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    private void refreshUI(final int count) {
        Tools.debug("message---" + count);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (count > 0) {
                    txtMsgCount.setVisibility(View.VISIBLE);
                    txtMsgCount.setText(count + "");
                } else txtMsgCount.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
//        if (!Tools.isForeground(this, "Home"))
//            stopService(backIntent);
        if (!AppPrefrence.getIsBack(this))
            stopService(backIntent);
    }

    @Override
    protected void onDestroy() {
        if (AppPrefrence.getIsPush(this))
            unregisterReceiver(mMessageReceiver);
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(myListener);
        stopService(backIntent);
        mMapView.onDestroy();
        if (geoCoder != null)
            geoCoder.destroy();
//        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        if (bitmap != null)
            bitmap.recycle();
        mCurrentMarker.recycle();
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MyReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "findboom.android.com.findboom.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private LatLng startLat, walkLat;

    public void registerMessageReceiver() {
        mMessageReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void getLoacation(BDLocation location) {
        if (location == null)
            return;
//        Tools.debug(location.toString());
        city = location.getCity();
        if (!TextUtils.isEmpty(city))
            city = city.substring(0, city.length() - 1);
        // 构造定位数据
        LatLng endLat = new LatLng(location.getLatitude(), location.getLongitude());
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        if (!isDrag && !isScan && !isPutBoom)
//            setLocation(locData);
            mBaiduMap.setMyLocationData(locData);

        if (startLat == null) {
            startLat = new LatLng(location.getLatitude(), location.getLongitude());
            getBoom(location);
        }
        if (walkLat == null) {
            walkLat = new LatLng(location.getLatitude(), location.getLongitude());
        }

        if (DistanceUtil.getDistance(startLat, endLat) > radius * 4 / 5) {
            getBoom(location);
            startLat = endLat;
        }
        if (mapBoomList != null && mapBoomList.size() > 0 && DistanceUtil.getDistance(walkLat, endLat) > 2) {
            //判断是否出发雷
            walkLat = endLat;
            dealBoomData(walkLat);
        }
    }

    private List<BitmapDescriptor> mCurrentList;
    private Marker currentMarker;

    private void setLocation(MyLocationData locData) {
        if (currentMarker != null) {
            currentMarker.remove();
        }
        if (mCurrentList == null) {
            mCurrentList = new ArrayList<>();
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.man_one);
            mCurrentList.add(bitmap);
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.man_two);
            mCurrentList.add(bitmap);
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.man_three);
            mCurrentList.add(bitmap);
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.man_four);
            mCurrentList.add(bitmap);
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.man_five);
            mCurrentList.add(bitmap);
        }


        mBaiduMap.setMyLocationData(locData);
        OverlayOptions options = new MarkerOptions()
                .position(new LatLng(locData.latitude, locData.longitude))  //设置marker的位置
                .icons((ArrayList<BitmapDescriptor>) mCurrentList)   //设置marker图标
                .zIndex(15)  //设置marker所在层级
                .draggable(false);  //设置手势拖拽
        currentMarker = (Marker) mBaiduMap.addOverlay(options);
    }

    private Handler mHandler;
    HandlerThread thread;
    private boolean isRuning = false;

    /**
     * 处理地图地雷数据,判断是否引爆
     *
     * @param endLat
     */
    private void dealBoomData(LatLng endLat) {
        Tools.debug("dealBoom run---------");
        if (thread == null) {

        }
        thread = new HandlerThread("MyHandlerThread");
        thread.start();//创建一个HandlerThread并启动它
        if (mHandler == null) {
            mHandler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
        }
        mHandler.post(mBackgroundRunnable);//将线程post到Handler中
    }


    Runnable mBackgroundRunnable = new Runnable() {

        @Override
        public void run() {

            if (isRuning) return;
            isRuning = true;
            for (int i = 0; i < mapBoomList.size(); i++) {
                Bean_MapBoom.MapBoom mapBoom = mapBoomList.get(i);
                if (!TextUtils.equals(AppPrefrence.getUserName(context), mapBoom.UserId) && DistanceUtil.getDistance(walkLat, new LatLng(mapBoom.Latitude, mapBoom.Longitude)) < mapBoom.BombRange) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boomBoom(mapBoom);
                }
            }
            Tools.debug("thread run---------");
            isRuning = false;
        }
    };

    synchronized private void boomBoom(final Bean_MapBoom.MapBoom mapBoom) {

        Map<String, String> params = new HashMap<>();
        String url = "";
        params.put("MineRecordId", mapBoom.MineRecordId);
//        if (mapBoom.MineType == 3)
//            url = CommonUntilities.MINE_URL + "BombRedPackMine";    //红包雷
//        if (mapBoom.MineType == 4)
//            url = CommonUntilities.MINE_URL + "BombGoldMine"; //寻宝雷
        if (mapBoom.MineType == 6 || mapBoom.MineType == 0 || mapBoom.MineType == 1 || mapBoom.MineType == 2)
            url = CommonUntilities.MINE_URL + "BombMine";
        PostTools.postData(context, url, params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                Bean_BoomBoom bean_boomBoom = new Gson().fromJson(response, Bean_BoomBoom.class);
                if (bean_boomBoom.Success) {
                    //根据类型做成功处理
                    removeBoom(mapBoom.MineRecordId);
                    if (!AppPrefrence.getIsBoom(context) && isForeground) {
                        FindBoomApplication.getInstance().playBoomSound();
                        vibrator.vibrate(300);
                    } else if (!isForeground)
                        vibrator.vibrate(300);
                    noDefense(bean_boomBoom.Data, mapBoom.MineType, mapBoom.MineRecordId);
                    if (defenseCount > 0) {
                        defenseBoom();
                        defenseCount -= 1;
                        if (defenseList.get(0).ArmType == 5) {
                            if (defenseList.get(0).Count > 0) {
                                defenseList.get(0).Count -= 1;
                            } else defenseList.get(1).Count -= 1;
                        } else {
                            if (defenseList.get(1).Count > 0) {
                                defenseList.get(1).Count -= 1;
                            } else defenseList.get(0).Count -= 1;
                        }
                        countData();
                    }
//                    else noDefense(bean_boomBoom.Data, mapBoom.MineType);
                }
            }
        });

    }

    private void noDefense(Bean_BoomBoom.BoomData boomData, int type, String id) {
        if (type == 2) {
            if (boomPic != null && boomPic.isShowing())
                return;
            if (boomPic == null)
                boomPic = new BoomPic(context);
//            boomPic.setText(boomData.PicTitle);
            boomPic.setPic(boomData.PicUrl);
            boomPic.showPop(txtArsenal);
            boomPic.setId(id);
            boomPic.setPopInterfacer(this, 35);
        } else if (type == 1) {
            if (boomText != null && boomText.isShowing())
                return;
            if (boomText == null)
                boomText = new BoomText(context);
            boomText.setText(boomData.Text);
            boomText.setId(id);
            boomText.showPop(txtArsenal);
            boomText.setPopInterfacer(this, 36);
        } else {
            if (boomPop != null && boomPop.isShowing())
                return;
            if (boomPop == null)
                boomPop = new BoomPop(context);
            boomPop.showPop(txtArsenal);
            boomPop.setPopInterfacer(Home.this, 18);
        }

    }

    private void defenseBoom() {
        int[] start_location = new int[2];
        imgDefense.getLocationInWindow(start_location);
//        if (boomDefensePop == null)
//            boomDefensePop = new BoomDefensePop(context);
//        boomDefensePop.setPopInterfacer(this, 32);
//        if (!boomDefensePop.isShowing()) {
//            boomDefensePop.setStartLoc(start_location);
//            boomDefensePop.showPop(imgDefense);
//        }

        setAnim(start_location);

    }

    private ViewGroup viewGroup;

    /**
     * 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE - 1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private void addViewToAnimLayout(final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
    }

    private void setAnim(final int[] start_location) {
        final ViewGroup viewGroup = createAnimLayout();
        final ImageView imgDefense = new ImageView(this);
        imgDefense.setLayoutParams(new LinearLayout.LayoutParams(Tools.dip2px(context, 80), Tools.dip2px(context, 80)));
        imgDefense.setBackgroundResource(R.mipmap.defense_boom);
        imgDefense.setVisibility(View.GONE);
        viewGroup.addView(imgDefense);
        imgDefense.setLeft(start_location[0]);
        imgDefense.setTop(start_location[1]);
        final int[] end_location = new int[]{(int) (Tools.getScreenWide(context) / 2), (int) (Tools.getScreenHeight(context) / 2)};// 这是用来存储动画结束位置的X、Y坐标

        // 计算位移
        int endX = start_location[0] - end_location[0] + Tools.dip2px(context, 10);// 动画位移的X坐标
        int endY = end_location[1] - start_location[1] + Tools.dip2px(context, 80);// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(start_location[0],
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                start_location[1], endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(500);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间

//        imgDefense.startAnimation(set);
        TranslateAnimation translateAnimation = new TranslateAnimation(start_location[0],
                endX, start_location[1],
                endY);
        translateAnimation.setDuration(800);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new LinearInterpolator());
        imgDefense.startAnimation(translateAnimation);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgDefense.setVisibility(View.GONE);
                imgDefense.clearAnimation();
                ExplosionField explosionField = new ExplosionField(context);
                explosionField.explode(imgDefense);
                explosionField.setOnExplosionListener(new ExplosionField.ExplosionListener() {
                    @Override
                    public void onEnd() {
                        imgDefense.clearAnimation();
                    }
                });
                viewGroup.removeView(imgDefense);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // 动画监听事件
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                imgDefense.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                imgDefense.setY(end_location[1] - Tools.dip2px(context, 40));
                imgDefense.setX(end_location[0] - Tools.dip2px(context, 40));
                imgDefense.clearAnimation();
                imgDefense.startAnimation(scaleAnimation);
            }
        });

    }

    private void removeBoom(final String mineRecordId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mapBoomList.size(); i++) {
                    if (TextUtils.equals(mineRecordId, mapBoomList.get(i).MineRecordId)) {
                        mapBoomList.remove(i);
                        break;
                    }
                }
            }
        }).start();
    }

    private void getBoom(BDLocation location) {
        Map<String, String> params = new HashMap<>();
        params.put("IsContainRedPack", "1");
        params.put("IsContainGold", "1");
        params.put("Longitude", location.getLongitude() + "");
        params.put("Latitude", location.getLatitude() + "");
        params.put("Range", radius + "");
        PostTools.postData(context, CommonUntilities.MINE_URL + "GetAreaMines", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                mapBoomList.clear();
                Bean_MapBoom bean_MapBoom = new Gson().fromJson(response, Bean_MapBoom.class);
                if (bean_MapBoom.Success && bean_MapBoom.Data != null && bean_MapBoom.Data.size() > 0) {
                    mapBoomList.addAll(bean_MapBoom.Data);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            clearRedMarker();
                            clearGoldMarker();
                            for (int i = 0; i < mapBoomList.size(); i++) {
                                Bean_MapBoom.MapBoom boom = mapBoomList.get(i);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", boom.MineRecordId);
                                bundle.putInt("type", boom.MineType);
                                bundle.putInt("boomRange", boom.BombRange);
                                bundle.putString("userId", boom.UserId);
                                if (boom.MineType == 4) {
                                    goldBooms.add(boom);
                                    if (chbMoney.isChecked())
                                        addGoldMarker(new LatLng(boom.Latitude, boom.Longitude), bundle);
                                }
                                if (boom.MineType == 3) {
                                    redBooms.add(boom);
                                    if (chbBoom.isChecked())
                                        addRedMarker(new LatLng(boom.Latitude, boom.Longitude), bundle);

                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dealBoomData(walkLat);
                                }
                            });

                        }
                    }).start();
                }

            }
        });
    }

    private void clearGoldMarker() {
        if (goldMarkers != null && goldMarkers.size() > 0) {
            for (int i = 0; i < goldMarkers.size(); i++) {
                goldMarkers.get(i).remove();
            }
            goldMarkers.clear();
        }
    }

    private void addGoldMarker(LatLng latLng, Bundle bundle) {
        if (goldMarkers == null)
            goldMarkers = new ArrayList<>();
        bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gold_boom);
        OverlayOptions options = new MarkerOptions()
                .position(latLng).animateType(MarkerOptions.MarkerAnimateType.grow)  //设置marker的位置
                .icon(bitmap).extraInfo(bundle)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(false);  //设置手势拖拽
        goldMarkers.add((Marker) mBaiduMap.addOverlay(options));
    }

    private void clearRedMarker() {
        if (redMarkers != null && redMarkers.size() > 0) {
            for (int i = 0; i < redMarkers.size(); i++) {
                redMarkers.get(i).remove();
            }
            redMarkers.clear();
        }
    }

    private void addRedMarker(LatLng latLng, Bundle bundle) {
        if (redMarkers == null)
            redMarkers = new ArrayList<>();
        bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_red_boom);
        OverlayOptions options = new MarkerOptions()
                .position(latLng).animateType(MarkerOptions.MarkerAnimateType.grow)  //设置marker的位置
                .icon(bitmap).extraInfo(bundle)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(false);  //设置手势拖拽
        redMarkers.add((Marker) mBaiduMap.addOverlay(options));
    }

    private GeoCoder geoCoder;

    @Override
    public void onMapClick(LatLng latLng) {
        if (isPutBoom) {
            longItude = latLng.longitude + "";
            latItude = latLng.latitude + "";
            initGeoCoder(latLng);
//            selectBoomPop(latLng);
            if (putBoomTypePop == null)
                putBoomTypePop = new PutBoomTypePop(context);
            putBoomTypePop.showPop(txtArsenal);
            putBoomTypePop.setPopInterfacer(this, 28);
        }
        if (isScan) {
//            scanBoom(latLng);
            useScan(latLng);
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        longItude = latLng.longitude + "";
        latItude = latLng.latitude + "";
        initGeoCoder(latLng);
        isPutBoom = true;
        if (boomCount > 0) {
            if (putBoomTypePop == null)
                putBoomTypePop = new PutBoomTypePop(context);
            putBoomTypePop.showPop(txtArsenal);
            putBoomTypePop.setPopInterfacer(this, 28);
        } else {
            if (notifyPop == null)
                notifyPop = new NotifyPop(context);
            notifyPop.setNotify(R.string.boom_empty);
            notifyPop.showPop(txtArsenal);
            notifyPop.invisiableChb();
        }

    }


    private void toastScan(boolean hasBoom) {
        Toast toast = new Toast(this);
        ImageView imageView = new ImageView(this);
//        imageView.setBackgroundResource(R.mipmap.scan_success);
        if (hasBoom)
            imageView.setBackgroundResource(R.mipmap.scan_success);
        else imageView.setBackgroundResource(R.mipmap.scan_fail);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private void useScan(LatLng location) {
        if (scanBoomPop == null)
            scanBoomPop = new ScanBoomPop(context);
        scanBoomPop.showPop(txtArsenal);
        scanBoomPop.setPopInterfacer(this, 33);
        Map<String, String> params = new HashMap<>();
        params.put("Longitude", location.longitude + "");
        params.put("Latitude", location.latitude + "");
        PostTools.postData(context, CommonUntilities.MINE_URL + "UseMineCleaner", params, new PostCallBack() {
            @Override
            public void onResponse(final String response) {
                super.onResponse(response);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (scanBoomPop != null)
                            scanBoomPop.dismiss();
                        if (TextUtils.isEmpty(response))
                            return;
                        isScan = false;
                        Bean_UseScan baseBean = new Gson().fromJson(response, Bean_UseScan.class);
                        if (baseBean != null && baseBean.Success) {
                            if (baseBean.Data > 0) {
                                toastScan(true);
                                for (int j = 0; j < boomList.size(); j++) {
                                    if (boomList.get(j).ArmType == 0) {
                                        boomList.get(j).Count += 1;
                                        break;
                                    }
                                }
                            } else {
                                toastScan(false);
                            }
                            if (scanList.get(0).ArmType == 3) {
                                if (scanList.get(0).Count > 0) {
                                    scanList.get(0).Count -= 1;
                                } else scanList.get(1).Count -= 1;
                            } else {
                                if (scanList.get(1).Count > 0) {
                                    scanList.get(1).Count -= 1;
                                } else scanList.get(0).Count -= 1;
                            }
                            countData();
                        } else {
                            Tools.toastMsgCenter(context, baseBean.Msg);
                        }

                    }
                }, 2500);
            }

        });
    }

    private View selectBoomView;

    /**
     * 创建infowindow,让用户选择要埋雷的类型
     *
     * @param latLng
     */
    private void selectBoomPop(LatLng latLng) {
        if (selectBoomView == null)
            selectBoomView = LayoutInflater.from(context).inflate(R.layout.map_select_boom_window, null);
        selectBoomView.findViewById(R.id.txt_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, PutCommenBoom.class).putExtra("config", configString), 0);
                mBaiduMap.hideInfoWindow();
                isPutBoom = false;
            }
        });

        selectBoomView.findViewById(R.id.txt_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.hideInfoWindow();
                startActivityForResult(new Intent(context, PutRedBoom.class).putExtra("config", configString), 1);
                isPutBoom = false;
            }
        });
        InfoWindow boomWindow = new InfoWindow(selectBoomView, latLng, -10);

        mBaiduMap.showInfoWindow(boomWindow);
    }

    private void initGeoCoder(LatLng latLng) {
        if (geoCoder == null)
            geoCoder = GeoCoder.newInstance();
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                provice = reverseGeoCodeResult.getAddressDetail().province;
                boomCity = reverseGeoCodeResult.getAddressDetail().city;
                street = reverseGeoCodeResult.getAddressDetail().street;
                area = reverseGeoCodeResult.getAddressDetail().district;
            }
        });
    }


    private String provice = "", boomCity = "", city = "", area = "", street = "", longItude = "", latItude = "", mineType = "", remark = "", text = "", picUrl = "", picTitle = "";

    /**
     * 放置普通雷
     */
    private void putCommentBoom() {
        Map<String, String> params = new HashMap<>();
        params.put("Provice", provice);
        params.put("City", boomCity);
        params.put("Area", area);
        params.put("Street", street);
        params.put("Longitude", longItude);
        params.put("Latitude", latItude);
        params.put("MineType", mineType);
        params.put("Remark", remark);
        params.put("Text", text);
        params.put("PicUrl", picUrl);
        params.put("PicTitle", picTitle);
        PostTools.postData(context, CommonUntilities.MINE_URL + "AddMine", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if (baseBean != null && baseBean.Success) {
                    Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                    if (user != null) {
                        boomCount -= 1;
                        if (boomList.get(0).ArmType == 1) {
                            if (boomList.get(0).Count > 0) {
                                boomList.get(0).Count -= 1;
                            } else if (boomList.get(1).Count > 0) {
                                boomList.get(1).Count -= 1;
                            }
                        } else {
                            if (boomList.get(1).Count > 0) {
                                boomList.get(1).Count -= 1;
                            } else if (boomList.get(0).Count > 0) {
                                boomList.get(0).Count -= 1;
                            }
                        }
                        if (boomCount <= 0) {
                            imgArsenal.setEnabled(false);
                            boomCount = 0;
                            txtArsenal.setVisibility(View.INVISIBLE);
                        }
                        txtArsenal.setText(boomCount + "");
                    }

                    addMarker(new LatLng(Double.parseDouble(latItude), Double.parseDouble(longItude)), 2);
                    addOverLay(new LatLng(Double.parseDouble(latItude), Double.parseDouble(longItude)));
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                Tools.debug(e.toString());
            }
        });
    }

    int boomRang = 50;

    private void addOverLay(LatLng latLng) {
        CircleOptions circle = new CircleOptions().center(latLng).fillColor(R.color.price_red).radius(boomRang).visible(true).zIndex(15);
        mBaiduMap.addOverlay(circle);
    }

    private BitmapDescriptor bitmap;

    private void addMarker(LatLng latLng, int type) {
        if (type == 3)
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_red_boom);
        else if (type == 4)
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_gold_boom);
        else
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_map_boom);
        OverlayOptions options = new MarkerOptions()
                .position(latLng)  //设置marker的位置
                .icon(bitmap).animateType(MarkerOptions.MarkerAnimateType.grow)   //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(false);  //设置手势拖拽
        mBaiduMap.addOverlay(options);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = marker.getExtraInfo();
        if (bundle != null && bundle.getBoolean("isget", false)) {
            Tools.toastMsgCenter(context, "不能重复领取~");
            return false;
        }
        if (bundle != null && bundle.getInt("type") == 4) {
            if (TextUtils.equals(bundle.getString("userId"), AppPrefrence.getUserName(context))) {
                Tools.toastMsgCenter(context, "不能领取自己的寻宝雷");
                return false;
            }
            if (DistanceUtil.getDistance(marker.getPosition(), walkLat) > goldGetRange) {
                //大于可领取距离,提示不能领取
                Tools.toastMsgCenter(context, "再靠近一点点,就让你领取~");
                return false;
            }
            String id = bundle.getString("id");
            int type = bundle.getInt("type");
            if (!TextUtils.isEmpty(id)) {
                boomGold(id, marker);
            }
        }
        if (bundle != null && bundle.getInt("type") == 3) {
            if (TextUtils.equals(bundle.getString("userId"), AppPrefrence.getUserName(context))) {
                Tools.toastMsgCenter(context, "不能领取自己的红包雷");
                return false;
            }
            if (DistanceUtil.getDistance(marker.getPosition(), walkLat) > redGetRange) {
                //大于可领取距离,提示不能领取
                Tools.toastMsgCenter(context, "再靠近一点点,就让你领取~");
                return false;
            }
            String id = bundle.getString("id");
            int type = bundle.getInt("type");
            if (!TextUtils.isEmpty(id)) {
                boomRed(id, marker);
            }
        }
        return false;
    }

    /**
     * 触发寻宝雷
     *
     * @param id
     */
    private void boomGold(final String id, final Marker marker) {
        Map<String, String> params = new HashMap<>();
        params.put("MineRecordId", id);
        PostTools.postData(context, CommonUntilities.MINE_URL + "BombGoldMine", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "网络错误", "请稍后重试").showPop();
                    return;
                }
                Bean_GoldBoom goldBoom = new Gson().fromJson(response, Bean_GoldBoom.class);
                if (goldBoom != null && goldBoom.Success) {
//                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, goldBoom.Data.PicTitle, "恭喜!获得宝藏").showPop();
                    if (boomGold != null && boomGold.isShowing())
                        return;
                    if (boomGold == null)
                        boomGold = new BoomGold(context);
                    boomGold.setText(goldBoom.Data.PicTitle);
                    boomGold.setPic(goldBoom.Data.PicUrl);
                    boomGold.setId(id);
                    boomGold.showPop(txtArsenal);
                    Bundle bundle = marker.getExtraInfo();
                    bundle.putBoolean("isget", true);
                    marker.setExtraInfo(bundle);
                } else {
                    Tools.toastMsgCenter(context, goldBoom.Msg);
                }
            }
        });
    }

    /**
     * 触发红包雷
     *
     * @param id
     */
    private void boomRed(final String id, final Marker marker) {
        Map<String, String> params = new HashMap<>();
        params.put("MineRecordId", id);
        PostTools.postData(context, CommonUntilities.MINE_URL + "BombRedPackMine", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "网络错误", "请稍后重试").showPop();
                    return;
                }
                Bean_RedBoom redBoom = new Gson().fromJson(response, Bean_RedBoom.class);
                if (redBoom != null && redBoom.Success) {
                    Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                    String money = user.RedPackBalance;
                    float moneyF = Float.parseFloat(money);
                    moneyF += redBoom.Data.Amount;
                    money = decentFloat(moneyF);
                    user.RedPackBalance = money;
                    BoomDBManager.getInstance().setUserData(user);
                    if (openRedPop == null)
                        openRedPop = new OpenRedPop(context);
                    openRedPop.setMoney(redBoom.Data.Amount + "");
                    openRedPop.setId(id);
                    openRedPop.showPop(txtArsenal);
                    Bundle bundle = marker.getExtraInfo();
                    bundle.putBoolean("isget", true);
                    marker.setExtraInfo(bundle);
                } else {
                    Tools.toastMsgCenter(context, redBoom.Msg);
                }
            }
        });
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        if (mapStatus.bound.contains(walkLat)) {
            invisLocation();
        } else visLocation();
    }

    private void visLocation() {
        isDrag = true;
        imgLocation.setVisibility(View.VISIBLE);
        ViewTreeObserver vto = imgLocation.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                imgLocation.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                rotateAnim(imgLocation.getWidth(), imgLocation.getHeight());
            }
        });
    }

    private void invisLocation() {
        isDrag = false;
        isPutBoom = false;
        isScan = false;
        imgLocation.setVisibility(View.GONE);
        imgLocation.clearAnimation();
    }

    /**
     * 实现动画旋转效果
     */
    private void rotateAnim(float w, float y) {
        //图片旋转中心坐标
        if (imgLocation.getAnimation() != null) {
            imgLocation.getAnimation().cancel();
        }
        final float centerX = w / 2.0f;
        final float centerY = y / 2.0f;
        RotateAnimation rotation;
        rotation = new RotateAnimation(0, -360, centerX, centerY, 310.0f,
                false);
        rotation.setDuration(1000);//动画持续时间
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());//设置加速度
        rotation.setRepeatCount(1);//重复次数
//        rotation.setRepeatMode(Animation.RESTART);//重复动画
        imgLocation.startAnimation(rotation);

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!Tools.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                Tools.debug("esae" + showMsg);
            }
        }
    }

    private void dissAllPop() {
        if (settingPop != null && settingPop.isShowing())
            settingPop.dismiss();
        if (shopPop != null && shopPop.isShowing())
            shopPop.dismiss();
        if (defensePop != null && defensePop.isShowing())
            defensePop.dismiss();
        if (personalCenterPop != null && personalCenterPop.isShowing())
            personalCenterPop.isShowing();
        if (scanPop != null && scanPop.isShowing())
            scanPop.isShowing();
        if (arsenalPop != null && arsenalPop.isShowing())
            arsenalPop.isShowing();
        if (friendListPop != null && friendListPop.isShowing())
            friendListPop.isShowing();
        if (recordPop != null && recordPop.isShowing())
            recordPop.isShowing();
        if (advicePop != null && advicePop.isShowing())
            advicePop.isShowing();
        if (reportPop != null && reportPop.isShowing())
            reportPop.isShowing();
        if (getRecordPop != null && getRecordPop.isShowing())
            getRecordPop.isShowing();
        if (recordListPop != null && recordListPop.isShowing())
            recordListPop.isShowing();
//        if (createPayPwd != null && createPayPwd.isShowing())
//            createPayPwd.isShowing();
//        if (changePayPwd != null && changePayPwd.isShowing())
//            changePayPwd.isShowing();
//        if (confirmPwdPop != null && confirmPwdPop.isShowing())
//            confirmPwdPop.isShowing();
        if (shopBuyPop != null && shopBuyPop.isShowing())
            shopBuyPop.isShowing();
        if (selectPayPop != null && selectPayPop.isShowing())
            selectPayPop.isShowing();
//        if (personalInfo != null && personalInfo.isShowing())
//            personalInfo.isShowing();
        if (boomPop != null && boomPop.isShowing())
            boomPop.isShowing();
        if (selectPayTypeAllPop != null && selectPayTypeAllPop.isShowing())
            selectPayTypeAllPop.isShowing();
        if (bandPhonePop != null && bandPhonePop.isShowing())
            bandPhonePop.isShowing();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (resultCode == RESULT_OK && requestCode == 0) {
            //普通雷/图片雷
            Bundle bundle = data.getBundleExtra("data");
            mineType = bundle.getString("type", "6");
            text = bundle.getString("text", "");
            picTitle = bundle.getString("imgInfo", "");
            picUrl = bundle.getString("imgUrl", "");
            boomRang = bundle.getInt("rang", 50);
            putCommentBoom();
        }
        if (resultCode == RESULT_OK && requestCode == 1) {
            Bundle bundle = data.getBundleExtra("data");
            mineType = bundle.getString("type", "3");
            text = bundle.getString("remark", "");
            redCount = bundle.getString("count", "");
            redAmount = bundle.getString("money", "");
            boomRang = bundle.getInt("rang", 50);
            pwdString = bundle.getString("pwd", "");
            putRedBoom();
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            if (data.getBooleanExtra("success", false)) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", AppPrefrence.getUserName(this));
                addGoldMarker(new LatLng(Double.parseDouble(latItude), Double.parseDouble(longItude)), bundle);
                boomCount -= 1;
                if (boomList.get(0).ArmType == 1) {
                    if (boomList.get(0).Count > 0) {
                        boomList.get(0).Count -= 1;
                    } else if (boomList.get(1).Count > 0) {
                        boomList.get(1).Count -= 1;
                    }
                } else {
                    if (boomList.get(1).Count > 0) {
                        boomList.get(1).Count -= 1;
                    } else if (boomList.get(0).Count > 0) {
                        boomList.get(0).Count -= 1;
                    }
                }
                if (boomCount <= 0) {
                    imgArsenal.setEnabled(false);
                    boomCount = 0;
                    txtArsenal.setVisibility(View.INVISIBLE);
                }
                txtArsenal.setText(boomCount + "");
            }
        }
    }

    private String redCount, redAmount, pwdString;

    /**
     * 放置红包雷
     */
    private void putRedBoom() {
        Map<String, String> params = new HashMap<>();
        params.put("Provice", provice);
        params.put("City", boomCity);
        params.put("Area", area);
        params.put("Street", street);
        params.put("Longitude", longItude);
        params.put("Latitude", latItude);
        params.put("MineType", mineType);
        params.put("Remark", remark);
        params.put("RedPackText", text);
        params.put("Count", redCount);
        params.put("TotalAmount", redAmount);
        params.put("PayPassWord", Tools.get32MD5StrWithOutKey(pwdString));
        PostTools.postData(context, CommonUntilities.MINE_URL + "AddRedPackMine", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if (baseBean != null && baseBean.Success) {
                    Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                    if (user != null) {
                        boomCount -= 1;
                        if (boomList.get(0).ArmType == 1) {
                            if (boomList.get(0).Count > 0) {
                                boomList.get(0).Count -= 1;
                            } else if (boomList.get(1).Count > 0) {
                                boomList.get(1).Count -= 1;
                            }
                        } else {
                            if (boomList.get(1).Count > 0) {
                                boomList.get(1).Count -= 1;
                            } else if (boomList.get(0).Count > 0) {
                                boomList.get(0).Count -= 1;
                            }
                        }
                        if (boomCount <= 0) {
                            imgArsenal.setEnabled(false);
                            boomCount = 0;
                            txtArsenal.setVisibility(View.INVISIBLE);
                        }
                        txtArsenal.setText(boomCount + "");
                    }
                    float money = Float.parseFloat(user.RedPackBalance);
                    money -= Float.parseFloat(redAmount);
                    user.RedPackBalance = decentFloat(money);
                    BoomDBManager.getInstance().updateUserData(user);
                    addMarker(new LatLng(Double.parseDouble(latItude), Double.parseDouble(longItude)), 3);
//                    addOverLay(new LatLng(Double.parseDouble(latItude), Double.parseDouble(longItude)));
                } else
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, baseBean.Msg, "").showPop();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                Tools.debug(e.toString());
            }
        });
    }

    /***
     * 好友变化listener
     */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(final String username) {
            // 保存增加的联系人
            Map<String, EaseUser> localUsers = FindBoomApplication.getInstance().getContactList();
            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
            EaseUser user = new EaseUser(username);
            // 添加好友时可能会回调added方法两次
            if (!localUsers.containsKey(username)) {
                userDao.saveContact(user);
            }
            toAddUsers.put(username, user);
            localUsers.putAll(toAddUsers);

        }

        @Override
        public void onContactDeleted(final String username) {
            // 被删除
            Map<String, EaseUser> localUsers = FindBoomApplication.getInstance().getContactList();
            localUsers.remove(username);
            userDao.deleteContact(username);
            inviteMessgeDao.deleteMessage(username);
        }

        @Override
        public void onContactInvited(final String username, String reason) {
        }

        @Override
        public void onContactAgreed(final String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }

    }


    private void showShare(String title, String url, String text, String imgurl) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://api.open.qq.com/tfs/show_img.php?appid=1105792674&uuid=app1105792674_40_40%7C1048576%7C1477965705.1411");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(this);
    }
}
