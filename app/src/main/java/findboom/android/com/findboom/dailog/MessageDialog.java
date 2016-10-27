package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.interfacer.PopInterfacer;

/**
 * Created by Administrator on 2016/10/27.
 */

public class MessageDialog extends PopupWindow implements View.OnClickListener {
    private View view;
    public PopInterfacer popInterfacer;
    public int flag;

    public void setPopInterfacer(PopInterfacer l, int flag) {
        this.popInterfacer = l;
        this.flag = flag;
    }

    public MessageDialog(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.message_type_pop, null);
        view.findViewById(R.id.img_system_msg).setOnClickListener(this);
        view.findViewById(R.id.img_friend).setOnClickListener(this);
        view.findViewById(R.id.ll_parent).getBackground().setAlpha(120);

        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.MsgPop);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.img_find:
                bundle.putInt("type", 1);

                break;
            case R.id.img_system_msg:
                bundle.putInt("type", 1);
                break;
        }
        if (popInterfacer != null)
            popInterfacer.OnConfirm(flag, bundle);
        dismiss();
    }

}
