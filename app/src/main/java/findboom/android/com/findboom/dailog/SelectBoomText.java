package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/10/22.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.BaseRecyAdapter;
import findboom.android.com.findboom.adapter.SelectBoomAdapter;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:
 */
public class SelectBoomText extends PopupWindow {
    private View view;
    private RecyclerView recyclerView;
    private List dataList = new ArrayList();
    private SelectBoomAdapter selectAdapter;
    private Context context;
    private PopInterfacer popInterfacer;

    public void setPopInterfacer(PopInterfacer l) {
        this.popInterfacer = l;
    }


    public SelectBoomText(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.select_boom_list, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recy_type);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        params.width = Tools.dip2px(context, 150);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        if (selectAdapter == null)
            selectAdapter = new SelectBoomAdapter(dataList);
        recyclerView.setAdapter(selectAdapter);
        selectAdapter.setOnItemClickListener(new BaseRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(1, bundle);
                dismiss();
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });

        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setContentView(view);
    }

    public void setStrings(String[] data) {
//        Tools.concat(typeList,data);
        dataList.clear();
        for (int i = 0; i < data.length; i++) {
            dataList.add(data[i]);
        }
        selectAdapter.notifyDataSetChanged();
    }
}
