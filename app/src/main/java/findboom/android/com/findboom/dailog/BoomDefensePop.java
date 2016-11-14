package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.explosion.ExplosionField;

/**
 * Created by Administrator on 2016/10/18.
 */

public class BoomDefensePop extends BasePopupwind {
    private View view;
    private ImageView imgDefense;

    public BoomDefensePop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.boom_defense_pop, null);
        imgDefense = (ImageView) view.findViewById(R.id.img_defense);
        imgDefense.setVisibility(View.GONE);
        this.setContentView(view);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    int[] start_location = new int[2];

    public void setStartLoc(int[] start) {
        this.start_location = start;

    }

    @Override
    public void showPop(View parent) {
        super.showPop(parent);
        setAnim(imgDefense, start_location);
    }

    private void setAnim(final View v, final int[] start_location) {
        imgDefense.setX(start_location[0]/2);
        imgDefense.setY(start_location[1]/2);
        final int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
        end_location[0] = (int) (Tools.getScreenWide(context) / 2);
        end_location[1] = (int) (Tools.getScreenHeight(context) / 2);

        // 计算位移
        int endX = end_location[0]- start_location[0];// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间
        imgDefense.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setY(end_location[1]);
                v.setX(end_location[0]-start_location[0]);
                v.setVisibility(View.GONE);
                ExplosionField explosionField = new ExplosionField(context);
                explosionField.explode(v);
                explosionField.setOnExplosionListener(new ExplosionField.ExplosionListener() {
                    @Override
                    public void onEnd() {
                        v.clearAnimation();
                        dismiss();
                    }
                });
            }
        });

    }

}
