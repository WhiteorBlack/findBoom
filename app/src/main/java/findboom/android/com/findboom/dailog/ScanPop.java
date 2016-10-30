package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.DefenseAdapter;
import findboom.android.com.findboom.adapter.ScanAdapter;
import findboom.android.com.findboom.bean.Bean_UserArm;
import findboom.android.com.findboom.interfacer.OnClickInterface;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:
 */
public class ScanPop extends BasePopupwind {
    private Context context;
    private View view;
    private RecyclerView recyDefense;
    private List defenseList;
    private ScanAdapter defenseAdapter;

    public ScanPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.scan_intro_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        recyDefense = (RecyclerView) view.findViewById(R.id.recy_defense);
        recyDefense.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyDefense.setItemAnimator(new DefaultItemAnimator());
        defenseList = new ArrayList();
        defenseAdapter = new ScanAdapter(defenseList);
        recyDefense.setAdapter(defenseAdapter);
        defenseAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();

                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
                dismiss();
            }
        });
        this.setContentView(view);
    }

    public void setData(List<Bean_UserArm.UserArm> dataList) {
        defenseList.clear();
        defenseList.addAll(dataList);
        defenseAdapter.notifyItemChanged(0, defenseList.size() - 1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
        }
    }
}
