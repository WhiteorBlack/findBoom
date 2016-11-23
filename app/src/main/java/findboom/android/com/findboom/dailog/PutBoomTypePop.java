package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.gallery.DepthPageTransformer;
import findboom.android.com.findboom.widget.gallery.RotateDownPageTransformer;
import findboom.android.com.findboom.widget.gallery.ViewPaperAdapter;
import findboom.android.com.findboom.widget.gallery.ZoomOutPageTransformer;

/**
 * Created by Administrator on 2016/10/31.
 */

public class PutBoomTypePop extends BasePopupwind {
    private View view;
    private ViewPager viewPager;
    private int type = 0;

    public PutBoomTypePop(Context context) {
        super(context);
        initView();

    }

    private void initData() {
        List<View> views = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(context);
        View goldView = inflater.inflate(R.layout.put_boom_type_item, null);
        ((ImageView) goldView.findViewById(R.id.img_boom)).setBackgroundResource(R.mipmap.boom);
        ((TextView) goldView.findViewById(R.id.txt_info)).setText("寻宝雷");
        goldView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        views.add(goldView);

        View imgView = inflater.inflate(R.layout.put_boom_type_item, null);
        ((ImageView) imgView.findViewById(R.id.img_boom)).setBackgroundResource(R.mipmap.boom);
        ((TextView) imgView.findViewById(R.id.txt_info)).setText("图片雷");
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        views.add(imgView);

        View commonView = inflater.inflate(R.layout.put_boom_type_item, null);
        ((ImageView) commonView.findViewById(R.id.img_boom)).setBackgroundResource(R.mipmap.boom);
        ((TextView) commonView.findViewById(R.id.txt_info)).setText("普通雷");
        commonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        views.add(commonView);

        final View textView = inflater.inflate(R.layout.put_boom_type_item, null);
        ((ImageView) textView.findViewById(R.id.img_boom)).setBackgroundResource(R.mipmap.boom);
        ((TextView) textView.findViewById(R.id.txt_info)).setText("文字雷");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });
        views.add(textView);

        View redView = inflater.inflate(R.layout.put_boom_type_item, null);
        ((ImageView) redView.findViewById(R.id.img_boom)).setBackgroundResource(R.mipmap.boom);
        ((TextView) redView.findViewById(R.id.txt_info)).setText("红包雷");
        redView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
            }
        });
        views.add(redView);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        viewPager.setOffscreenPageLimit(views.size());
        viewPager.setPageMargin(-Tools.dip2px(context, 135));
        viewPager.setAdapter(new ViewPaperAdapter(views));
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setCurrentItem(2);
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.put_boom_type_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//              switch (position){
//                  case 0:
//                      type=4;
//                      break;
//                  case 1:
//                        type=2;
//                      break;
//                  case 2:
//                        type=0;
//                      break;
//                  case 3:
//                        type=1;
//                      break;
//                  case 4:
//                        type=3;
//                      break;
//              }
                type=position;
            }
        });
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) viewPager.getLayoutParams();
        params.height= (int) (Tools.getScreenHeight(context)*0.3);
        viewPager.setLayoutParams(params);
        this.setContentView(view);
    }

    @Override
    public void showPop(View parent) {
        super.showPop(parent);
        initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_confirm) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            if (popInterfacer != null)
                popInterfacer.OnConfirm(flag, bundle);
        }

        dismiss();
    }
}
