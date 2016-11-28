package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.GoldRecordDetial;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.RadiusImageView;

/**
 * Created by Administrator on 2016/10/18.
 */

public class BoomGold extends BasePopupwind {
    private View view;
    //    private BrokenView brokenView;
//    private BrokenTouchListener brokenTouchListener;
    private RadiusImageView imgPic;
    private TextView txtInfo;
    private String id;

    public BoomGold(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.boom_gold, null);
//        brokenView = BrokenView.add2Window((Activity) context);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        brokenTouchListener = new BrokenTouchListener.Builder(brokenView).
//                setComplexity(18).
//                setBreakDuration(500).
//                setFallDuration(1000).setPaint(paint).
//                setCircleRiftsRadius(80).setEnableArea(view).
//                build();
        view.findViewById(R.id.btn_detial).setOnClickListener(this);
        imgPic = (RadiusImageView) view.findViewById(R.id.img_pic);
        imgPic.setType(RadiusImageView.TYPE_ROUND);
        imgPic.setBorderRadius(20);
        int wide= (int) (Tools.getScreenWide(context)*0.5);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) imgPic.getLayoutParams();
        params.width=wide;
        params.height=wide*16/9;
        imgPic.setLayoutParams(params);
        txtInfo = (TextView) view.findViewById(R.id.txt_info);
        this.setContentView(view);
    }

    public void setText(String text) {
        txtInfo.setText(text);
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
            context.startActivity(new Intent(context, GoldRecordDetial.class).putExtra("id", id).putExtra("isMine", false));
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
//                if (!isShow)
//                    broken();
//                if (millisUntilFinished < 1000 && !isBoom) {
//                    FindBoomApplication.getInstance().playBreakSound();
//                    isBoom = true;
//                }

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
