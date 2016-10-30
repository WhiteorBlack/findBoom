package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/10/30.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.AppPrefrence;

/**
 * author:${白曌勇} on 2016/10/30
 * TODO:
 */
public class NotifyPop extends BasePopupwind {
    private View view;
    private TextView txtInfo;
    private CheckBox checkBox;

    public NotifyPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.notify_pop, null);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        txtInfo = (TextView) view.findViewById(R.id.txt_notify);
        checkBox = (CheckBox) view.findViewById(R.id.chb_notify);
        checkBox.setChecked(AppPrefrence.getIsNotify(context));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppPrefrence.setIsNotify(context, isChecked);
            }
        });
        this.setContentView(view);
    }
    public void setNotify(int resID){
        txtInfo.setText(context.getResources().getString(resID));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_confirm:
                dismiss();
                break;
        }
    }
}
