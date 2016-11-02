package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.MyRecordDetial;
import findboom.android.com.findboom.activity.MyRedRecordDetial;
import findboom.android.com.findboom.adapter.BaseRecyAdapter;
import findboom.android.com.findboom.adapter.MyBoomRecordAdapter;
import findboom.android.com.findboom.adapter.MyGetBoomAdapter;
import findboom.android.com.findboom.adapter.RankAdapter;
import findboom.android.com.findboom.adapter.ViewPagerAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_MyBoomRecord;
import findboom.android.com.findboom.bean.Bean_Rank;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.CircleImageView;
import findboom.android.com.findboom.widget.xrecycleview.XRecyclerView;
import findboom.android.com.findboom.wxpay.Constants;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:战绩中心
 */
public class RecordPop extends BasePopupwind implements ViewPager.OnPageChangeListener, XRecyclerView.LoadingListener {
    private Context context;
    private View view;
    private ViewPager viewPager;
    private View myBoomView, myGetView, rankView;
    private XRecyclerView boomListView, getListView, rankListView;
    private ImageView imgSelector;
    private ImageView imgBoom, imgGet, imgRank;
    private List<View> viewList;

    private List<Bean_MyBoomRecord.BoomInfo> myBoomList, getBoomList;
    private List<Bean_Rank.RankUser> rankList;
    private MyBoomRecordAdapter myRecordAdapter;
    private MyGetBoomAdapter getBoomAdapter;
    private RankAdapter rankAdapter;
    private Button btnShare;
    private int position = 0;

    private int myIndex = 1, getIndex = 1, rankIndex = 1, pageSize = 15;


    public RecordPop(Context context) {
        super(context);
        this.context = context;
        initView();
        initData();
        getMyBoom();
    }

    private void getMyBoom() {
        Map<String, String> params = new HashMap<>();
        params.put("PageIndex", myIndex + "");
        params.put("PageSize", pageSize + "");
        PostTools.getData(context, CommonUntilities.USER_URL + "GetMyTotalMine", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                Bean_MyBoomRecord bean_MyBoomRecord = new Gson().fromJson(response, Bean_MyBoomRecord.class);
                if (bean_MyBoomRecord != null && bean_MyBoomRecord.Success) {
                    if (myIndex == 1)
                        myBoomList.clear();
                    myBoomList.addAll(bean_MyBoomRecord.Data);
                    if (bean_MyBoomRecord.Data.size() < pageSize)
                        boomListView.setLoadingMoreEnabled(false);
                    else boomListView.setLoadingMoreEnabled(true);
                } else boomListView.setLoadingMoreEnabled(false);
                myRecordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                boomListView.loadMoreComplete();
            }
        });
    }

    private void getBoom() {
        Map<String, String> params = new HashMap<>();
        params.put("PageIndex", getIndex + "");
        params.put("PageSize", pageSize + "");
        PostTools.getData(context, CommonUntilities.USER_URL + "GetMyBombTotalMine", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                Bean_MyBoomRecord bean_MyBoomRecord = new Gson().fromJson(response, Bean_MyBoomRecord.class);
                if (bean_MyBoomRecord != null && bean_MyBoomRecord.Success) {
                    if (getIndex == 1)
                        getBoomList.clear();
                    getBoomList.addAll(bean_MyBoomRecord.Data);
                    if (bean_MyBoomRecord.Data.size() < pageSize)
                        getListView.setLoadingMoreEnabled(false);
                    else getListView.setLoadingMoreEnabled(true);
                } else getListView.setLoadingMoreEnabled(false);
                getBoomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                getListView.loadMoreComplete();
            }
        });
    }

    private void getRank() {
        Map<String, String> params = new HashMap<>();
        params.put("rankDate", System.currentTimeMillis() + "");
        PostTools.getData(context, CommonUntilities.RANK_URL, params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                Bean_Rank bean_MyBoomRecord = new Gson().fromJson(response, Bean_Rank.class);
                if (bean_MyBoomRecord != null && bean_MyBoomRecord.Success) {
                    rankList.clear();
                    rankList.addAll(bean_MyBoomRecord.Data);
                }
                rankAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAfter() {
                super.onAfter();
//                getListView.loadMoreComplete();
            }
        });
    }

    private void initData() {
        myBoomList = new ArrayList<>();
        viewList = new ArrayList<>();
        getBoomList = new ArrayList<>();
        rankList = new ArrayList<>();

        //我埋得雷
        myBoomView = LayoutInflater.from(context).inflate(R.layout.xrecycleview, null);
        boomListView = (XRecyclerView) myBoomView.findViewById(R.id.xrecycleview);
        boomListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        boomListView.setPullRefreshEnabled(false);
        boomListView.setLoadingMoreEnabled(false);
        boomListView.setLoadingListener(this);
        myRecordAdapter = new MyBoomRecordAdapter(myBoomList);
        boomListView.setAdapter(myRecordAdapter);
        myRecordAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
                Bean_MyBoomRecord.BoomInfo boomInfo = myBoomList.get(position);
                if (boomInfo.MineType == 3)
                    context.startActivity(new Intent(context, MyRedRecordDetial.class).putExtra("id", boomInfo.MineRecordId));
                else
                    context.startActivity(new Intent(context, MyRecordDetial.class).
                            putExtra("id", boomInfo.MineRecordId).putExtra("isMine", true));
            }
        });

        //我踩到雷
        myGetView = LayoutInflater.from(context).inflate(R.layout.xrecycleview, null);
        getListView = (XRecyclerView) myGetView.findViewById(R.id.xrecycleview);
        getListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        getListView.setPullRefreshEnabled(false);
        getListView.setLoadingMoreEnabled(false);
        getListView.setLoadingListener(this);
        getBoomAdapter = new MyGetBoomAdapter(getBoomList);
        getListView.setAdapter(getBoomAdapter);
        getBoomAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
                //举报
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putString("userId", getBoomList.get(position).MineRecordId);
                popInterfacer.OnConfirm(flag, bundle);
            }
        });

        getBoomAdapter.setOnItemClickListener(new BaseRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Bean_MyBoomRecord.BoomInfo boomInfo = myBoomList.get(position);
                if (boomInfo.MineType == 3)
                    context.startActivity(new Intent(context, MyRedRecordDetial.class).putExtra("id", boomInfo.MineRecordId));
                else
                    context.startActivity(new Intent(context, MyRecordDetial.class).
                            putExtra("id", boomInfo.MineRecordId).putExtra("isMine", false));
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });

        //排行榜
        rankView = LayoutInflater.from(context).inflate(R.layout.xrecycleview, null);
        rankListView = (XRecyclerView) rankView.findViewById(R.id.xrecycleview);
        rankListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rankListView.setPullRefreshEnabled(false);
        rankListView.setLoadingMoreEnabled(false);
        rankListView.setLoadingListener(this);
        rankAdapter = new RankAdapter(rankList);
        rankListView.setAdapter(rankAdapter);
        rankAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("id",rankList.get(position).UserId);
                bundle.putInt("type",1);
                if (popInterfacer!=null)
                    popInterfacer.OnConfirm(flag,bundle);
            }
        });

        viewList.add(myBoomView);
        viewList.add(myGetView);
        viewList.add(rankView);
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        boomSelected();
        contentWide = (int) (Tools.getScreenWide(context) - Tools.dip2px(context, 45));//viewPager总移动距离
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.record_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        params.height = (int) (Tools.getScreenWide(context) * 0.8);
        viewPager.setLayoutParams(params);
        imgSelector = (ImageView) view.findViewById(R.id.img_selector);
        btnShare = (Button) view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);

        imgBoom = (ImageView) view.findViewById(R.id.img_personal);
        imgBoom.setOnClickListener(this);
        imgGet = (ImageView) view.findViewById(R.id.img_find);
        imgGet.setOnClickListener(this);
        imgRank = (ImageView) view.findViewById(R.id.img_account);
        imgRank.setOnClickListener(this);
        this.setContentView(view);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.img_account:
                viewPager.setCurrentItem(2);
                break;
            case R.id.img_personal:
                viewPager.setCurrentItem(0);
                break;
            case R.id.img_find:
                viewPager.setCurrentItem(1);
                break;
            case R.id.btn_share:
                Bundle bundle=new Bundle();
                bundle.putInt("type",2);
//                share();
                if(popInterfacer!=null)
                    popInterfacer.OnConfirm(flag,bundle);
                break;
        }
    }

    IWXAPI msgApi;

    private void share() {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = CommonUntilities.SHARE_RECORD + AppPrefrence.getUserName(context);

        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = "快来看看我无敌的战绩";
        msg.description = "我在激战中,需要你的支援";
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_logo);
        msg.thumbData = findboom.android.com.findboom.wxpay.Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        if (msgApi == null)
            msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    float marginList = 0, contentWide, moveDis;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        moveDis = Tools.getScreenWide(context) - Tools.dip2px(context, 110) + imgSelector.getWidth() / 2;//selector移动距离
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgSelector.getLayoutParams();

        marginList = (moveDis / 3) * (position + positionOffset);
        params.leftMargin = (int) marginList;
        imgSelector.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        switch (position) {
            case 0:
                boomSelected();
                break;
            case 1:
                if (getBoomList == null || getBoomList.size() == 0) {
                    getIndex = 1;
                    getBoom();
                }
                getSelected();
                break;
            case 2:
                if (rankList == null || rankList.size() == 0) {
                    getRank();
                }
                rankSelected();
                break;
        }
    }

    private void boomSelected() {
        imgRank.setEnabled(true);
        imgGet.setEnabled(true);
        imgBoom.setEnabled(false);
        btnShare.setVisibility(View.GONE);
    }

    private void getSelected() {
        imgRank.setEnabled(true);
        imgGet.setEnabled(false);
        imgBoom.setEnabled(true);
        btnShare.setVisibility(View.VISIBLE);
    }

    private void rankSelected() {
        imgRank.setEnabled(false);
        imgGet.setEnabled(true);
        imgBoom.setEnabled(true);
        btnShare.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        switch (position) {
            case 0:
                myIndex++;
                getMyBoom();
                break;
            case 1:
                getIndex++;
                getBoom();
                break;
            case 2:

                break;
        }
    }
}
