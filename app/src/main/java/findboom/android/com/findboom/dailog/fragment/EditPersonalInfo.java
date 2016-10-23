package findboom.android.com.findboom.dailog.fragment;/**
 * Created by Administrator on 2016/9/13.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.widget.CircleImageView;

/**
 * author:${白曌勇} on 2016/9/13
 * TODO:
 */
public class EditPersonalInfo extends Fragment{
    private TextView txtName,txtAge,txtCity,txtWork,txtLevel,txtIntegra;
    private CircleImageView imgPhoto;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null)
            view=inflater.inflate(R.layout.fragment_edit_personal,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        txtAge=(TextView)view.findViewById(R.id.txt_user_age);
        txtCity=(TextView)view.findViewById(R.id.txt_user_city);
        txtIntegra=(TextView)view.findViewById(R.id.txt_user_intergra);
        txtLevel=(TextView)view.findViewById(R.id.txt_user_level);
        txtName=(TextView)view.findViewById(R.id.txt_user_name);
        txtWork=(TextView)view.findViewById(R.id.txt_user_work);
    }
}
