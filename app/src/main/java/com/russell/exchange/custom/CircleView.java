package com.russell.exchange.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.russell.exchange.R;


public class CircleView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = "CircleView";
    private int width;
    private int height;
    private int type=0;
    private int radius;
    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 相同点：返回获取某个dimen的值，如果dimen单位是dp或sp，则需要将其乘以density（屏幕密度）；如果单位是px，则不用。
     *
     * 不同点：
     * getDimension：返回类型为float，
     * getDimensionPixelSize：返回类型为int，由浮点型转成整型时，采用四舍五入原则。 
     * getDimensionPixelOffset：返回类型为int，由浮点型转成整型时，原则是忽略小数点部分。
     */

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        type=array.getInt(R.styleable.CircleView_circleType,0);
        radius=array.getDimensionPixelSize(R.styleable.CircleView_circleRadius,20);
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=getMeasuredWidth();
        height=getMeasuredHeight();
        if(type!=2){
            int x=Math.min(width,height);
            width=x;
            height=x;
            setMeasuredDimension(width,height);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        Bitmap bitmap=getBitmap();
        Paint mPaint=new Paint();
        BitmapShader shader=new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
        Matrix mMatrix = new Matrix();
        int w=bitmap.getWidth();
        int h=bitmap.getHeight();
        mMatrix.setScale(width>w?w*1.0f/width:width*1.0f/w, height>h?h*1.0f/height:height*1.0f/h);
        shader.setLocalMatrix(mMatrix);
        mPaint.setShader(shader);
        if(type==2){
            canvas.drawRoundRect(new RectF(0,0,width,height),radius,radius,mPaint);
        }else {
            canvas.drawCircle(getWidth()/2, getWidth()/2, getWidth()/2, mPaint);
        }
    }

    private Bitmap getBitmap() {
        Drawable drawable=getDrawable();
        if(drawable instanceof BitmapDrawable)
            return  ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
