package findboom.android.com.findboom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.FindBoomDetial;
import findboom.android.com.findboom.adapter.RedRecordAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_BoomDetial;
import findboom.android.com.findboom.bean.RedRecord;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.xrecycleview.XRecyclerView;

/**
 * Created by Administrator on 2016/9/30.
 */
public class RedBoomFragment extends Fragment implements XRecyclerView.LoadingListener {
    private View view;
    private String boomId;
    private boolean isMine = true;
    private int type;
    private XRecyclerView xRecyclerView;
    private int pageIndex = 1, pageSize = 20;
    private List<RedRecord> recordList;
    private RedRecordAdapter redRecordAdapter;

    public void setInfo(String id, int type, boolean isMine) {
        this.isMine = isMine;
        this.boomId = id;
        this.type = type;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getBoomInfo();
    }

    private Bean_BoomDetial bean_BoomDetial;

    private void getBoomInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("mineRecordId", boomId);
        params.put("PageIndex", pageIndex + "");
        params.put("PageSize", pageSize + "");
        PostTools.getData(getContext(), CommonUntilities.USER_URL + "GetRedPackMineInfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                bean_BoomDetial = new Gson().fromJson(response, Bean_BoomDetial.class);
                if (bean_BoomDetial != null && bean_BoomDetial.Success) {
                    if (pageIndex == 1)
                        recordList.clear();
                    setData();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                xRecyclerView.loadMoreComplete();
            }
        });
    }

    private void setData() {
        ((FindBoomDetial) getActivity()).setState(bean_BoomDetial.Data.BombTypeTxt, bean_BoomDetial.Data.StatusTxt);
        redRecordAdapter.setAddress(bean_BoomDetial.Data.Address, bean_BoomDetial.Data.MineUserNickName);
        recordList.addAll(bean_BoomDetial.Data.RedPackReciveRecords);
        if (bean_BoomDetial.Data.RedPackReciveRecords != null) {
            xRecyclerView.setPullRefreshEnabled(bean_BoomDetial.Data.RedPackReciveRecords.size() < pageSize);
        }
        redRecordAdapter.notifyDataSetChanged();
    }


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

    private void initView() {
        recordList = new ArrayList<>();
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.xrecycleview);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
        redRecordAdapter = new RedRecordAdapter(recordList);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        getBoomInfo();
    }
}
