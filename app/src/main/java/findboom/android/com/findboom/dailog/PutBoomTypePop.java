package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import findboom.android.com.findboom.R;

/**
 * Created by Administrator on 2016/10/31.
 */

public class PutBoomTypePop extends BasePopupwind {
    private View view;

    public PutBoomTypePop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.put_boom_type_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        view.findViewById(R.id.btn_common).setOnClickListener(this);
        view.findViewById(R.id.btn_red).setOnClickListener(this);
        this.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.btn_common:
                bundle.putInt("type", 0);
                break;
            case R.id.btn_red:
                bundle.putInt("type", 1);
                break;
        }
        if (popInterfacer != null)
            popInterfacer.OnConfirm(flag, bundle);
        dismiss();
    }
}
