package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/10/6.
 */

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import findboom.android.com.findboom.R;

/**
 * author:${白曌勇} on 2016/10/6
 * TODO:
 */
public class GetRecordPop extends BasePopupwind {
    private View view;
    private TextView txtRecord, txtNotify;

    public GetRecordPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.get_record_pop, null);
        txtRecord = (TextView) view.findViewById(R.id.txt_record);
        txtNotify = (TextView) view.findViewById(R.id.txt_notify);
        this.setContentView(view);
    }

    public void setInfo(String record, String notify) {
        if (!TextUtils.isEmpty(notify))
            txtNotify.setText(notify);
        txtRecord.setText(record);
    }

    @Override
    public void showPop(View parent) {
        super.showPop(parent);
        new CountDownTimer(1000 * 2, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dismiss();
            }
        }.start();
    }
}
