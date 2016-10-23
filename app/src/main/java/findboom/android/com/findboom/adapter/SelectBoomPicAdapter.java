package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/10/22.
 */

import android.support.v7.widget.RecyclerView;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_AllConfig;

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:
 */
public class SelectBoomPicAdapter extends BaseRecyAdapter {


    public SelectBoomPicAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHoler = (ViewHolder) holder;
        Bean_AllConfig.PicInfo picInfo = (Bean_AllConfig.PicInfo) dataList.get(position);
        mHoler.setText(R.id.txt_pic_info, picInfo.PicTitle);
        mHoler.setImage(R.id.img_pic, picInfo.PicUrl);
    }

    @Override
    public int getLayout() {
        return R.layout.select_boom_pic_item;
    }

}
