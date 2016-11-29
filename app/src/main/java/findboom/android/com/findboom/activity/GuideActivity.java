package findboom.android.com.findboom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import findboom.android.com.findboom.BaseActivity;
import findboom.android.com.findboom.BaseFragmentActivity;
import findboom.android.com.findboom.Home;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.ViewPagerAdapter;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by 41508 on 2016/11/29.
 */

public class GuideActivity extends BaseFragmentActivity {
    private ViewPager viewPager;
    private LinearLayout llParent;
    private List<View> views;
    private ViewPagerAdapter pagerAdapter;
    private int[] guideRes = new int[]{R.mipmap.splash_day, R.mipmap.splash_night, R.mipmap.splash_rose};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        initView();
        setData();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setData() {
        for (int i = 0; i < guideRes.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.guide_imageview, null);
            ImageView imageview = (ImageView) view.findViewById(R.id.img_guide);
            Glide.with(this).load(guideRes[i]).into(imageview);
            views.add(view);
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) point.getLayoutParams();
            params.leftMargin = Tools.dip2px(context, 15);
            params.rightMargin = Tools.dip2px(context, 15);
            params.width=Tools.dip2px(context,10);
            params.height=Tools.dip2px(context,10);
            point.setLayoutParams(params);
            if (i == 0)
                point.setBackgroundResource(R.drawable.guide_point_selected);
            else point.setBackgroundResource(R.drawable.guide_point_unselect);
            llParent.addView(point);
        }
        pagerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        views = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        llParent = (LinearLayout) findViewById(R.id.ll_point_content);
        pagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < guideRes.length; i++) {

                }
            }
        });
    }

    @Override
    public void boomClick(View v) {
        super.boomClick(v);
        if (AppPrefrence.getIsLogin(this))
            startActivity(new Intent(this, Home.class));
        else
            startActivity(new Intent(this, LoginActivity.class));
        AppPrefrence.setIsFirst(this,false);
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
