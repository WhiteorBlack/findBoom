package findboom.android.com.findboom;/**
 * Created by Administrator on 2016/8/8.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import findboom.android.com.findboom.activity.LoginActivity;
import findboom.android.com.findboom.activity.PutCommenBoom;
import findboom.android.com.findboom.activity.PutRedBoom;
import findboom.android.com.findboom.alipay.AliPayHelper;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.BaseBean;
import findboom.android.com.findboom.bean.Bean_AllConfig;
import findboom.android.com.findboom.bean.Bean_BoomBoom;
import findboom.android.com.findboom.bean.Bean_MapBoom;
import findboom.android.com.findboom.bean.Bean_UserArm;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.bean.Bean_WXpay;
import findboom.android.com.findboom.dailog.AdvicePop;
import findboom.android.com.findboom.dailog.ArsenalPop;
import findboom.android.com.findboom.dailog.BandPhonePop;
import findboom.android.com.findboom.dailog.BoomPop;
import findboom.android.com.findboom.dailog.ChangePayPwdPop;
import findboom.android.com.findboom.dailog.ConfrimPwdPop;
import findboom.android.com.findboom.dailog.CreatePayPwdPop;
import findboom.android.com.findboom.dailog.DefensePop;
import findboom.android.com.findboom.dailog.FriendListPop;
import findboom.android.com.findboom.dailog.GetRecordPop;
import findboom.android.com.findboom.dailog.MessageDialog;
import findboom.android.com.findboom.dailog.NewShopPop;
import findboom.android.com.findboom.dailog.PersonalCenterPop;
import findboom.android.com.findboom.dailog.PersonalInfo;
import findboom.android.com.findboom.dailog.PickerDialog;
import findboom.android.com.findboom.dailog.PostResultPop;
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
public class Home extends BaseActivity implements PopInterfacer, LocationListener, BaiduMap.OnMapClickListener, BaiduMap.OnMapLongClickListener {
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

    private List<Bean_UserArm.UserArm> defenseList;
    private List<Bean_UserArm.UserArm> boomList;
    private List<Bean_UserArm.UserArm> scanList;

    public static boolean isForeground;
    private boolean isPutBoom = false; //标识现在是否有放雷的动作

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initLocation();
        initView();
        getUserArm();
        getAllConfig();
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private void startRecordPop() {
        if (getRecordPop == null)
            getRecordPop = new GetRecordPop(context);
        getRecordPop.showPop(imgArsenal);
        getRecordPop.setPopInterfacer(this, 10);
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

        expandableSelector = (ExpandableSelector) findViewById(R.id.img_expand);
        expandableSelector.setVisibility(View.INVISIBLE);
        List<ExpandableItem> expandableItems = new ArrayList<>();
        expandableItems.add(new ExpandableItem(R.mipmap.friend_msg));
        expandableItems.add(new ExpandableItem(R.mipmap.system_msg));
        expandableSelector.showExpandableItems(expandableItems);
        expandableSelector.setOnExpandableItemClickListener(new OnExpandableItemClickListener() {
            @Override
            public void onExpandableItemClickListener(int index, View view) {
                if (index == 0) {
                    //系统消息

                } else {
                    //好友消息

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
//                if (messageDialog == null)
//                    messageDialog = new MessageDialog(context);
//                messageDialog.showAtLocation(imgMsg, Gravity.BOTTOM | Gravity.RIGHT, Tools.dip2px(context, 15), Tools.dip2px(context, 100));
//                messageDialog.setPopInterfacer(this, 23);
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
        }
    }

    private int chargeTyp = -1;
    private float money;
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
                break;
            case 5: //放置地雷
                isPutBoom = true;
                Tools.toastMsg(context, "请在地图上选择你要放置的地点");
                break;
            case 7:   //战绩-->举报
                if (bundle != null && bundle.getInt("type", 0) == 0) {
                    if (reportPop == null)
                        reportPop = new ReportPop(context);
                    reportPop.showPop(imgArsenal);
                    reportPop.setId(bundle.getString("userId"));
                    reportPop.setPopInterfacer(this, 9);
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
        }
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
//                File file = new File(Environment.getExternalStorageDirectory(), "/findBoom/" + System.currentTimeMillis() + ".jpg");
//                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
//                final Uri imageUri = Uri.fromFile(file);
//                CharSequence[] items = {"手机相册", "手机拍照"};
//                final TakePhoto takePhoto = getTakePhoto();
//                final CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
//                new AlertDialog.Builder(this).setTitle("选择照片").setCancelable(true).setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
//                                break;
//                            case 1:
//                                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
//                                break;
//                            case 2:
//                                dialog.dismiss();
//                                break;
//                        }
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
                Tools.debug("takephoto");
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
                            if (!TextUtils.isEmpty(orderInfo))
                                orderInfo = orderInfo.replace("\\", "");
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
                    new PostResultPop(context, txtArsenal, R.drawable.icon_right, baseBean.Msg, "").showPop();
                    switch (armType) {
                        case 0:
                            boomList.addAll(baseBean.Data);
                            imgArsenal.setEnabled(true);
                            txtArsenal.setVisibility(View.VISIBLE);
                            boomCount = 0;
                            for (int i = 0; i < boomList.size(); i++) {
                                boomCount += boomList.get(i).Count;
                            }
                            txtArsenal.setText(boomCount + "");
                            break;
                        case 1:

                            break;
                        case 2:
                            scanList.addAll(baseBean.Data);
                            imgScan.setEnabled(true);
                            txtScan.setVisibility(View.VISIBLE);
                            scanCount = 0;
                            for (int i = 0; i < scanList.size(); i++) {
                                scanCount += scanList.get(i).Count;
                            }
                            txtScan.setText(scanCount + "");
                            break;
                        case 4:
                            defenseList.addAll(baseBean.Data);
                            imgDefense.setEnabled(true);
                            txtDefense.setVisibility(View.VISIBLE);
                            defenseCount = 0;
                            for (int i = 0; i < defenseList.size(); i++) {
                                defenseCount += defenseList.get(i).Count;
                            }
                            txtDefense.setText(defenseCount + "");
                            break;
                    }

                } else
                    new PostResultPop(context, txtArsenal, R.drawable.icon_error, baseBean.Msg, "").showPop();
            }
        });
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
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                Tools.debug("ease logout success");
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        startActivity(new Intent(context, LoginActivity.class));
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

    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
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
                    if (boomPop == null)
                        boomPop = new BoomPop(context);
                    boomPop.showPop(txtArsenal);
                    boomPop.setPopInterfacer(Home.this, 18);
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
                }

            }
        });
    }

    private GeoCoder geoCoder;

    @Override
    public void onMapClick(LatLng latLng) {
        if (isPutBoom) {
            longItude = latLng.longitude + "";
            latItude = latLng.latitude + "";
            initGeoCoder(latLng);
            selectBoomPop(latLng);
        }
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
}
