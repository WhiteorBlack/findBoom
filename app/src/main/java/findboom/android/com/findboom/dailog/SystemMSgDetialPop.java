package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/12/2.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/12/2
 * TODO:
 */
public class SystemMSgDetialPop extends BasePopupwind {
    private View view;
    private LinearLayout llParent;
    private TextView txtContent;
    private TextView txtTitle;

    public SystemMSgDetialPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.system_msg_detial_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
        txtContent = (TextView) view.findViewById(R.id.txt_content);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) llParent.getLayoutParams();
        param.width = (int) (Tools.getScreenWide(context) * 0.8);
        param.height = (int) (Tools.getScreenHeight(context) * 0.7);
        llParent.setLayoutParams(param);
        txtTitle=(TextView)view.findViewById(R.id.txt_title);
        this.setContentView(view);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setContent(String msg){
        txtContent.setText(msg);
    }
    public void setTitle(String title){
        txtTitle.setText(title);
    }
}
