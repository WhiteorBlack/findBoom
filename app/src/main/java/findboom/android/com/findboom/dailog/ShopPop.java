package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.ShopAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_ShopList;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:
 */
public class ShopPop extends BasePopupwind{
    private Context context;
    private View view;
    private RecyclerView recyShop;
    private List shopList;
    private ShopAdapter shopAdapter;

    public ShopPop(Context context){
        super(context);
        this.context=context;
        initView();
        getData();
    }

    private void getData() {
        PostTools.getData(context, CommonUntilities.ARMS_CENTER_URL+"Index",new HashMap<String, String>(),new PostCallBack(){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response)){

                    return;
                }
                Bean_ShopList bean_ShopList=new Gson().fromJson(response,Bean_ShopList.class);
                if (bean_ShopList!=null&&bean_ShopList.Success){
                    shopList.addAll(bean_ShopList.Data);
                    shopAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initView() {
        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.shop_center,null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        recyShop=(RecyclerView)view.findViewById(R.id.recy_shop);
        recyShop.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyShop.setItemAnimator(new DefaultItemAnimator());

        shopList=new ArrayList();
        shopAdapter=new ShopAdapter(context,shopList);
        recyShop.setAdapter(shopAdapter);
        shopAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
                Bean_ShopList.GoodsInfo goodsInfo = (Bean_ShopList.GoodsInfo) shopList.get(position);
                Bundle bundle=new Bundle();
                bundle.putInt("type",0);
                bundle.putString("id",goodsInfo.ArmInfoId);
                if (popInterfacer!=null)
                    popInterfacer.OnConfirm(flag,bundle);
            }
        });
        this.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.img_close:
                dismiss();
                break;
        }
    }
}
