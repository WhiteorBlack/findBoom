package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/3.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.util.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.wxpay.Constants;

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
        chbMusic.setChecked(AppPrefrence.getIsBack(context));

        chbRemind = (CheckBox) view.findViewById(R.id.chb_remind);
        chbRemind.setOnCheckedChangeListener(this);
        chbRemind.setChecked(AppPrefrence.getIsPush(context));

        chbSound = (CheckBox) view.findViewById(R.id.chb_sound_effect);
        chbSound.setOnCheckedChangeListener(this);
        chbSound.setChecked(AppPrefrence.getIsBoom(context));
        this.setContentView(view);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_advice:
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                popInterfacer.OnConfirm(flag, bundle);
                break;
            case R.id.btn_share:
                Bundle bundleS = new Bundle();
                bundleS.putInt("type", 1);
                popInterfacer.OnConfirm(flag, bundleS);
//                share();
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

    IWXAPI msgApi;

    private void share() {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = CommonUntilities.SHARE_REGISTER + AppPrefrence.getUserName(context);

        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = "邀你一起扫雷";
        msg.description = "我在激战中,需要你的支援";
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_logo);
        msg.thumbData = findboom.android.com.findboom.wxpay.Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        if (msgApi == null)
            msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void showPop(View parent) {
        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Bundle bundle = new Bundle();
        switch (buttonView.getId()) {
            case R.id.chb_music:
                AppPrefrence.setIsBack(context, isChecked);
                bundle.putInt("type", 4);
                break;
            case R.id.chb_sound_effect:
                AppPrefrence.setIsBoom(context, isChecked);
                bundle.putInt("type", 2);
                break;
            case R.id.chb_remind:
                AppPrefrence.setIsPush(context, isChecked);
                bundle.putInt("type", 3);
                break;

        }
        if (popInterfacer!=null)
            popInterfacer.OnConfirm(flag,bundle);
    }
}
