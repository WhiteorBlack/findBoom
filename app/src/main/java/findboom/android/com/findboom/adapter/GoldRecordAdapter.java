package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.GoldRecord;
import findboom.android.com.findboom.bean.RedRecord;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class GoldRecordAdapter extends BaseRecyAdapter {
    public GoldRecordAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public GoldRecordAdapter(List dataList) {
        super(dataList);
        this.dataList = dataList;
    }

    private String address, setUser;

    public void setAddress(String address, String setUser) {
        this.address = address;
        this.setUser = setUser;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        GoldRecord redRecord = (GoldRecord) dataList.get(position);
        mHolder.setText(R.id.txt_name, TextUtils.isEmpty(redRecord.ReceiveUserNickName) ? redRecord.ReceiveUserId : redRecord.ReceiveUserNickName);
//        mHolder.setText(R.id.txt_money, redRecord.Amount + "元");
        mHolder.setRadiusImage(R.id.img_photo, redRecord.ReceiveUserAvatar);
        mHolder.setText(R.id.txt_info, redRecord.ReceiveTime);


    }

    @Override
    public int getLayout() {
        return R.layout.record_red_item;
    }

}
