package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_UserArm;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public class ArsenalAdapter extends BaseRecyAdapter {
    public ArsenalAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    public ArsenalAdapter(List dataList) {
        super(dataList);
        this.dataList = dataList;
    }

    private boolean isArsenal = false;

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == dataList.size()-1)
            return new ViewHolderNew(LayoutInflater.from(parent.getContext()).inflate(R.layout.textview, null));
        else
            return super.onCreateViewHolder(parent, viewType);
    }

    public void setArsenal(boolean isArsenal) {
        this.isArsenal = isArsenal;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (position == dataList.size() - 1) {
            ViewHolderNew mHolder= (ViewHolderNew) holder;
            mHolder.setText(R.id.txt_info,R.string.arsenalInfo);
        } else {
            ViewHolder mHolder = (ViewHolder) holder;
            Bean_UserArm.UserArm userArm = (Bean_UserArm.UserArm) dataList.get(position);
            mHolder.setText(R.id.txt_name, "地雷" + userArm.Count + "个");
            mHolder.setImage(R.id.img_photo,R.mipmap.boom);
            if (userArm.ArmType == 0) {
                mHolder.setText(R.id.txt_intro, "(永久期限)");
                mHolder.setText(R.id.txt_statue,"永久");
            } else {
                mHolder.setText(R.id.txt_intro, "(当天使用)");
                mHolder.setText(R.id.txt_statue,"临时");
            }
            if (isArsenal)
                mHolder.itemView.findViewById(R.id.btn_use).setVisibility(View.VISIBLE);
            else mHolder.itemView.findViewById(R.id.btn_use).setVisibility(View.GONE);
            mHolder.setOnClick(R.id.btn_use, position);
        }

    }

    @Override
    public int getLayout() {
        return R.layout.defense_item;
    }

}
