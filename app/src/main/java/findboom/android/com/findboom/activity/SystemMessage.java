package findboom.android.com.findboom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import findboom.android.com.findboom.BaseActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.MsgListAdapter;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_MsgList;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.xrecycleview.XRecyclerView;

/**
 * Created by Administrator on 2016/10/28.
 */

public class SystemMessage extends Activity implements XRecyclerView.LoadingListener {
    private XRecyclerView xRecyclerView;
    private List msgList;
    private MsgListAdapter msgAdapter;
    private int pageIndex = 1, pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_message);
        initView();
        getMsgData();
    }

    private void getMsgData() {
        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", "" + pageIndex);
        params.put("pageSize", "" + pageSize);
        PostTools.getData(this, CommonUntilities.MSG_URL + "GetSystemMsg", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                if (pageIndex == 1)
                    msgList.clear();
                Bean_MsgList bean_friendList = new Gson().fromJson(response, Bean_MsgList.class);
                if (bean_friendList != null && bean_friendList.Success) {
                    msgList.addAll(bean_friendList.Data);
                    if (bean_friendList.Data.size() < pageSize)
                        xRecyclerView.setLoadingMoreEnabled(false);
                    else xRecyclerView.setLoadingMoreEnabled(true);
                }
                msgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                xRecyclerView.loadMoreComplete();
            }
        });
    }

    private void initView() {
        msgList = new ArrayList();
        xRecyclerView = (XRecyclerView) findViewById(R.id.xrecycleview);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(true);
        msgAdapter = new MsgListAdapter(msgList);
        xRecyclerView.setAdapter(msgAdapter);
    }

    public void boomClick(View v) {
        FindBoomApplication.getInstance().playClickSound();
        finish();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        getMsgData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
