package findboom.android.com.findboom.dailog;/**
 * Created by Administrator on 2016/9/10.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.FragmentAdapter;
import findboom.android.com.findboom.adapter.ViewPagerAdapter;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.dailog.fragment.AccountFragment;
import findboom.android.com.findboom.dailog.fragment.EditPersonalInfo;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.CircleImageView;

/**
 * author:${白曌勇} on 2016/9/10
 * TODO:
 */
public class PersonalCenterPop extends BasePopupwind implements ViewPager.OnPageChangeListener {
    private Context context;
    private View view;
    private ViewPager viewPager;
    private ImageView imgPersonal, imgAccount;
    private ImageView imgSelector;
    private Button btnBottom;

    private List<View> viewList;
    private View personView, accountView;
    private boolean isEdit = false;

    //账户信息
    private TextView txtRed, txtAccount;
    private Button btnApply;

    //个人资料
    private TextView txtAge, txtCity, txtWork, txtLevel, txtIntegra, txtName;
    private EditText edtName;
    private CircleImageView imgPhoto;


    public PersonalCenterPop(Context context) {
        super(context);
        this.context = context;
        initView();
        initData();
        setUserData();
    }

    private void setUserData() {
        Bean_UserInfo.GameUser userInfo = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context));
        if (userInfo != null) {
            txtAccount.setText(userInfo.UserBalance);
            txtAge.setText(TextUtils.isEmpty(userInfo.Age) ? "0" : userInfo.Age);//暂时没有该字段
            txtName.setText(TextUtils.isEmpty(userInfo.NickName) ? AppPrefrence.getUserName(context) : userInfo.NickName);
            edtName.setText(TextUtils.isEmpty(userInfo.NickName) ? AppPrefrence.getUserName(context) : userInfo.NickName);
            txtRed.setText(userInfo.RedPackBalance);
            txtWork.setText(TextUtils.isEmpty(userInfo.Profession) ? "其他" : userInfo.Profession);
            txtIntegra.setText(TextUtils.isEmpty(userInfo.UserScore) ? "0" : userInfo.UserScore);
            txtLevel.setText(TextUtils.isEmpty(userInfo.UserLevel) ? "0" : userInfo.UserLevel);
            txtCity.setText(userInfo.City);
            if (!TextUtils.isEmpty(userInfo.Avatar))
                setPhoto(userInfo.Avatar);
        }
    }

    public void setUserData(Bean_UserInfo.GameUser userInfo) {
        if (userInfo != null) {
            txtAccount.setText(userInfo.UserBalance);
            txtAge.setText(TextUtils.isEmpty(userInfo.Age) ? "0" : userInfo.Age); //暂时没有该字段
            txtName.setText(userInfo.NickName);
            edtName.setText(userInfo.NickName);
            txtRed.setText(userInfo.RedPackBalance);
            txtWork.setText(userInfo.Profession);
            txtCity.setText(userInfo.City);
            txtIntegra.setText(TextUtils.isEmpty(userInfo.UserScore) ? "0" : userInfo.UserScore);
            txtLevel.setText(TextUtils.isEmpty(userInfo.UserLevel) ? "0" : userInfo.UserLevel);
            if (!TextUtils.isEmpty(userInfo.Avatar))
                setPhoto(userInfo.Avatar);
        }
        btnBottom.setBackgroundResource(R.mipmap.btn_edit_info);
    }


    public void setPhoto(String path) {
        Glide.with(context).load(path).into(imgPhoto);
    }

    public void refreshData() {
        setUserData();
    }

    private void initData() {
        personView = LayoutInflater.from(context).inflate(R.layout.fragment_edit_personal, null);
        txtAge = (TextView) personView.findViewById(R.id.txt_user_age);
        txtAge.setOnClickListener(this);
        txtCity = (TextView) personView.findViewById(R.id.txt_user_city);
        txtIntegra = (TextView) personView.findViewById(R.id.txt_user_intergra);
        txtLevel = (TextView) personView.findViewById(R.id.txt_user_level);
        txtName = (TextView) personView.findViewById(R.id.txt_user_name);
        edtName = (EditText) personView.findViewById(R.id.edt_user_name);
        edtName.setVisibility(View.GONE);
        personView.findViewById(R.id.ll_record).setOnClickListener(this);
        txtWork = (TextView) personView.findViewById(R.id.txt_user_work);
        txtWork.setOnClickListener(this);
        imgPhoto = (CircleImageView) personView.findViewById(R.id.img_user_photo);
        imgPhoto.setOnClickListener(this);
        personView.findViewById(R.id.txt_user_city).setOnClickListener(this);
        btnBottom = (Button) personView.findViewById(R.id.btn_edit_info);
        btnBottom.setOnClickListener(this);
        btnBottom.setTag(1);

        accountView = LayoutInflater.from(context).inflate(R.layout.fragment_account_info, null);
        txtAccount = (TextView) accountView.findViewById(R.id.txt_account_fee);
        txtRed = (TextView) accountView.findViewById(R.id.txt_account_red);
        accountView.findViewById(R.id.btn_convert_money).setOnClickListener(this);
        accountView.findViewById(R.id.btn_charge_money).setOnClickListener(this);
        accountView.findViewById(R.id.btn_charge_red).setOnClickListener(this);
        if (TextUtils.isEmpty(AppPrefrence.getUserPhone(context)))
            accountView.findViewById(R.id.btn_change_pwd).setVisibility(View.GONE);
        else accountView.findViewById(R.id.btn_change_pwd).setVisibility(View.VISIBLE);
        accountView.findViewById(R.id.btn_change_pwd).setOnClickListener(this);

        viewList = new ArrayList<>();
        viewList.add(personView);
        viewList.add(accountView);
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.personal_center_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        params.height = (int) (Tools.getScreenWide(context) * 0.9);
        viewPager.setLayoutParams(params);
        imgAccount = (ImageView) view.findViewById(R.id.img_account);
        imgAccount.setOnClickListener(this);
        imgAccount.setEnabled(true);
        imgPersonal = (ImageView) view.findViewById(R.id.img_personal);
        imgPersonal.setOnClickListener(this);

        imgPersonal.setEnabled(false);
        imgSelector = (ImageView) view.findViewById(R.id.img_selector);

        this.setContentView(view);
        this.setFocuse(true);
    }

    public void setMoney(String money) {
        txtAccount.setText(money);
    }

    public void setRed(String money) {
        txtRed.setText(money);
    }

    public void setUseNmae(String name) {
        txtName.setText(name);
        edtName.setText(name);
    }

    public void setAge(String age) {
        txtAge.setText(age);
    }

    public void setWorkd(String work) {
        txtWork.setText(work);

    }

    public void setCity(String city) {
        if (TextUtils.isEmpty(city))
            return;
        txtCity.setText(city);
    }

    private String province = "";

    public void setProvince(String pro) {
        this.province = pro;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.img_account:
                viewPager.setCurrentItem(1);
                imgAccount.setEnabled(false);
                imgPersonal.setEnabled(true);
                break;
            case R.id.img_personal:
                viewPager.setCurrentItem(0);
                imgAccount.setEnabled(true);
                imgPersonal.setEnabled(false);
                break;
            case R.id.btn_edit_info:
                Bundle bundleBtn = new Bundle();
                bundleBtn.putInt("type", (int) btnBottom.getTag());

                switch ((int) btnBottom.getTag()) {
                    case 1:
                        //编辑个人信息
                        edtName.setVisibility(View.VISIBLE);
                        txtName.setVisibility(View.GONE);
                        edtName.setFocusable(true);
                        isEdit = true;
                        btnBottom.setTag(6);
                        btnBottom.setBackgroundResource(R.mipmap.btn_save_info);
                        Tools.openInput(edtName, context);
                        break;
                    case 2:
                        //创建支付密码
                        popInterfacer.OnConfirm(flag, bundleBtn);
                        break;
                    case 3:
                        //修改支付密码
                        popInterfacer.OnConfirm(flag, bundleBtn);
                        break;
                    case 6:
                        //保存个人资料

                        String name = edtName.getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            Tools.toastMsg(context, "请输入用户名");
                            return;
                        }
                        edtName.setVisibility(View.GONE);
                        txtName.setVisibility(View.VISIBLE);
                        txtName.setText(name);
                        isEdit = false;
                        bundleBtn.putString("age", txtAge.getText().toString());
                        bundleBtn.putString("name", name);
                        bundleBtn.putString("pro", txtWork.getText().toString());
                        bundleBtn.putString("city", txtCity.getText().toString());
                        bundleBtn.putString("province", province);
                        Bean_UserInfo.GameUser user = new Bean_UserInfo.GameUser();
                        user.Age = txtAge.getText().toString();
                        user.NickName = txtName.getText().toString();
                        user.Profession = txtWork.getText().toString();
                        user.Province = province;
                        user.City = txtCity.getText().toString();
                        BoomDBManager.getInstance().setUserData(user);
                        if (popInterfacer != null)
                            popInterfacer.OnConfirm(flag, bundleBtn);
                        btnBottom.setTag(1);
                        break;
                }
                break;
            case R.id.btn_convert_money:
                //提现
                Bundle bundleT = new Bundle();
                bundleT.putInt("type", 9);
                popInterfacer.OnConfirm(flag, bundleT);
                break;
            case R.id.ll_record:
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                popInterfacer.OnConfirm(flag, bundle);
                break;
            case R.id.img_photo:
                if (!isEdit)
                    return;
                if (popInterfacer != null)
                    popInterfacer.OnCancle(flag);
                break;
            case R.id.btn_charge_money://充值账号
                Bundle bundlec = new Bundle();
                bundlec.putInt("type", 4);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundlec);
                break;
            case R.id.btn_charge_red://red package
                Bundle bundler = new Bundle();
                bundler.putInt("type", 5);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundler);
                break;
            case R.id.txt_user_work:
                if (!isEdit)
                    return;
                Bundle bundleW = new Bundle();
                bundleW.putInt("type", 7);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundleW);

                break;
            case R.id.txt_user_age:
                if (!isEdit)
                    return;
                Bundle bundleA = new Bundle();
                bundleA.putInt("type", 8);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundleA);
                break;
            case R.id.img_user_photo:
                if (!isEdit)
                    return;
                Bundle bundle1P = new Bundle();
                bundle1P.putInt("type", 10);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle1P);
                break;
            case R.id.txt_user_city:
                if (!isEdit)
                    return;
                Bundle bundle1C = new Bundle();
                bundle1C.putInt("type", 11);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle1C);
                break;
            case R.id.btn_change_pwd:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", 12);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgSelector.getLayoutParams();
        if (position == 1 && positionOffsetPixels == 0) {
            return;
        }

        params.leftMargin = (int) (positionOffsetPixels * ((Tools.getScreenWide(context) - Tools.dip2px(context, 142) - imgSelector.getWidth()) / (viewPager.getWidth() - Tools.dip2px(context, 20))));
        imgSelector.setLayoutParams(params);
    }


    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            imgAccount.setEnabled(true);
            imgPersonal.setEnabled(false);
            btnBottom.setBackgroundResource(R.mipmap.btn_edit_info);
            btnBottom.setVisibility(View.VISIBLE);
            btnBottom.setTag(1);
//            llContent.setVisibility(View.GONE);
        } else {
            imgAccount.setEnabled(false);
            imgPersonal.setEnabled(true);
//            btnBottom.setVisibility(View.INVISIBLE);
        }
    }

    public void setBottomStatu(int type) {
        switch (type) {
            case 0:
                btnBottom.setBackgroundResource(R.mipmap.btn_change_pwd);
                btnBottom.setTag(3);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
