package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
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
        mHolder.setText(R.id.txt_name, redRecord.ReceiveUserId);
        mHolder.setText(R.id.txt_money, redRecord.Amount);
        mHolder.setRadiusImage(R.id.img_photo, redRecord.Avatar);
        TextView txtInfo = (TextView) mHolder.itemView.findViewById(R.id.txt_info);
        txtInfo.setText("");
        txtInfo.append(Tools.getSpanString(mHolder.itemView.getContext(), redRecord.ReceiveTime, R.color.content_yellow) + " 在 " +
                Tools.getSpanString(mHolder.itemView.getContext(), address, R.color.content_yellow) + " 领取了 " +
                Tools.getSpanString(mHolder.itemView.getContext(), setUser, R.color.content_yellow) + " 的红包。");
    }

    @Override
    public int getLayout() {
        return R.layout.record_red_item;
    }

}
