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
import findboom.android.com.findboom.adapter.ArsenalAdapter;
import findboom.android.com.findboom.adapter.BaseRecyAdapter;
import findboom.android.com.findboom.adapter.DefenseAdapter;
import findboom.android.com.findboom.bean.Bean_UserArm;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:军火库
 */
public class ArsenalPop extends BasePopupwind {
    private Context context;
    private View view;
    private RecyclerView recyDefense;
    private List<Bean_UserArm.UserArm> defenseList;
    private ArsenalAdapter defenseAdapter;

    public ArsenalPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void setData(List<Bean_UserArm.UserArm> dataList) {
        defenseList.clear();
        defenseList.addAll(dataList);
        defenseAdapter.notifyItemChanged(0, defenseList.size() - 1);
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.arsenal_intro_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        recyDefense = (RecyclerView) view.findViewById(R.id.recy_defense);
        recyDefense.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyDefense.setItemAnimator(new DefaultItemAnimator());
        this.setContentView(view);
        defenseList = new ArrayList();
        defenseAdapter = new ArsenalAdapter(defenseList);
        defenseAdapter.setArsenal(true);
        recyDefense.setAdapter(defenseAdapter);
        defenseAdapter.setOnItemClickListener(new BaseRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });

        defenseAdapter.setOnclick(new OnClickInterface() {
            @Override
            public void onClick(View view, int position) {
//                if (defenseList.get(0).Count == 0 && defenseList.get(1).Count == 0) {
//                    Tools.toastMsg(context, "地雷已用完,请购买后使用");
//                    return;
//                }
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag,bundle);
                dismiss();
            }
        });
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
