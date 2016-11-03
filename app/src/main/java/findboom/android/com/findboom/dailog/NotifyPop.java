package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/10/30.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
    private ImageView imgClose;

    public NotifyPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.notify_pop, null);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        txtInfo = (TextView) view.findViewById(R.id.txt_notify);
        imgClose = (ImageView) view.findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
        imgClose.setVisibility(View.INVISIBLE);
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

    public void setNotify(int resID) {
        txtInfo.setText(context.getResources().getString(resID));
    }

    public void visiableClose() {
        imgClose.setVisibility(View.VISIBLE);
    }

    public void setNotify(String content) {
        txtInfo.setText(content);
    }

    public void invisiableChb() {
        checkBox.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, new Bundle());
                dismiss();
                break;
            case R.id.img_close:
                if (popInterfacer != null)
                    popInterfacer.OnCancle(flag);
                dismiss();
                break;
        }
    }
}
