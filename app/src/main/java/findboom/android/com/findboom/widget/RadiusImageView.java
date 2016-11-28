package findboom.android.com.findboom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import findboom.android.com.findboom.R;

public class RadiusImageView extends ImageView {

    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    /**
     * 圆角大小的默认�??
     */
    private static final int BODER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大�?
     */
    private int mBorderRadius;

    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    /**
     * 圆角的半�?
     */
    private int mRadius;
    /**
     * 3x3 矩阵，主要用于缩小放�?
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形�?�?
     */
    private BitmapShader mBitmapShader;
    /**
     * view的宽�?
     */
    private int mWidth;
    private RectF mRoundRect;
    private final Paint mBorderPaint = new Paint();

    public RadiusImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }

    public RadiusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);

        mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, BODER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));// 默认�?10dp
        type = a.getInt(R.styleable.RoundImageView_Radiustype, TYPE_CIRCLE);// 默认为Circle

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            if (!(mWidth > 0)) {
                mWidth = 50;
            }
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        // Tools.debug("radius detached");
        // setImageDrawable(null);
        // System.gc();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setUpShader();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(getResources().getColor(R.color.content_yellow));
        mBorderPaint.setStrokeWidth(5);

        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mBitmapPaint);
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mBorderPaint);
        } else {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);

            // drawSomeThing(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆角图片的范�?
        if (type == TYPE_ROUND)
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal) {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader() {

        Drawable drawable = getDrawable();
        if (drawable == null) {
        }

        Bitmap bmp = drawableToBitamp(drawable);
        if (bmp != null) {
            // 将bmp作为�?色器，就是在指定区域内绘制bmp
            mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
            float scale = 1.0f;
            if (type == TYPE_CIRCLE) {
                // 拿到bitmap宽或高的小�??
                int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
                scale = mWidth * 1.0f / bSize;

            } else if (type == TYPE_ROUND) {
                // 如果图片的宽或�?�高与view的宽高不匹配，计算出�?要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；�?以我们这里取大�?�；
                scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
            }
            // shader的变换矩阵，我们这里主要用于放大或�?�缩�?
            mMatrix.setScale(scale, scale);
            // 设置变换矩阵
            mBitmapShader.setLocalMatrix(mMatrix);
            // 设置shader
            mBitmapPaint.setShader(mBitmapShader);
        }
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        try {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
        }
        return bitmap;
    }

    // @Override
    // public void setImageDrawable(Drawable drawable) {
    // // TODO Auto-generated method stub
    // super.setImageDrawable(drawable);
    // final Drawable previousDrawable = getDrawable();
    //
    // // Call super to set new Drawable
    // super.setImageDrawable(drawable);
    //
    // // Notify new Drawable that it is being displayed
    // notifyDrawable(drawable, true);
    //
    // // Notify old Drawable so it is no longer being displayed
    // notifyDrawable(previousDrawable, false);
    // }

    /**
     * Notifies the drawable that it's displayed state has changed.
     *
     * @param drawable
     * @param isDisplayed
     */
    // private static void notifyDrawable(Drawable drawable, final boolean
    // isDisplayed) {
    // if (drawable instanceof RecyclingBitmapDrawable) {
    // // The drawable is a CountingBitmapDrawable, so notify it
    // ((RecyclingBitmapDrawable) drawable).setIsDisplayed(isDisplayed);
    // } else if (drawable instanceof LayerDrawable) {
    // // The drawable is a LayerDrawable, so recurse on each layer
    // LayerDrawable layerDrawable = (LayerDrawable) drawable;
    // for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
    // notifyDrawable(layerDrawable.getDrawable(i), isDisplayed);
    // }
    // }
    // }
}
