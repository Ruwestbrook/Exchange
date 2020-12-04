package com.russell.exchange;

import android.app.Application;

import io.branch.referral.Branch;

/**
 * @author russell
 * @description:
 * @date : 2020/12/1 20:27
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
    }
}
