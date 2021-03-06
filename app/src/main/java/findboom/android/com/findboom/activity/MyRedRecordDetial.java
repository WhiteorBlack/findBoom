package findboom.android.com.findboom.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.BaseFragmentActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.BaseRecyAdapter;
import findboom.android.com.findboom.adapter.RedRecordAdapter;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_BoomDetial;
import findboom.android.com.findboom.bean.RedRecord;
import findboom.android.com.findboom.dailog.AddFriendPop;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2016/9/29.
 * 红包雷记录详情
 */
public class MyRedRecordDetial extends BaseFragmentActivity implements PopInterfacer {
    private TextView imgType;
    private TextView txtBoomType, txtBoomState;
    private TextView txtRemark;
    private TextView txtUserName;
    private TextView txtCount;
    private String boomId;
    private int type = 0;
    private boolean isMine = false; //标示是不是我埋的雷
    private List<RedRecord> redRecords;
    private RedRecordAdapter redRecordAdapter;
    private RecyclerView recyclerView;
    private AddFriendPop addFriendPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_red_record_detial);
        initView();
        getBoomInfo();
    }

    private void initView() {
        redRecords = new ArrayList<>();
        boomId = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        isMine = getIntent().getBooleanExtra("isMine", false);
        imgType = (TextView) findViewById(R.id.txt_type);

        txtRemark = (TextView) findViewById(R.id.txt_red_text);
        txtUserName = (TextView) findViewById(R.id.txt_user_name);
        txtBoomState = (TextView) findViewById(R.id.txt_boom_state);
        txtBoomType = (TextView) findViewById(R.id.txt_boom_type);
        txtCount = (TextView) findViewById(R.id.txt_count);

        if (redRecordAdapter == null)
            redRecordAdapter = new RedRecordAdapter(redRecords);
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
        }
    }

    private void createPop(String id) {
        if (TextUtils.isEmpty(id) || TextUtils.equals(id, AppPrefrence.getUserName(context)))
            return;
        if (addFriendPop == null)
            addFriendPop = new AddFriendPop(this);
        addFriendPop.showPop(txtBoomState);
        addFriendPop.setId(id);
        addFriendPop.setPopInterfacer(this, 0);
    }

    private Bean_BoomDetial bean_BoomDetial;

    private void getBoomInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("mineRecordId", boomId);
        PostTools.getData(this, CommonUntilities.USER_URL + "GetRedPackMineInfo", params, new PostCallBack() {
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

    private void setData() {
        imgType.setText(bean_BoomDetial.Data.MineTypeTxt);
        txtBoomState.setText(bean_BoomDetial.Data.StatusTxt);
        txtBoomType.append("总金额:");
        txtBoomType.append(Tools.getSpanString(this, bean_BoomDetial.Data.TotalAmount, Color.rgb(255, 255, 255)));
        txtBoomType.append("元");
        txtRemark.setText(bean_BoomDetial.Data.RedPackText);
        if (isMine)
            txtUserName.setText("我的红包雷");
        else
            txtUserName.setText((TextUtils.isEmpty(bean_BoomDetial.Data.UserNickName) ? "玩儿家 " + bean_BoomDetial.Data.UserId : bean_BoomDetial.Data.UserNickName) + " 的红包雷");
        txtCount.setText("已领取:" + (bean_BoomDetial.Data.Count - bean_BoomDetial.Data.LeftCount) + "/" + bean_BoomDetial.Data.Count);

        if (bean_BoomDetial.Data.RedPackReciveRecords != null) {
            redRecords.addAll(bean_BoomDetial.Data.RedPackReciveRecords);
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
