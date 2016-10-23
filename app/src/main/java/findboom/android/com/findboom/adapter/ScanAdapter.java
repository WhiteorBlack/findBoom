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
import findboom.android.com.findboom.bean.Bean_UserArm;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class ScanAdapter extends BaseRecyAdapter {
    public ScanAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public ScanAdapter(List dataList) {
        super(dataList);
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        Bean_UserArm.UserArm userArm = (Bean_UserArm.UserArm) dataList.get(position);
        TextView txtName = (TextView) mHolder.itemView.findViewById(R.id.txt_name);
        txtName.append(userArm.ArmTypeTxt);
        SpannableString count = new SpannableString(userArm.Count + "");
        count.setSpan(new ForegroundColorSpan(mHolder.itemView.getContext().getResources().getColor(R.color.price_red)), 0, count.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        count.setSpan(new AbsoluteSizeSpan(20,true),0,count.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtName.append(count);
        txtName.append("个");
        if (userArm.ArmType == 2) {
            mHolder.setText(R.id.txt_intro, "永久");
            mHolder.setImage(R.id.img_photo, R.mipmap.scan_normal);
        } else {
            mHolder.setText(R.id.txt_intro, "临时");
            mHolder.setImage(R.id.img_photo, R.mipmap.scan_temp);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.defense_item;
    }

}
