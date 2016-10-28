package findboom.android.com.findboom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.BaseRecyAdapter;
import findboom.android.com.findboom.adapter.InviteListAdapter;
import findboom.android.com.findboom.adapter.MsgListAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_InviteList;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.xrecycleview.XRecyclerView;

/**
 * Created by Administrator on 2016/10/28.
 */

public class InviteFragment extends Fragment implements XRecyclerView.LoadingListener {
    private View view;
    private XRecyclerView xRecyclerView;
    private List msgList;
    private InviteListAdapter msgAdapter;
    private int pageIndex = 1, pageSize = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.xrecycleview, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void getMsgData() {
        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", "" + pageIndex);
        params.put("pageSize", "" + pageSize);
        PostTools.getData(getContext(), CommonUntilities.FRIEND_URL + "GetApplyRecord", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                if (pageIndex == 1)
                    msgList.clear();
                Bean_InviteList bean_friendList = new Gson().fromJson(response, Bean_InviteList.class);
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
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.xrecycleview);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(true);
        msgAdapter = new InviteListAdapter(msgList);
        xRecyclerView.setAdapter(msgAdapter);
        msgAdapter.setOnItemClickListener(new BaseRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        getMsgData();
    }

}
