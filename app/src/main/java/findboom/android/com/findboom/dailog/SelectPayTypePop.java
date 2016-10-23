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
 * TODO:
 */
public class SelectPayTypePop extends BasePopupwind {
    Context context;
    View view;
    private EditText edtMoney;
    private ImageView imgAlipay, imgWx;
    private String money;

    public SelectPayTypePop(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.select_pay_typepop, null);
        view.findViewById(R.id.ll_alipay).setOnClickListener(this);
        view.findViewById(R.id.ll_chat).setOnClickListener(this);
        view.findViewById(R.id.btn_pay).setOnClickListener(this);
        imgAlipay = (ImageView) view.findViewById(R.id.img_alipay);
        edtMoney = (EditText) view.findViewById(R.id.editText);
        imgWx = (ImageView) view.findViewById(R.id.img_wxpay);
        imgWx.setVisibility(View.INVISIBLE);
        this.setContentView(view);
    }

    private int type = 1;

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_close:

                break;
            case R.id.ll_chat:
                bundle.putInt("type", 0);
                type = 0;
                imgWx.setVisibility(View.VISIBLE);
                imgAlipay.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_alipay:
                bundle.putInt("type", 1);
                type = 1;
                imgWx.setVisibility(View.INVISIBLE);
                imgAlipay.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_pay:
                money = edtMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    Tools.toastMsg(context, "请输入要充值的金额");
                    return;
                }
                bundle.putInt("type", type);
                bundle.putFloat("money", Float.parseFloat(money));
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
                dismiss();
                break;
        }
//        if (popInterfacer != null)
//            popInterfacer.OnConfirm(flag, bundle);

    }

}
