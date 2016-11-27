package findboom.android.com.findboom.dailog;

import android.app.Activity;
import android.content.Context;
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
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2016/10/18.
 */

public class BoomPic extends BasePopupwind {
    private View view;
    private BrokenView brokenView;
    private LinearLayout relParent;
    private BrokenTouchListener brokenTouchListener;
    private ImageView imgPic;
    private TextView txtInfo;

    public BoomPic(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.boom_pic, null);
        brokenView = BrokenView.add2Window((Activity) context);
        relParent = (LinearLayout) view.findViewById(R.id.rel_parent);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) relParent.getLayoutParams();
        params.height = (int) (Tools.getScreenWide(context) * 0.7);
        params.width = (int) (Tools.getScreenWide(context) * 0.7);
        relParent.setLayoutParams(params);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        brokenTouchListener = new BrokenTouchListener.Builder(brokenView).
                setComplexity(18).
                setBreakDuration(500).
                setFallDuration(1000).setPaint(paint).
                setCircleRiftsRadius(80).setEnableArea(view).
                build();
        imgPic = (ImageView) view.findViewById(R.id.img_pic);
        txtInfo = (TextView) view.findViewById(R.id.txt_boom_text);
        this.setContentView(view);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setText(String text) {
        txtInfo.setText(TextUtils.isEmpty(text) ? "糟糕的一天..." : text);
    }

    public void setPic(String url) {
        Glide.with(context).load(url).error(R.mipmap.boom_icon).into(imgPic);
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
        if (brokenTouchListener != null)
            brokenTouchListener.breake(view, (int) Tools.getScreenWide(context) / 2, (int) Tools.getScreenHeight(context) / 2);
        isShow = true;
    }
}
