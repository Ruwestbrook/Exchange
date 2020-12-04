package com.russell.exchange.navigation;


import androidx.fragment.app.Fragment;

public interface NavigationListener {


    void onPageChange(int nowIndex, Fragment nowFragment, int lastIndex, Fragment lastFragment);

}
