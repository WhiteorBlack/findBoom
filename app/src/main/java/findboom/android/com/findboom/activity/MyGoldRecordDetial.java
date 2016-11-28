package findboom.android.com.findboom.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.BaseFragmentActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.BaseRecyAdapter;
import findboom.android.com.findboom.adapter.GoldRecordAdapter;
import findboom.android.com.findboom.adapter.RedRecordAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_BoomDetial;
import findboom.android.com.findboom.bean.GoldRecord;
import findboom.android.com.findboom.bean.RedRecord;
import findboom.android.com.findboom.dailog.AddFriendPop;
import findboom.android.com.findboom.dailog.CheckPhoto;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.CircleImageView;

/**
 * Created by Administrator on 2016/9/29.
 * 寻宝雷记录详情
 */
public class MyGoldRecordDetial extends BaseFragmentActivity implements PopInterfacer {
    private String boomId;
    private int type = 0;
    private boolean isMine = false; //标示是不是我埋的雷
    private List<GoldRecord> redRecords;
    private GoldRecordAdapter redRecordAdapter;
    private RecyclerView recyclerView;
    private AddFriendPop addFriendPop;
    private CheckPhoto checkPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_gold_record_detial);
        initView();
        getBoomInfo();
    }

    private void initView() {
        redRecords = new ArrayList<>();
        boomId = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        isMine = getIntent().getBooleanExtra("isMine", false);

        if (redRecordAdapter == null)
            redRecordAdapter = new GoldRecordAdapter(redRecords);
        recyclerView = (RecyclerView) findViewById(R.id.recy_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(redRecordAdapter);
        redRecordAdapter.setOnItemClickListener(new BaseRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                createPop(redRecords.get(position).ReceiveUserId);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });
    }

    @Override
    public void boomClick(View v) {
        super.boomClick(v);
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.txt_user_name:
                createPop(bean_BoomDetial.Data.UserId);
                break;
            case R.id.img_pic:
                if (!TextUtils.isEmpty(picUrl)) {
                    if (checkPhoto == null)
                        checkPhoto = new CheckPhoto(context);
                    checkPhoto.setPhoto(picUrl);
                    checkPhoto.setDeleteInvis();
                    checkPhoto.showPop(recyclerView);
                }
                break;
        }
    }

    private void createPop(String id) {
        if (TextUtils.isEmpty(id) || TextUtils.equals(id, AppPrefrence.getUserName(context)))
            return;
        if (addFriendPop == null)
            addFriendPop = new AddFriendPop(this);
        addFriendPop.showPop(recyclerView);
        addFriendPop.setId(id);
        addFriendPop.setPopInterfacer(this, 0);
    }

    private Bean_BoomDetial bean_BoomDetial;

    private void getBoomInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("mineRecordId", boomId);
        PostTools.getData(this, CommonUntilities.USER_URL + "GetGoldMineInfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                bean_BoomDetial = new Gson().fromJson(response, Bean_BoomDetial.class);
                if (bean_BoomDetial != null && bean_BoomDetial.Success) {
                    setData();
                }
            }
        });
    }

    private String picUrl = "";

    private void setData() {
        if (bean_BoomDetial.Data.GoldReciveRecords != null) {
            redRecordAdapter.setAddress(bean_BoomDetial.Data.City+bean_BoomDetial.Data.Area+bean_BoomDetial.Data.Street);
            redRecords.addAll(bean_BoomDetial.Data.GoldReciveRecords);
            redRecordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnDismiss(int flag) {

    }

    @Override
    public void OnConfirm(int flag, Bundle bundle) {

    }

    @Override
    public void OnCancle(int flag) {

    }
}
