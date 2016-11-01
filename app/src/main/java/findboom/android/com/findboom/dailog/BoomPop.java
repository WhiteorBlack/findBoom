package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;

import findboom.android.com.findboom.R;

/**
 * Created by Administrator on 2016/10/18.
 */

public class BoomPop extends BasePopupwind {
    private View view;

    public BoomPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.boom_pop, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
    }

    @Override
    public void showPop(View parent) {
        super.showPop(parent);
        new CountDownTimer(2000, 2000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (isShowing())
                    dismiss();
            }
        }.start();
    }
}
