package com.tools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.tools.R;


/**
 * 自定义ImageView
 * 1.支持圆形圆角
 * 2.圆角大小设置
 */
public class MyImageView extends AppCompatImageView {
    private Paint paint;
    private Paint paintBorder;
    private Bitmap mSrcBitmap;

    private boolean mIsCircle;//是否是圆形
    private float mRadius;// 圆角的弧度

    public MyImageView(final Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.customImageViewStyle);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.MyImageView, defStyle, 0);
        mRadius = ta.getDimension(R.styleable.MyImageView_radius, 0);
        mIsCircle = ta.getBoolean(R.styleable.MyImageView_circle, false);
        int srcResource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        if (srcResource != 0)
            mSrcBitmap = BitmapFactory.decodeResource(getResources(), srcResource);
        ta.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        int height = canvas.getHeight() - getPaddingTop() - getPaddingBottom();
        Bitmap image = drawableToBitmap(getDrawable());
        if (mIsCircle) {
            Bitmap reSizeImage = reSizeImageC(image, width, height);
            canvas.drawBitmap(createCircleImage(reSizeImage, width, height), getPaddingLeft(), getPaddingTop(), null);

        } else {

            Bitmap reSizeImage = reSizeImage(image, width, height);
            canvas.drawBitmap(createRoundImage(reSizeImage, width, height),
                    getPaddingLeft(), getPaddingTop(), null);
        }
    }

    /**
     * 画圆角
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private Bitmap createRoundImage(Bitmap source, int width, int height) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        // 核心代码取两个图片的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 画圆
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int width, int height) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2,
                paint);
        // 核心代码取两个图片的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, (width - source.getWidth()) / 2,
                (height - source.getHeight()) / 2, paint);
        return target;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            if (mSrcBitmap != null) {
                return mSrcBitmap;
            } else {
                return null;
            }
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap reSizeImage(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算出缩放比
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 矩阵缩放bitmap
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap reSizeImageC(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int x = (newWidth - width) / 2;
        int y = (newHeight - height) / 2;
        if (x > 0 && y > 0) {
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, null, true);
        }

        float scale = 1;

        if (width > height) {
            // 按照宽度进行等比缩放
            scale = ((float) newWidth) / width;

        } else {
            // 按照高度进行等比缩放
            // 计算出缩放比
            scale = ((float) newHeight) / height;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
