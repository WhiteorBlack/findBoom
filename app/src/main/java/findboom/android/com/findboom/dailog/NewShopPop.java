package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.GoldAdapter;
import findboom.android.com.findboom.adapter.ShopAdapter;
import findboom.android.com.findboom.adapter.ViewPagerAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_GoldList;
import findboom.android.com.findboom.bean.Bean_ShopList;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.CircleImageView;
import findboom.android.com.findboom.widget.xrecycleview.XRecyclerView;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:
 */
public class NewShopPop extends BasePopupwind implements ViewPager.OnPageChangeListener {
    private Context context;
    private View view;
    private ViewPager viewPager;
    private ImageView imgPersonal, imgAccount;
    private ImageView imgSelector;

    private List<View> viewList;

    //商城信息
    private View boomView;
    private XRecyclerView recyShop;
    private List shopList;
    private ShopAdapter shopAdapter;

    //金币商城
    //商城信息
    private View goldView;
    private XRecyclerView recyGold;
    private List goldList;
    private GoldAdapter goldAdapter;

    public NewShopPop(Context context) {
        super(context);
        this.context = context;
        initView();
        initData();
        getData();
        getGoldData();
    }

    private void getData() {
        PostTools.getData(context, CommonUntilities.ARMS_CENTER_URL + "Index", new HashMap<String, String>(), new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response)) {

                    return;
                }
                Bean_ShopList bean_ShopList = new Gson().fromJson(response, Bean_ShopList.class);
                if (bean_ShopList != null && bean_ShopList.Success) {
                    shopList.addAll(bean_ShopList.Data);
                    shopAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getGoldData() {
        PostTools.getData(context, CommonUntilities.CONFIG_URL + "GetGoldCoinConfig", new HashMap<String, String>(), new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Bean_GoldList bean_ShopList = new Gson().fromJson(response, Bean_GoldList.class);
                if (bean_ShopList != null && bean_ShopList.Success) {
                    goldList.addAll(bean_ShopList.Data);
                    goldAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData() {
        if (boomView == null)
            boomView = LayoutInflater.from(context).inflate(R.layout.xrecycleview, null);
        recyShop = (XRecyclerView) boomView.findViewById(R.id.xrecycleview);
        recyShop.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyShop.setItemAnimator(new DefaultItemAnimator());
        recyShop.setPullRefreshEnabled(false);
        recyShop.setLoadingMoreEnabled(false);

        shopList = new ArrayList();
        shopAdapter = new ShopAdapter(context, shopList);
        recyShop.setAdapter(shopAdapter);
        shopAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
                Bean_ShopList.GoodsInfo goodsInfo = (Bean_ShopList.GoodsInfo) shopList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putFloat("money", goodsInfo.Price );
                bundle.putInt("armType", goodsInfo.ArmType);
                bundle.putString("id", goodsInfo.ArmInfoId);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
            }
        });

        if (goldView == null)
            goldView = LayoutInflater.from(context).inflate(R.layout.xrecycleview, null);
        recyGold = (XRecyclerView) goldView.findViewById(R.id.xrecycleview);
        recyGold.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyGold.setItemAnimator(new DefaultItemAnimator());
        recyGold.setPullRefreshEnabled(false);
        recyGold.setLoadingMoreEnabled(false);

        goldList = new ArrayList();
        goldAdapter = new GoldAdapter(context, goldList);
        recyGold.setAdapter(goldAdapter);
        goldAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
               Bean_GoldList.GoldList goodsInfo = ( Bean_GoldList.GoldList) goldList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putFloat("money", goodsInfo.Price );
                bundle.putString("id", goodsInfo.ConfigId);
                bundle.putInt("amount",goodsInfo.GoldAmount);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
            }
        });
        if (viewList == null)
            viewList = new ArrayList<>();
        viewList.add(boomView);
        viewList.add(goldView);
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.new_shop_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        params.height = (int) (Tools.getScreenWide(context) * 0.8);
        viewPager.setLayoutParams(params);
        imgAccount = (ImageView) view.findViewById(R.id.img_account);
        imgAccount.setOnClickListener(this);
        imgAccount.setEnabled(true);
        imgPersonal = (ImageView) view.findViewById(R.id.img_personal);
        imgPersonal.setOnClickListener(this);

        imgPersonal.setEnabled(false);
        imgSelector = (ImageView) view.findViewById(R.id.img_selector);
        this.setContentView(view);
        this.setFocuse(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.img_account:
                viewPager.setCurrentItem(1);
                imgAccount.setEnabled(false);
                imgPersonal.setEnabled(true);
                break;
            case R.id.img_personal:
                viewPager.setCurrentItem(0);
                imgAccount.setEnabled(true);
                imgPersonal.setEnabled(false);
                break;

        }
    }

    float moveDis, marginList = 0;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgSelector.getLayoutParams();
//        if (position == 1 && positionOffsetPixels == 0) {
//            return;
//        }
//
//        params.leftMargin = (int) (positionOffsetPixels * ((Tools.getScreenWide(context) - Tools.dip2px(context, 142) - imgSelector.getWidth()) / (viewPager.getWidth() - Tools.dip2px(context, 20))));
//        imgSelector.setLayoutParams(params);

        moveDis = Tools.getScreenWide(context) - Tools.dip2px(context, 110) + imgSelector.getWidth() / 2;//selector移动距离
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgSelector.getLayoutParams();

        marginList = (moveDis / 2) * (position + positionOffset);
        params.leftMargin = (int) marginList;
        imgSelector.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            imgAccount.setEnabled(true);
            imgPersonal.setEnabled(false);
        } else {
            imgAccount.setEnabled(false);
            imgPersonal.setEnabled(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
