package findboom.android.com.findboom;/**
 * Created by Administrator on 2016/8/8.
 */

import android.*;
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
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
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
import com.hyphenate.chat.EMClient;
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
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import findboom.android.com.findboom.activity.FriendMessage;
import findboom.android.com.findboom.activity.LoginActivity;
import findboom.android.com.findboom.activity.PutCommenBoom;
import findboom.android.com.findboom.activity.PutRedBoom;
import findboom.android.com.findboom.activity.SystemMessage;
import findboom.android.com.findboom.alipay.AliPayHelper;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.BaseBean;
import findboom.android.com.findboom.bean.Bean_BoomBoom;
import findboom.android.com.findboom.bean.Bean_MapBoom;
import findboom.android.com.findboom.bean.Bean_RedBoom;
import findboom.android.com.findboom.bean.Bean_UserArm;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.bean.Bean_WXpay;
import findboom.android.com.findboom.chat.db.EaseUser;
import findboom.android.com.findboom.chat.db.InviteMessgeDao;
import findboom.android.com.findboom.chat.db.UserDao;
import findboom.android.com.findboom.dailog.AddFriendPop;
import findboom.android.com.findboom.dailog.AdvicePop;
import findboom.android.com.findboom.dailog.ArsenalPop;
import findboom.android.com.findboom.dailog.BandPhonePop;
import findboom.android.com.findboom.dailog.BoomPop;
import findboom.android.com.findboom.dailog.ChangePayPwdPop;
import findboom.android.com.findboom.dailog.ConfrimPwdPop;
import findboom.android.com.findboom.dailog.ConvertRedPop;
import findboom.android.com.findboom.dailog.CreatePayPwdPop;
import findboom.android.com.findboom.dailog.DefensePop;
import findboom.android.com.findboom.dailog.FriendListPop;
import findboom.android.com.findboom.dailog.GetRecordPop;
import findboom.android.com.findboom.dailog.MessageDialog;
import findboom.android.com.findboom.dailog.NewShopPop;
import findboom.android.com.findboom.dailog.NotifyPop;
import findboom.android.com.findboom.dailog.PersonalCenterPop;
import findboom.android.com.findboom.dailog.PersonalInfo;
import findboom.android.com.findboom.dailog.PickerDialog;
import findboom.android.com.findboom.dailog.PostResultPop;
import findboom.android.com.findboom.dailog.PutBoomTypePop;
import findboom.android.com.findboom.dailog.RecordListPop;
import findboom.android.com.findboom.dailog.RecordPop;
import findboom.android.com.findboom.dailog.ReportPop;
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
import findboom.android.com.findboom.service.BackgroundService;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.expandableselector.ExpandableItem;
import findboom.android.com.findboom.widget.expandableselector.ExpandableSelector;
import findboom.android.com.findboom.widget.expandableselector.ExpandableSelectorListener;
import findboom.android.com.findboom.widget.expandableselector.OnExpandableItemClickListener;
import findboom.android.com.findboom.wxpay.WxPayHelper;
import okhttp3.Call;

/**
 * author:${白曌勇} on 2016/8/8
 * TODO:
 */
public class Home extends BaseActivity implements PopInterfacer, LocationListener, BaiduMap.OnMapClickListener, BaiduMap.OnMapLongClickListener, BaiduMap.OnMarkerClickListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ImageView imgDefense, imgScan, imgRecord, imgArsenal, imgMsg;
    private TextView txtDefense, txtScan, txtRecord, txtArsenal;
    private CheckBox chbMoney, chbBoom;

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
    private CreatePayPwdPop createPayPwd;
    private ChangePayPwdPop changePayPwd;
    private ConfrimPwdPop confirmPwdPop;
    private ShopBuyPop shopBuyPop;
    private SelectPayTypePop selectPayPop;
    private PersonalInfo personalInfo;
    private BoomPop boomPop;
    private SelectPayTypeAllPop selectPayTypeAllPop;
    private BandPhonePop bandPhonePop;
    private MessageDialog messageDialog;
    private ConvertRedPop convertRedPop;
    private AddFriendPop addFriendPop;
    private NotifyPop notifyPop;
    private PutBoomTypePop putBoomTypePop;

    private List<Bean_UserArm.UserArm> defenseList;
    private List<Bean_UserArm.UserArm> boomList;
    private List<Bean_UserArm.UserArm> scanList;

    public static boolean isForeground;
    private boolean isPutBoom = false; //标识现在是否有放雷的动作
    private boolean isScan = false; //标识现在是否使用扫雷器

    //百度地图定位相关
    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    private int radius = 1000;//获取一千米范围内的雷

    private List<Bean_MapBoom.MapBoom> mapBoomList;

    //购买商品相关
    private String goodsId, goodsCount;

    private int boomCount = 0, scanCount = 0, defenseCount = 0;
    private PickerDialog ageDialog, workDialog;
    private List<String> workStrings = new ArrayList<>();
    private List<String> ageStrings = new ArrayList<>();

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    private Intent backIntent;
    private Vibrator vibrator;

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
        JPushInterface.init(getApplicationContext());
        JPushInterface.setAlias(getApplicationContext(), AppPrefrence.getUserName(this), new TagAliasCallback() {
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
        registerMessageReceiver();
        new AliPayHelper(this, payHandler).debugSign("123456");

        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        //注册联系人变动监听
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
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
                        addMarker(new LatLng(mapBoom.Latitude, mapBoom.Longitude));
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
                txtScan.setVisibility(View.GONE);
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
                txtDefense.setVisibility(View.GONE);
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
                txtArsenal.setVisibility(View.GONE);
            }

        }
    }

    BitmapDescriptor mCurrentMarker;
    private ExpandableSelector expandableSelector;

    private void initView() {
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        expandableSelector = (ExpandableSelector) findViewById(R.id.img_expand);
        expandableSelector.setVisibility(View.INVISIBLE);
        List<ExpandableItem> expandableItems = new ArrayList<>();
        expandableItems.add(new ExpandableItem(R.mipmap.friend_msg));
        expandableItems.add(new ExpandableItem(R.mipmap.system_msg));
        expandableSelector.showExpandableItems(expandableItems);
        expandableSelector.setOnExpandableItemClickListener(new OnExpandableItemClickListener() {
            @Override
            public void onExpandableItemClickListener(int index, View view) {
                if (index == 1) {
                    //系统消息
                    startActivity(new Intent(context, SystemMessage.class));
                } else {
                    //好友消息
                    startActivityForResult(new Intent(context, FriendMessage.class), 10);
                }
                expandableSelector.collapse();
            }
        });
        expandableSelector.setExpandableSelectorListener(new ExpandableSelectorListener() {
            @Override
            public void onCollapse() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onCollapsed() {
                expandableSelector.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onExpanded() {
                expandableSelector.setVisibility(View.VISIBLE);
            }
        });
        mapBoomList = new ArrayList<>();
        boomList = new ArrayList<>();
        defenseList = new ArrayList<>();
        scanList = new ArrayList<>();
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate status = MapStatusUpdateFactory.zoomTo(20f);
        mBaiduMap.setMapStatus(status);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）

        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.man_one);

        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(config);

        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMapLongClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);

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

        chbBoom = (CheckBox) findViewById(R.id.chb_set_treasure);
        chbBoom.setChecked(AppPrefrence.getIsBoomShow(context));
        chbBoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppPrefrence.setIsBoomShow(context, isChecked);
            }
        });

        chbMoney = (CheckBox) findViewById(R.id.chb_set_red_packet);
        chbMoney.setChecked(AppPrefrence.getIsRedShow(context));
        chbMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppPrefrence.setIsRedShow(context, isChecked);
            }
        });
    }


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
                personalCenterPop.setCicy(city);
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
                if (!expandableSelector.isExpanded()) {
                    expandableSelector.setVisibility(View.VISIBLE);
                    expandableSelector.expand();
                } else {
                    expandableSelector.setVisibility(View.INVISIBLE);
                    expandableSelector.collapse();
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
                createPayPwd = null;
                break;
            case 13:
                changePayPwd = null;
                break;
            case 14:
                confirmPwdPop = null;
                break;
            case 15:
                shopBuyPop = null;
                break;
            case 16:
                selectPayPop = null;
                break;
            case 17:
                personalInfo = null;
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
                confirmPwdPop = null;
                break;
            case 26:
                addFriendPop = null;
                break;
            case 27:
                notifyPop = null;
                break;
            case 28:
                putBoomTypePop = null;
                break;
        }
    }

    private int chargeTyp = -1;
    private float money = 0.00f;
    private int armType = 0;
    private int goldAmout = 0;//金币数量

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
                    showShare("需要你的支援", CommonUntilities.SHARE_REGISTER, "我已经被炸的体无完肤,速速支援寡人", "");
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
                    shopBuyPop.setPrice(money);
                    armType = bundle.getInt("armType");
                    shopBuyPop.setGoodPic(armType);
                }
                if (bundle.getInt("type", -1) == 1) {//金币直接购买
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
                if (bundle != null && bundle.getInt("type", -1) == 2) {
                    //2创建,3修改
                    if (createPayPwd == null)
                        createPayPwd = new CreatePayPwdPop(context);
                    createPayPwd.showPop(imgArsenal);
                    createPayPwd.setPopInterfacer(this, 12);
                }
                if (bundle != null && bundle.getInt("type", -1) == 3) {
                    if (TextUtils.isEmpty(AppPrefrence.getUserPhone(context))) {
                        //提示绑定手机号码
                        dissAllPop();
                        if (bandPhonePop == null)
                            bandPhonePop = new BandPhonePop(context);
                        bandPhonePop.showPop(txtArsenal);
                        bandPhonePop.setPopInterfacer(this, 22);

                    } else {
                        if (changePayPwd == null)
                            changePayPwd = new ChangePayPwdPop(context);
                        changePayPwd.showPop(imgArsenal);
                        changePayPwd.setPopInterfacer(this, 13);
                    }

                }
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
                break;
            case 4:
                //使用扫雷器
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
                    showShare("痛不欲生", CommonUntilities.SHARE_RECORD, "我已经被炸得怀疑人生,闪开,我想静静", "");
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
                    redBuyGold(bundle.getString("pwd"));
                else
                    accountBuy(bundle.getString("pwd"));
                break;
            case 15:
                //余额支付,确认支付密码
                if (bundle == null)
                    return;
                goodsCount = bundle.getInt("count") + "";
                money = bundle.getFloat("money", 0.00f);
                if (confirmPwdPop == null)
                    confirmPwdPop = new ConfrimPwdPop(context);
                confirmPwdPop.showPop(txtArsenal);
                confirmPwdPop.setMoney(money);
                confirmPwdPop.setPopInterfacer(this, 14);
                isBuyGold = false;
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
                    if (confirmPwdPop == null)
                        confirmPwdPop = new ConfrimPwdPop(context);
                    confirmPwdPop.showPop(txtArsenal);
                    confirmPwdPop.setMoney(money);
                    confirmPwdPop.setPopInterfacer(this, 14);
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
                if (confirmPwdPop == null)
                    confirmPwdPop = new ConfrimPwdPop(context);
                confirmPwdPop.showPop(txtArsenal);
                confirmPwdPop.setPopInterfacer(this, 25);
                break;
            case 25:
                if (bundle == null)
                    return;
                convertRed(bundle.getString("pwd"));
                confirmPwdPop.dismiss();
                break;
            case 28:
                if (bundle == null)
                    return;
                if (bundle.getInt("type", -1) == 0) {
                    startActivityForResult(new Intent(context, PutCommenBoom.class).putExtra("config", configString), 0);
                    isPutBoom = false;
                } else if (bundle.getInt("type", -1) == 1) {
                    startActivityForResult(new Intent(context, PutRedBoom.class).putExtra("config", configString), 1);
                    isPutBoom = false;
                }
                break;
        }
    }

    String alipayName = "", alipayNo = "";

    private void convertRed(String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("AlipayName", alipayName);
        params.put("AlipayAccount", alipayNo);
        params.put("Amount", money + "");
        params.put("PayPassWord", Tools.get32MD5StrWithOutKey(pwd));
        PostTools.postData(context, CommonUntilities.WITH_URL + "ApplyWithdraw", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("convertRed" + response);
                if (TextUtils.isEmpty(response)) {
                    Tools.toastMsg(context, "网络错误,请检查后重试");
                    return;
                }
                Bean_UserArm baseBean = new Gson().fromJson(response, Bean_UserArm.class);
                if (baseBean != null && baseBean.Success) {
                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, baseBean.Msg, "").showPop();
                    String balance = "";
                    recharMoney = "";
                    float moneyF = 0.00f;
                    Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                    if (user != null)
                        balance = user.RedPackBalance;
                    if (!TextUtils.isEmpty(balance))
                        moneyF = Float.parseFloat(balance) - money;
                    recharMoney = decentFloat(moneyF);
                    if (personalCenterPop != null)
                        personalCenterPop.setRed(recharMoney);
                    user.RedPackBalance = recharMoney;
                    BoomDBManager.getInstance().setUserData(user);
                    if (convertRedPop != null && convertRedPop.isShowing())
                        convertRedPop.dismiss();
                } else
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, baseBean.Msg, "").showPop();
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
                if (TextUtils.isEmpty(AppPrefrence.getUserPhone(context))) {
                    //忘记密码-->绑定手机号码
                    dissAllPop();
                    if (bandPhonePop == null)
                        bandPhonePop = new BandPhonePop(context);
                    bandPhonePop.showPop(txtArsenal);
                    bandPhonePop.setPopInterfacer(this, 22);
                } else {
                    if (changePayPwd == null)
                        changePayPwd = new ChangePayPwdPop(context);
                    changePayPwd.showPop(imgArsenal);
                    changePayPwd.setPopInterfacer(this, 13);
                }
                break;
            case 17:
                break;
            case 25:
                //forget pwd
                if (TextUtils.isEmpty(AppPrefrence.getUserPhone(context))) {
                    //忘记密码-->绑定手机号码
                    dissAllPop();
                    if (bandPhonePop == null)
                        bandPhonePop = new BandPhonePop(context);
                    bandPhonePop.showPop(txtArsenal);
                    bandPhonePop.setPopInterfacer(this, 22);
                } else {
                    if (changePayPwd == null)
                        changePayPwd = new ChangePayPwdPop(context);
                    changePayPwd.showPop(imgArsenal);
                    changePayPwd.setPopInterfacer(this, 13);
                }
                break;
        }
    }

    /**
     * 红包购买金币
     *
     * @param pwd
     */
    private void redBuyGold(String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("GoldId", goodsId);
        params.put("PayPassWord", Tools.get32MD5StrWithOutKey(pwd));
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
                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, baseBean.Msg, "").showPop();
                    String balance = "";
                    recharMoney = "";
                    float moneyF = 0.00f;
                    Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
                    if (user != null)
                        balance = user.UserBalance;
                    if (!TextUtils.isEmpty(balance))
                        moneyF = Float.parseFloat(balance) + goldAmout;
                    recharMoney = decentFloat(moneyF);
                    user.UserBalance = recharMoney;
                    BoomDBManager.getInstance().setUserData(user);
                } else
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, baseBean.Msg, "").showPop();
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
        params.put("GolbId", goodsId);
        params.put("PayMentType", type + "");
        PostTools.postData(context, CommonUntilities.RECHARGE_URL + "RechargeGold", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response)) {
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "网络错误", "检查网络后重试").showPop();
                    return;
                }

                if (type == 1) {
                    //支付宝支付
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object != null && object.getBoolean("Success")) {
                            String orderInfo = object.getString("Data");
                            if (TextUtils.isEmpty(orderInfo)) {
                                new PostResultPop(context, txtArsenal, R.drawable.icon_error, "支付失败,请重试", "").showPop();
                                return;
                            }

                            new AliPayHelper(Home.this, payHandler).pay(orderInfo);
                        }

                    } catch (Exception e) {
                        new PostResultPop(context, txtArsenal, R.drawable.icon_error, "支付失败,请重试", "").showPop();
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
                        new PostResultPop(context, txtArsenal, R.drawable.icon_error, "支付失败,请重试", "").showPop();
                }
            }
        });
    }

    String recharMoney = "";
    Handler payHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
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
                money = 0f;
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
            }
        });
    }

    private void saveUserInfo(Bundle bundle) {
        Map<String, String> params = new HashMap<>();
        String name = bundle.getString("name");
        String pro = bundle.getString("pro");
        String age = bundle.getString("age");
        int ageInt = TextUtils.isEmpty(age) ? 0 : Integer.parseInt(age);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        params.put("NickName", TextUtils.isEmpty(name) ? "" : name);
        params.put("City", city);
        params.put("Province", provice);
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
                }
            }
        });
    }

    /**
     * 余额支付
     *
     * @param pwd
     */
    private void accountBuy(String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("ArmInfoId", goodsId);
        params.put("Count", goodsCount);
        params.put("PayPassWord", Tools.get32MD5StrWithOutKey(pwd));
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
                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, "购买成功", "").showPop();
                    confirmPwdPop.dismiss();
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
                                boomList.add(baseBean.Data.get(i));
                                break;
                            case 2:
                                scanList.add(baseBean.Data.get(i));

                                break;
                            case 3:
                                scanList.add(baseBean.Data.get(i));
                                break;
                            case 4:
                                defenseList.add(baseBean.Data.get(i));

                                break;
                            case 5:
                                defenseList.add(baseBean.Data.get(i));
                                break;
                        }
                    }
                    countData();
                } else
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, baseBean.Msg, "").showPop();
            }
        });
    }

    private void countData() {
        boomCount = 0;
        for (int i = 0; i < boomList.size(); i++) {
            boomCount += boomList.get(i).Count;
        }
        if (boomCount > 0) {
            imgArsenal.setEnabled(true);
            txtArsenal.setVisibility(View.VISIBLE);
        }

        txtArsenal.setText(boomCount + "");

        defenseCount = 0;
        for (int i = 0; i < defenseList.size(); i++) {
            defenseCount += defenseList.get(i).Count;
        }
        if (defenseCount > 0) {
            imgDefense.setEnabled(true);
            txtDefense.setVisibility(View.VISIBLE);
        }
        txtDefense.setText(defenseCount + "");

        scanCount = 0;
        for (int i = 0; i < scanList.size(); i++) {
            scanCount += scanList.get(i).Count;
        }
        if (scanCount > 0) {
            imgScan.setEnabled(true);
            txtScan.setVisibility(View.VISIBLE);
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
//        if (!TextUtils.isEmpty(photoPath)) {
//            if (personalCenterPop != null && personalCenterPop.isShowing())
//                personalCenterPop.setPhoto(photoPath);
//            if (personalInfo != null && personalInfo.isShowing())
//                personalInfo.setPhoto(photoPath);
//        }
        if (!TextUtils.isEmpty(photoPath)) {
            if (personalCenterPop != null && personalCenterPop.isShowing())
                personalCenterPop.setPhoto(photoPath);
        }
    }

    private void logOut() {
        AppPrefrence.setIsLogin(context, false);
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
            money = 0f;
        }
        if (!AppPrefrence.getIsBack(this))
            startService(backIntent);
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        if (!AppPrefrence.getIsBack(this))
            stopService(backIntent);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        stopService(backIntent);
        mMapView.onDestroy();
        if (geoCoder != null)
            geoCoder.destroy();
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private LatLng startLat, walkLat;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
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
        if (!isPutBoom && (endLat == null || walkLat == null || DistanceUtil.getDistance(startLat, endLat) > 5))
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

    /**
     * 处理地图地雷数据,判断是否引爆
     *
     * @param endLat
     */
    synchronized private void dealBoomData(LatLng endLat) {
        for (int i = 0; i < mapBoomList.size(); i++) {
            Bean_MapBoom.MapBoom mapBoom = mapBoomList.get(i);
            if (!TextUtils.equals(AppPrefrence.getUserName(context), mapBoom.UserId) && DistanceUtil.getDistance(endLat, new LatLng(mapBoom.Latitude, mapBoom.Longitude)) < mapBoom.BombRange) {
                boomBoom(mapBoom);
            }
        }
    }

    private void boomBoom(final Bean_MapBoom.MapBoom mapBoom) {
        Map<String, String> params = new HashMap<>();
        String url = "";
        params.put("MineRecordId", mapBoom.MineRecordId);
        if (mapBoom.MineType == 3)
            url = CommonUntilities.MINE_URL + "BombRedPackMine";    //红包雷
        if (mapBoom.MineType == 4)
            url = CommonUntilities.MINE_URL + "BombGoldMine"; //寻宝雷
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
                    FindBoomApplication.getInstance().playBoomSound();
                    if (boomPop == null)
                        boomPop = new BoomPop(context);
                    boomPop.showPop(txtArsenal);
                    boomPop.setPopInterfacer(Home.this, 18);
                    if (!AppPrefrence.getIsBoom(context))
                        vibrator.vibrate(300);
                }
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
        params.put("IsContainRedPack", chbMoney.isChecked() ? "1" : "0");
        params.put("IsContainGold", chbBoom.isChecked() ? "1" : "0");
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
                    if (chbMoney.isChecked() || chbBoom.isChecked()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < mapBoomList.size(); i++) {
                                    Bean_MapBoom.MapBoom boom = mapBoomList.get(i);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", boom.MineRecordId);
                                    bundle.putInt("type", boom.MineType);

                                    if ((chbMoney.isChecked() && boom.MineType == 3) || (chbBoom.isChecked() && boom.MineType == 4)) {

                                        addRedMarker(new LatLng(boom.Latitude, boom.Longitude), bundle);
                                    }
                                }
                            }
                        }).start();
                    }
                }

            }
        });
    }

    private void addRedMarker(LatLng latLng, Bundle bundle) {

        bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_map_boom);
        OverlayOptions options = new MarkerOptions()
                .position(latLng)  //设置marker的位置
                .icon(bitmap).extraInfo(bundle)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(false);  //设置手势拖拽
        mBaiduMap.addOverlay(options);
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
            scanBoom(latLng);
        }
    }

    private void scanBoom(LatLng location) {
        int scanRange = 50;
        Map<String, String> params = new HashMap<>();
        params.put("IsContainRedPack", chbMoney.isChecked() ? "1" : "0");
        params.put("IsContainGold", chbBoom.isChecked() ? "1" : "0");
        params.put("Longitude", location.longitude + "");
        params.put("Latitude", location.latitude + "");
        params.put("Range", scanRange + "");
        PostTools.postData(context, CommonUntilities.MINE_URL + "GetAreaMines", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                scanCount -= 1;
                if (scanCount < 0)
                    scanCount = 0;
                txtScan.setText(scanCount + "");
                if (scanCount > 0) {
                    imgScan.setEnabled(true);
                    txtScan.setVisibility(View.VISIBLE);
                    txtScan.setText(scanCount + "");
                } else {
                    imgScan.setEnabled(false);
                    txtScan.setVisibility(View.GONE);
                }
                isScan = false;
                Bean_MapBoom bean_MapBoom = new Gson().fromJson(response, Bean_MapBoom.class);
                if (bean_MapBoom.Success && bean_MapBoom.Data != null && bean_MapBoom.Data.size() > 0) {
                    Bean_MapBoom.MapBoom mapBoom = bean_MapBoom.Data.get(0);
                    addMarker(new LatLng(mapBoom.Latitude, mapBoom.Longitude));
                    useScan(mapBoom.MineRecordId, "1");
                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, "WOW!成功排除一颗雷", "您将获得一颗永久雷").showPop();
                } else {
                    useScan("0", "0");
                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, "很遗憾", "什么都没有,继续努力吧!").showPop();
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }

    private void useScan(String id, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("MineRecordId", id);
        params.put("IsCleanMine", type);
        PostTools.postData(context, CommonUntilities.MINE_URL + "", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug("scan" + response);
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
                city = reverseGeoCodeResult.getAddressDetail().city;
                street = reverseGeoCodeResult.getAddressDetail().street;
                area = reverseGeoCodeResult.getAddressDetail().district;
            }
        });
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
    }

    private String provice = "", city = "", area = "", street = "", longItude = "", latItude = "", mineType = "", remark = "", text = "", picUrl = "", picTitle = "";

    /**
     * 放置普通雷
     */
    private void putCommentBoom() {
        Map<String, String> params = new HashMap<>();
        params.put("Provice", provice);
        params.put("City", city);
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
                        for (int i = 0; i < boomList.size(); i++) {
                            if (boomList.get(i).ArmType == 2 && boomList.get(i).Count > 0) {
                                //临时雷
                                boomList.get(i).Count -= 1;
                                break;
                            }
                        }
                        if (boomList.size() > 1) {
                            if (boomList.get(0).ArmType == 2) {
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
                        } else {
                            if (boomList.get(0).Count > 0)
                                boomList.get(0).Count -= 1;
                        }
                        if (boomCount <= 0) {
                            imgArsenal.setEnabled(false);
                            boomCount = 0;
                        }
                        txtArsenal.setText(boomCount + "");
                    }

                    addMarker(new LatLng(Double.parseDouble(latItude), Double.parseDouble(longItude)));
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

    private void addMarker(LatLng latLng) {
        bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_map_boom);
        OverlayOptions options = new MarkerOptions()
                .position(latLng)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(false);  //设置手势拖拽
        mBaiduMap.addOverlay(options);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = marker.getExtraInfo();
        if (bundle != null) {
            String id = bundle.getString("id");
            int type = bundle.getInt("type");
            if (!TextUtils.isEmpty(id)) {
                boomRed(id);
            }
        }
        return false;
    }

    /**
     * 触发红包雷
     *
     * @param id
     */
    private void boomRed(String id) {
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
                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, redBoom.Data.RedPackText, "恭喜!获得" + redBoom.Data.Amount + "元红包").showPop();
                } else
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, "很遗憾", redBoom.Msg).showPop();
            }
        });
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
        if (createPayPwd != null && createPayPwd.isShowing())
            createPayPwd.isShowing();
        if (changePayPwd != null && changePayPwd.isShowing())
            changePayPwd.isShowing();
        if (confirmPwdPop != null && confirmPwdPop.isShowing())
            confirmPwdPop.isShowing();
        if (shopBuyPop != null && shopBuyPop.isShowing())
            shopBuyPop.isShowing();
        if (selectPayPop != null && selectPayPop.isShowing())
            selectPayPop.isShowing();
        if (personalInfo != null && personalInfo.isShowing())
            personalInfo.isShowing();
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
            //普通雷
            Bundle bundle = data.getBundleExtra("data");
            mineType = bundle.getString("type", "6");
            text = bundle.getString("text", "");
            picTitle = bundle.getString("imgUrl", "");
            picUrl = bundle.getString("imgInfo", "");
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
    }

    private String redCount, redAmount, pwdString;

    /**
     * 放置红包雷
     */
    private void putRedBoom() {
        Map<String, String> params = new HashMap<>();
        params.put("Provice", provice);
        params.put("City", city);
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
                        for (int i = 0; i < boomList.size(); i++) {
                            if (boomList.get(i).ArmType == 2 && boomList.get(i).Count > 0) {
                                //临时雷
                                boomList.get(i).Count -= 1;
                                break;
                            }
                        }
                        if (boomList.size() > 1) {
                            if (boomList.get(0).ArmType == 2) {
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
                        } else {
                            if (boomList.get(0).Count > 0)
                                boomList.get(0).Count -= 1;
                        }
                        if (boomCount <= 0) {
                            imgArsenal.setEnabled(false);
                            boomCount = 0;
                        }
                        txtArsenal.setText(boomCount + "");
                    }

                    addMarker(new LatLng(Double.parseDouble(latItude), Double.parseDouble(longItude)));
                    addOverLay(new LatLng(Double.parseDouble(latItude), Double.parseDouble(longItude)));
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
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
//                    inviteMessgeDao.deleteMessage(username);
//                }
//            }
//            // 自己封装的javabean
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(username);
//            msg.setTime(System.currentTimeMillis());
//            msg.setReason(reason);
//
//            // 设置相应status
//            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);

        }

        @Override
        public void onContactAgreed(final String username) {
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getFrom().equals(username)) {
//                    return;
//                }
//            }
//            // 自己封装的javabean
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(username);
//            msg.setTime(System.currentTimeMillis());
//
//            msg.setStatus(InviteMesageStatus.BEAGREED);
//            notifyNewIviteMessage(msg);
//            runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(), "好友申请同意：+"+username, Toast.LENGTH_SHORT).show();
//                }
//
//
//            });

        }

        @Override
        public void onContactRefused(String username) {
            // 参考同意，被邀请实现此功能,demo未实现
//            Log.d(username, username + "拒绝了你的好友请求");
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
