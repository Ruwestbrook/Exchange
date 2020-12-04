package com.russell.exchange.navigation;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.russell.exchange.R;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationBar extends LinearLayout implements View.OnClickListener {
    List<NavigationItem> items;

    /*
        选中时的颜色
     */
    private int chooseColor;

    /*
        未选中时的颜色
     */
    private int normalColor;

    /*
        当前选中的序号
     */
    private int chooseItem=0;

    private int imageTop=10;

    private int textTop=10;

    private boolean isPrepare=false;

    private int imageSize=0;

    private int imageWidth=0;

    private int imageHeight=0;

    private int textSize=0;

    private  NavigationListener navigationListener;


    private AppCompatActivity activity;

    public BottomNavigationBar(Context context) {
        this(context,null);
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);
        items=new ArrayList<>();



       float scale=context.getResources().getDisplayMetrics().scaledDensity;

       float density=context.getResources().getDisplayMetrics().density;

        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.BottomNavigationBar);

        chooseColor=array.getColor(R.styleable.BottomNavigationBar_choose_color, Color.BLACK);

        normalColor=array.getColor(R.styleable.BottomNavigationBar_normal_color, Color.WHITE);

        textSize= (int) array.getDimension(R.styleable.BottomNavigationBar_text_size,12*density);

        imageSize=(int) array.getDimension(R.styleable.BottomNavigationBar_image_size,0);

        imageWidth=(int) array.getDimension(R.styleable.BottomNavigationBar_image_width,0);

        imageHeight=(int) array.getDimension(R.styleable.BottomNavigationBar_image_height,0);

        imageTop=(int) array.getDimension(R.styleable.BottomNavigationBar_image_top,5);

        textTop=(int) array.getDimension(R.styleable.BottomNavigationBar_text_top,5);

        textSize= (int) ((int) (textSize/density+0.5)*scale);

        array.recycle();




    }



    public  void addItem(String title, int normal, int choose, Fragment fragment){
        NavigationItem item=new NavigationItem();
        item.setFragment(fragment);
        item.setTitle(title);
        item.setChooseDrawable(choose);
        item.setNormalDrawable(normal);
        addItem(item);
    }

    public void addItem(NavigationItem item) {

        items.add(item);
    }


    public void addItems(List<NavigationItem> item ) {
        items.addAll(item);
    }

    /*
     全部完成时调用
     */
    public void prepare(AppCompatActivity prepareActivity, int containerViewId){

        removeAllViews();

        activity=prepareActivity;

        FragmentManager fragmentManager=activity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();



        for (int i = 0; i < items.size(); i++) {

            NavigationItem item=items.get(i);

            fragmentTransaction.add(containerViewId,item.getFragment());

            final LinearLayout layout=new LinearLayout(getContext());
            layout.setOrientation(VERTICAL);
            LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            layoutParams.weight=1;

            ImageView imageView=new ImageView(getContext());
            item.setImageView(imageView);
            LayoutParams imageLayoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if(imageSize!=0){
                    imageLayoutParams.height=imageSize;
                    imageLayoutParams.width=imageSize;
            }else {

                if(imageHeight !=0 || imageWidth!=0){
                    imageLayoutParams.height=imageHeight;
                    imageLayoutParams.width=imageWidth;
                }
            }

            imageLayoutParams.topMargin=imageTop;
            imageLayoutParams.gravity= Gravity.CENTER;
            layout.addView(imageView,imageLayoutParams);


            TextView textView=new TextView(getContext());
            item.setTextView(textView);
            LayoutParams textLayoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textLayoutParams.gravity = Gravity.CENTER;
            textLayoutParams.topMargin = textTop;

            textView.setText(item.getTitle());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            layout.addView(textView,textLayoutParams);

            if(i==0){
                fragmentTransaction.show(item.getFragment());
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(),item.getChooseDrawable()));
                textView.setTextColor(chooseColor);
            }else {
                fragmentTransaction.hide(item.getFragment());
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(),item.getNormalDrawable()));
                textView.setTextColor(normalColor);
            }

            layout.setTag(i);
            layout.setOnClickListener(this);


            addView(layout,layoutParams);
        }

        fragmentTransaction.commit();

    }

    private static final String TAG = "BottomNavigationBar";

    @Override
    public void onClick(View v) {


        int lastItem=chooseItem;
        Fragment lastFragment=null;

        Fragment nowFragment=null;

        chooseItem= (int) v.getTag();


        Log.d(TAG, "onClick: chooseItem="+chooseItem);


        for (int j = 0; j < items.size(); j++) {

            NavigationItem item=items.get(j);


            if(j==chooseItem){
                nowFragment=item.getFragment();
                item.getImageView().setImageDrawable(ContextCompat.getDrawable(getContext(),item.getChooseDrawable()));
                item.getTextView().setTextColor(chooseColor);
            }else {
                item.getImageView().setImageDrawable(ContextCompat.getDrawable(getContext(),item.getNormalDrawable()));
                item.getTextView().setTextColor(normalColor);
            }


            if(j==lastItem){
                lastFragment=item.getFragment();
            }
        }


        if(lastFragment==null){
            return;
        }
        if(nowFragment==null){
            return;
        }
        if(navigationListener!=null){
            navigationListener.onPageChange(chooseItem,nowFragment,lastItem,lastFragment);
        }

        FragmentManager fragmentManager=activity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

        fragmentTransaction.hide(lastFragment).show(nowFragment).commit();
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }
}
