package findboom.android.com.findboom.widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Administrator on 2016/11/9.
 */

public class RotateAnimation extends Animation {
    /**开始角度*/
    private final float mFromDegrees;
    /**结束角度*/
    private final float mToDegrees;
    /**中心坐标X轴*/
    private final float mCenterX;
    /**中心坐标Y轴*/
    private final float mCenterY;
    /**反面*/
    private final boolean mReverse;
    /**相机类*/
    private Camera mCamera;

    /**
     * 构造方法
     * @param fromDegrees 开始角度
     * @param toDegrees 结束角度
     * @param centerX 中心坐标
     * @param centerY 中心坐标
     * @param depthZ
     * @param reverse
     */
    public RotateAnimation(float fromDegrees, float toDegrees,
                           float centerX, float centerY, float depthZ, boolean reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mReverse = reverse;
    }

    /**
     * 初始化动画对象尺寸
     */
    @Override
    public void initialize(int width, int height,
                           int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation tran) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;
        final Matrix matrix = tran.getMatrix();
        camera.save();//保存状态
        if (mReverse) {
            //x,y,z距离中心轴各自的距离长度
            camera.translate(0.0f, 0.0f, 0.0f);
        } else {
            camera.translate(0.0f, 0.0f, 0.0f);
        }
        //以多少角度围绕Y轴旋转，可以围绕X，Y，Z三个方向旋转
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        //恢复保存状态
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
