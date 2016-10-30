package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.FriendAdapter;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_FriendList;
import findboom.android.com.findboom.chat.activity.ChatActivity;
import findboom.android.com.findboom.chat.db.EaseUser;
import findboom.android.com.findboom.chat.db.UserDao;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class FriendListPop extends BasePopupwind {

    private Context context;
    private View view;
    private RecyclerView recyFriend;
    private EditText edtSearch;
    private List friendList;
    private FriendAdapter friendAdapter;

    public FriendListPop(Context context) {
        super(context);
        this.context = context;
        initView();
        getFriend();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.friend_list_pop, null);
        edtSearch = (EditText) view.findViewById(R.id.edt_search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.findViewById(R.id.img_close).setOnClickListener(this);
        recyFriend = (RecyclerView) view.findViewById(R.id.recy_friend);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyFriend.getLayoutParams();
        params.height = (int) (Tools.getScreenWide(context) * 0.8);
        recyFriend.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyFriend.setItemAnimator(new DefaultItemAnimator());

        friendList = new ArrayList();
        friendAdapter = new FriendAdapter(context, friendList);
        friendAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
                Bean_FriendList.Friend friend = (Bean_FriendList.Friend) friendList.get(position);
                context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", friend.EasemobId).putExtra("username", friend.FriendNickName));
            }
        });
        recyFriend.setAdapter(friendAdapter);
        this.setContentView(view);
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
        params.put("pageIndex", "1");
        params.put("pageSize", "1000");
        PostTools.getData(context, CommonUntilities.FRIEND_URL + "GetRecord", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Tools.debug(response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                Bean_FriendList bean_friendList = new Gson().fromJson(response, Bean_FriendList.class);
                if (bean_friendList != null && bean_friendList.Success) {
                    friendList.addAll(bean_friendList.Data);
                    friendAdapter.notifyDataSetChanged();
                    List<EaseUser> userList = new ArrayList<EaseUser>();
                    for (int i = 0; i < friendList.size(); i++) {
                        Bean_FriendList.Friend friend = (Bean_FriendList.Friend) friendList.get(i);
                        EaseUser easeUser = new EaseUser(friend.EasemobId);
                        easeUser.setAvatar(friend.Avatar);
                        easeUser.setNick(friend.FriendNickName);
                        userList.add(easeUser);
                    }
                    new UserDao(context).saveContactList(userList);
                }

            }
        });
    }
}
