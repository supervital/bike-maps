package com.vitalapps.bikemaps.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.vitalapps.bikemaps.app.App;

public class BaseActivity extends Activity {

    public App getApp() {
        return (App) getApplication();
    }

    public void addFragment(int containerViewId, Fragment fragment, String tag, boolean addToBackStack, boolean withAnimation) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (withAnimation) {
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out);
        }
        fragmentTransaction.add(containerViewId, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }
}
