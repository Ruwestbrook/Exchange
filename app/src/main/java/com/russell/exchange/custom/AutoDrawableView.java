package com.russell.exchange.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.russell.exchange.R;


public class AutoDrawableView extends androidx.appcompat.widget.AppCompatTextView {
    public AutoDrawableView(Context context) {
        this(context,null);
    }

    int drawableHeight=0;
    int drawableWidth=0;

    public AutoDrawableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoDrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.AutoDrawableView);

        drawableHeight= (int) array.getDimension(R.styleable.AutoDrawableView_drawable_height,0);
        drawableWidth= (int) array.getDimension(R.styleable.AutoDrawableView_drawable_width,0);

        array.recycle();
    }

    Drawable[] newDrawables=new Drawable[4];
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables=getCompoundDrawables();
        for (int i = 0; i < drawables.length; i++) {
            Drawable drawable=drawables[i];
            if(drawable!=null){
                newDrawables[i]=getSizeDrawable(drawable);
            }else {
                newDrawables[i]=null;
            }
        }

        setCompoundDrawables(newDrawables[0],newDrawables[1],newDrawables[2],newDrawables[3]);



        super.onDraw(canvas);
    }




    private Drawable getSizeDrawable(Drawable drawable){


        if(drawableWidth==0 || drawableHeight==0){
            return drawable;
        }

        drawable.setBounds(0,0, drawableWidth,drawableHeight);

        return  drawable;

    }


}
