package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/10/22.
 */

import android.support.v7.widget.RecyclerView;

import java.util.List;

import findboom.android.com.findboom.R;

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:
 */
public class SelectBoomTextAdapter extends BaseRecyAdapter {

    public SelectBoomTextAdapter(List dataList) {
        super(dataList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder mHoler = (ViewHolder) holder;

        mHoler.setText(R.id.txt_type, (String) dataList.get(position));
    }

    @Override
    public int getLayout() {
        return R.layout.select_boom_text_list_item;
    }

}
