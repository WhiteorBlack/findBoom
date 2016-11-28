package findboom.android.com.findboom.dailog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.MyRecordDetial;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2016/10/18.
 */

public class BoomText extends BasePopupwind {
    private View view;
//    private BrokenView brokenView;
//    private BrokenTouchListener brokenTouchListener;
    private FrameLayout flParent;
    private TextView txtInfo;
    private String id;

    public BoomText(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.boom_text, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        txtInfo=(TextView)view.findViewById(R.id.txt_info);
        view.findViewById(R.id.btn_detial).setOnClickListener(this);
        flParent=(FrameLayout)view.findViewById(R.id.fl_parent);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) flParent.getLayoutParams();
        params.width= (int) (Tools.getScreenWide(context)*0.7);
        params.height= (int) (Tools.getScreenWide(context)*0.7/1.2);
        flParent.setLayoutParams(params);
//        brokenView = BrokenView.add2Window((Activity) context);
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        brokenTouchListener = new BrokenTouchListener.Builder(brokenView).
//                setComplexity(18).
//                setBreakDuration(500).
//                setFallDuration(1000).setPaint(paint).
//                setCircleRiftsRadius(80).setEnableArea(view).
//                build();
        this.setContentView(view);
    }

    public void setText(String text) {
        txtInfo.setText(text);
    }

    public void setId(String id){
        this.id=id;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()==R.id.btn_detial){
            context.startActivity(new Intent(context, MyRecordDetial.class).
                    putExtra("id", id).putExtra("isMine", false));
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
