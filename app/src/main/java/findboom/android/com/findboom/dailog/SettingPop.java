package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/3.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import findboom.android.com.findboom.R;

/**
 * author:${白曌勇} on 2016/9/3
 * TODO:
 */
public class SettingPop extends BasePopupwind implements CompoundButton.OnCheckedChangeListener {

    private Context context;
    private View view;
    private Button btnBand;
    private ImageView imgClose;
    private CheckBox chbMusic, chbSound, chbRemind;

    public SettingPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.setting_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        view.findViewById(R.id.btn_share).setOnClickListener(this);
        view.findViewById(R.id.btn_advice).setOnClickListener(this);
        view.findViewById(R.id.btn_exit).setOnClickListener(this);

        chbMusic = (CheckBox) view.findViewById(R.id.chb_music);
        chbMusic.setOnCheckedChangeListener(this);

        chbRemind = (CheckBox) view.findViewById(R.id.chb_remind);
        chbRemind.setOnCheckedChangeListener(this);

        chbSound = (CheckBox) view.findViewById(R.id.chb_sound_effect);
        chbSound.setOnCheckedChangeListener(this);
        this.setContentView(view);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_advice:
                Bundle bundle=new Bundle();
                bundle.putInt("type",0);
                popInterfacer.OnConfirm(flag,bundle);
                break;
            case R.id.btn_share:
                Bundle bundleS=new Bundle();
                bundleS.putInt("type",1);
                popInterfacer.OnConfirm(flag,bundleS);
                break;
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_exit:
                dismiss();
                popInterfacer.OnCancle(flag);
                break;
        }
    }

    public void showPop(View parent) {
        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chb_music:

                break;
            case R.id.chb_sound_effect:

                break;
            case R.id.chb_remind:

                break;

        }
    }
}
