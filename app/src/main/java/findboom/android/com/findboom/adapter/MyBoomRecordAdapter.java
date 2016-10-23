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
import findboom.android.com.findboom.bean.Bean_MyBoomRecord;
import findboom.android.com.findboom.bean.Bean_UserArm;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class MyBoomRecordAdapter extends BaseRecyAdapter {
    public MyBoomRecordAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public MyBoomRecordAdapter(List dataList) {
        super(dataList);
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHolder = (ViewHolder) holder;
        Bean_MyBoomRecord.BoomInfo boomInfo = (Bean_MyBoomRecord.BoomInfo) dataList.get(position);
        mHolder.setText(R.id.txt_name, boomInfo.MineTypeTxt);
        mHolder.setText(R.id.txt_date, boomInfo.CreateTime);
        mHolder.setText(R.id.txt_state, boomInfo.StatusTxt);
        if (boomInfo.MineType == 0) {
            mHolder.setImage(R.id.img_photo, R.mipmap.record_boom_normal);
        } else mHolder.setImage(R.id.img_photo, R.mipmap.record_boom_other);
        mHolder.setOnClick(R.id.btn_detial, position);
    }

    @Override
    public int getLayout() {
        return R.layout.my_boom_record;
    }

}
