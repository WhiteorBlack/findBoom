package findboom.android.com.findboom.dailog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2016/10/18.
 */

public class BoomPop extends BasePopupwind {
    private View view;
    private BrokenView brokenView;
    private RelativeLayout relParent;
    private BrokenTouchListener brokenTouchListener;

    public BoomPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.boom_pop, null);
        brokenView = BrokenView.add2Window((Activity) context);
        relParent = (RelativeLayout) view.findViewById(R.id.rel_parent);
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
        this.setContentView(view);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
