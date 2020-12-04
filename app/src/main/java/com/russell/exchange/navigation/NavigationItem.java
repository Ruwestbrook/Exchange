package com.russell.exchange.navigation;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class NavigationItem {


    private int chooseDrawable;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public int getChooseDrawable() {
        return chooseDrawable;
    }

    public void setChooseDrawable(int chooseDrawable) {
        this.chooseDrawable = chooseDrawable;
    }

    public int getNormalDrawable() {
        return normalDrawable;
    }

    public void setNormalDrawable(int normalDrawable) {
        this.normalDrawable = normalDrawable;
    }

    private int normalDrawable;



    private Fragment fragment;

    private ImageView imageView;

    private TextView textView;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

     ImageView getImageView() {
        return imageView;
    }

     void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

     TextView getTextView() {
        return textView;
    }

     void setTextView(TextView textView) {
        this.textView = textView;
    }
}
