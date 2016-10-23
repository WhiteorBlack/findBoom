package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.ViewPagerAdapter;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.CircleImageView;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:编辑个人资料
 */
public class PersonalInfo extends BasePopupwind {
    private Context context;
    private View view;

    //个人资料
    private TextView txtCity, txtLevel, txtIntegra;
    private CircleImageView imgPhoto;
    private EditText txtName, txtAge, txtWork;

    public PersonalInfo(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.personal_info_pop, null);
        txtAge = (EditText) view.findViewById(R.id.txt_user_age);
        txtCity = (TextView) view.findViewById(R.id.txt_user_city);
        txtIntegra = (TextView) view.findViewById(R.id.txt_user_intergra);
        txtLevel = (TextView) view.findViewById(R.id.txt_user_level);
        txtName = (EditText) view.findViewById(R.id.txt_user_name);
        txtWork = (EditText) view.findViewById(R.id.txt_user_work);
        imgPhoto = (CircleImageView) view.findViewById(R.id.img_user_photo);
        view.findViewById(R.id.btn_edit_info).setOnClickListener(this);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        imgPhoto = (CircleImageView) view.findViewById(R.id.img_user_photo);
        imgPhoto.setOnClickListener(this);
        this.setContentView(view);
    }

    public void setUserData() {
        Bean_UserInfo.GameUser userInfo = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
        if (userInfo != null) {
            txtAge.setText(TextUtils.isEmpty(userInfo.Age) ? "0" : userInfo.Age); //暂时没有该字段
            txtIntegra.setText(TextUtils.isEmpty(userInfo.UserScore) ? "0" : userInfo.UserScore);
            txtLevel.setText(TextUtils.isEmpty(userInfo.UserLevel) ? "0" : userInfo.UserLevel);
            txtName.setText(TextUtils.isEmpty(userInfo.NickName) ? AppPrefrence.getUserName(context) : userInfo.NickName);
            txtWork.setText(TextUtils.isEmpty(userInfo.Profession) ? "自由" : userInfo.Profession);
            if (!TextUtils.isEmpty(userInfo.Avatar))
                setPhoto(userInfo.Avatar);
        }
    }

    public void setPhoto(String path) {
        Glide.with(context).load(path).into(imgPhoto);
    }

    public void setCity(String city) {
        txtCity.setText(city);
    }

    public void setInfo(String age, String name, String city, String pro, String integra, String level) {
        txtAge.setText(TextUtils.isEmpty(age) ? "0" : age);
        txtCity.setText(TextUtils.isEmpty(city) ? "" : city);
        txtIntegra.setText(TextUtils.isEmpty(integra) ? "0" : integra);
        txtLevel.setText(TextUtils.isEmpty(level) ? "0" : level);
        txtName.setText(TextUtils.isEmpty(name) ? "" : name);
        txtWork.setText(TextUtils.isEmpty(pro) ? "" : pro);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_edit_info:
                Bundle bundle = new Bundle();
                bundle.putString("age", txtAge.getText().toString());
                bundle.putString("name", txtName.getText().toString());
                bundle.putString("pro", txtWork.getText().toString());
                Bean_UserInfo.GameUser user = new Bean_UserInfo.GameUser();
                user.Age = txtAge.getText().toString();
                user.NickName = txtName.getText().toString();
                user.Profession = txtWork.getText().toString();
                BoomDBManager.getInstance().setUserData(user);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
                break;
            case R.id.img_user_photo:
                if (popInterfacer != null)
                    popInterfacer.OnCancle(flag);
                break;
        }
    }

}
