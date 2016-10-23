package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.utils.AppPrefrence;

/**
 * Created by Administrator on 2016/10/21.
 */

public class AccountPayPop extends BasePopupwind {
    private View view;
    private TextView txtAccount, txtPay;

    public AccountPayPop(Context context) {
        super(context);
        initView();
        setUserData();
    }

    private void setUserData() {
        Bean_UserInfo.GameUser userInfo = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
        if (userInfo != null) {
            if (TextUtils.isEmpty(userInfo.UserBalance))
                account = 0f;
            else account = Float.parseFloat(userInfo.UserBalance);
            txtAccount.setText(account + " 金币");
        }
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.account_pay_pop, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        txtAccount = (TextView) view.findViewById(R.id.txt_account_money);
        txtPay = (TextView) view.findViewById(R.id.txt_pay_money);

        view.findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popInterfacer != null) {
                    if (money > account)
                        popInterfacer.OnConfirm(flag, null);
                    else popInterfacer.OnConfirm(flag, new Bundle());
                }
                dismiss();
            }
        });
        this.setContentView(view);
    }

    private float money, account;

    public void setMoney(float money) {
        this.money = money;
        txtPay.setText(money + " 金币");
    }
}
