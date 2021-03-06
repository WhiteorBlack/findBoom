package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.RedRecord;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class RedRecordAdapter extends BaseRecyAdapter {
    public RedRecordAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public RedRecordAdapter(List dataList) {
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
        RedRecord redRecord = (RedRecord) dataList.get(position);
        mHolder.setText(R.id.txt_name, TextUtils.isEmpty(redRecord.ReceiveUserNickName) ? redRecord.ReceiveUserId : redRecord.ReceiveUserNickName);
        mHolder.setText(R.id.txt_money, redRecord.Amount + "元");
        mHolder.setRadiusImage(R.id.img_photo, redRecord.ReceiveUserAvatar);
        mHolder.setText(R.id.txt_info, redRecord.ReceiveTime);


    }

    @Override
    public int getLayout() {
        return R.layout.record_red_item;
    }

}
