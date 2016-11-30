package findboom.android.com.findboom.dailog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.GoldRecordDetial;
import findboom.android.com.findboom.activity.PicRecordDetial;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2016/10/18.
 */

public class BoomPic extends BasePopupwind {
    private View view;
    //    private BrokenView brokenView;
//    private BrokenTouchListener brokenTouchListener;
    private ImageView imgPic;
    private String id;

    public BoomPic(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.boom_pic, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imgPic = (ImageView) view.findViewById(R.id.img_boom);
        int wide = (int) (Tools.getScreenWide(context) * 0.35);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imgPic.getLayoutParams();
        params.width = wide;
        params.height = (int) (wide * 2.1);
        params.setMargins((int) (wide * 0.2), (int) (wide * 0.2), (int) (wide * 0.2), (int) (wide * 0.2));
        imgPic.setLayoutParams(params);
        view.findViewById(R.id.btn_detial).setOnClickListener(this);
        this.setContentView(view);
    }

    public void setText(String text) {
    }

    public void setPic(String url) {
        Glide.with(context).load(url).error(R.mipmap.boom_icon).into(imgPic);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_detial) {
            context.startActivity(new Intent(context, PicRecordDetial.class).putExtra("id", id).putExtra("isMine", false));
        }
    }

    private boolean isShow = false;
    private boolean isBoom = false;

    @Override
    public void showPop(View parent) {
        super.showPop(parent);
        new CountDownTimer(2000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (!isShow)
                    broken();
                if (millisUntilFinished < 1000 && !isBoom) {
                    FindBoomApplication.getInstance().playBreakSound();
                    isBoom = true;
                }

            }

            @Override
            public void onFinish() {
                if (isShowing())
                    dismiss();
            }
        }.start();

    }

    private void broken() {
//        if (brokenTouchListener != null)
//            brokenTouchListener.breake(view, (int) Tools.getScreenWide(context) / 2, (int) Tools.getScreenHeight(context) / 2);
//        isShow = true;
    }
}
