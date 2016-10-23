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

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:
 */
public class SelectBoomType extends PopupWindow {
    private View view;
    private RecyclerView recyclerView;
    private List typeList = new ArrayList();
    //    private String[] typeList;
    private SelectBoomAdapter selectAdapter;
    private Context context;
    private PopInterfacer popInterfacer;

    public void setPopInterfacer(PopInterfacer l) {
        this.popInterfacer = l;
    }


    public SelectBoomType(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.select_boom_list, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recy_type);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        typeList.add("普通雷");
        typeList.add("文字雷");
        typeList.add("图片雷");
        if (selectAdapter == null)
            selectAdapter = new SelectBoomAdapter(typeList);
        recyclerView.setAdapter(selectAdapter);
        selectAdapter.notifyDataSetChanged();
        selectAdapter.setOnItemClickListener(new BaseRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(0, bundle);
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
}
