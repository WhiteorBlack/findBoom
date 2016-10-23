package findboom.android.com.findboom.dailog.fragment;/**
 * Created by Administrator on 2016/9/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import findboom.android.com.findboom.R;

/**
 * author:${白曌勇} on 2016/9/16
 * TODO:
 */
public class AccountFragment extends Fragment {
    private View view;
    private TextView txtRed, txtAccount;
    private Button btnApply;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_account_info, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        txtAccount = (TextView) view.findViewById(R.id.txt_account_fee);
        txtRed = (TextView) view.findViewById(R.id.txt_account_red);

        btnApply = (Button) view.findViewById(R.id.btn_convert_money);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
