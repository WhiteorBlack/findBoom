package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.FriendAdapter;
import findboom.android.com.findboom.adapter.RecordListAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_FriendList;
import findboom.android.com.findboom.bean.Bean_RecordList;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.xrecycleview.XRecyclerView;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:积分列表
 */
public class RecordListPop extends BasePopupwind implements XRecyclerView.LoadingListener {

    private Context context;
    private View view;
    private XRecyclerView recyFriend;
    private List friendList;
    private RecordListAdapter friendAdapter;
    private int pageIndex = 1, pageSize = 20;

    public RecordListPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.record_list_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        recyFriend = (XRecyclerView) view.findViewById(R.id.recy_friend);
        recyFriend.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyFriend.setItemAnimator(new DefaultItemAnimator());
        recyFriend.setPullRefreshEnabled(false);
        recyFriend.setLoadingMoreEnabled(true);
        recyFriend.setLoadingListener(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyFriend.getLayoutParams();
        params.height = (int) (Tools.getScreenWide(context) * 0.8);
        recyFriend.setLayoutParams(params);

        friendList = new ArrayList();
        friendAdapter = new RecordListAdapter(context, friendList);
        recyFriend.setAdapter(friendAdapter);
        this.setContentView(view);
        getFriend();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;

        }
    }

    private void getFriend() {
        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", "" + pageIndex);
        params.put("pageSize", "" + pageSize);
        PostTools.getData(context, CommonUntilities.USER_URL + "GetUserScoreRecords", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                if (pageIndex == 1)
                    friendList.clear();
                Bean_RecordList bean_friendList = new Gson().fromJson(response, Bean_RecordList.class);
                if (bean_friendList != null && bean_friendList.Success) {
                    friendList.addAll(bean_friendList.Data);
                    if (bean_friendList.Data.size() < pageSize)
                        recyFriend.setLoadingMoreEnabled(false);
                    else recyFriend.setLoadingMoreEnabled(true);
                }
//                recyFriend.loadMoreComplete();
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        getFriend();
    }
}
