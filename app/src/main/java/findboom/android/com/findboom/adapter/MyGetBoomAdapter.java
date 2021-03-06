package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_MyBoomRecord;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class MyGetBoomAdapter extends BaseRecyAdapter {
    public MyGetBoomAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public MyGetBoomAdapter(List dataList) {
        super(dataList);
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        Bean_MyBoomRecord.BoomInfo boomInfo = (Bean_MyBoomRecord.BoomInfo) dataList.get(position);
        mHolder.setText(R.id.txt_name, boomInfo.MineTypeTxt);
        TextView txtContent = (TextView) mHolder.itemView.findViewById(R.id.txt_state);
        if (TextUtils.isEmpty(boomInfo.MineUserNickName)) {
            boomInfo.MineUserNickName = "玩儿家";
        }
        txtContent.setText("");
        SpannableString content = new SpannableString(boomInfo.MineUserNickName);
        content.setSpan(new ForegroundColorSpan(mHolder.itemView.getContext().getResources().getColor(R.color.content_yellow)), 0, boomInfo.MineUserNickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtContent.append(boomInfo.CreateTime + "踩到了");
        txtContent.append(content);
        txtContent.append("的雷");
        mHolder.setText(R.id.txt_address, "地址:" + boomInfo.Address);
        mHolder.setImage(R.id.img_photo, R.mipmap.record_boom_normal);
        mHolder.setOnClick(R.id.btn_detial, position);
    }

    @Override
    public int getLayout() {
        return R.layout.my_get_record;
    }

}
