package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.BaseRecyAdapter;
import findboom.android.com.findboom.adapter.DefenseAdapter;
import findboom.android.com.findboom.bean.Bean_UserArm;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:
 */
public class DefensePop extends BasePopupwind {
    private Context context;
    private View view;
    private RecyclerView recyDefense;
    private List<Bean_UserArm.UserArm> defenseList;
    private DefenseAdapter defenseAdapter;
    private LinearLayout llParent;

    public DefensePop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.defense_intro_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        recyDefense = (RecyclerView) view.findViewById(R.id.recy_defense);
        recyDefense.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyDefense.setItemAnimator(new DefaultItemAnimator());
        defenseList = new ArrayList();
        defenseAdapter = new DefenseAdapter(defenseList);
        defenseAdapter.setOnItemClickListener(new BaseRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });
        recyDefense.setAdapter(defenseAdapter);
        llParent=(LinearLayout)view.findViewById(R.id.ll_parent);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) llParent.getLayoutParams();
        params.height= (int) (Tools.getScreenHeight(context)*0.6);
        llParent.setLayoutParams(params);
        this.setContentView(view);
    }

    public void setData(List<Bean_UserArm.UserArm> dataList) {
        defenseList.clear();
        defenseList.addAll(dataList);
        defenseList.add(new Bean_UserArm.UserArm());
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
