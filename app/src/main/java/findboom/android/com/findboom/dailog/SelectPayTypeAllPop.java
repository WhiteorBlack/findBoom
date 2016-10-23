package findboom.android.com.findboom.dailog;
/**
 * Created by Administrator on 2016/9/6.
 */

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.Tools;

/**
 * author:${白曌勇} on 2016/9/6
 * TODO:所有支付选项,包括红包支付
 */
public class SelectPayTypeAllPop extends BasePopupwind {
    Context context;
    View view;
    private ImageView imgAlipay, imgWx, imgAccount;
    private String money;

    public SelectPayTypeAllPop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.select_pay_type_all_pop, null);
        view.findViewById(R.id.ll_alipay).setOnClickListener(this);
        view.findViewById(R.id.ll_chat).setOnClickListener(this);
        view.findViewById(R.id.btn_pay).setOnClickListener(this);
        view.findViewById(R.id.ll_account).setOnClickListener(this);
        imgAlipay = (ImageView) view.findViewById(R.id.img_alipay);
        imgWx = (ImageView) view.findViewById(R.id.img_wxpay);
        imgAccount = (ImageView) view.findViewById(R.id.img_account);
        imgAccount.setVisibility(View.INVISIBLE);
        imgWx.setVisibility(View.INVISIBLE);
        this.setContentView(view);
    }

    private int type = 1;

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_chat:
                type = 0;
                imgWx.setVisibility(View.VISIBLE);
                imgAlipay.setVisibility(View.INVISIBLE);
                imgAccount.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_alipay:
                type = 1;
                imgWx.setVisibility(View.INVISIBLE);
                imgAlipay.setVisibility(View.VISIBLE);
                imgAccount.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_account:
                type = 2;
                imgWx.setVisibility(View.INVISIBLE);
                imgAlipay.setVisibility(View.INVISIBLE);
                imgAccount.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_pay:
                bundle.putInt("type", type);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
                dismiss();
                break;
        }

    }

}
