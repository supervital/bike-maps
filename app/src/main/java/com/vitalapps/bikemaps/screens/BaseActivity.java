package com.vitalapps.bikemaps.screens;

import com.vitalapps.bikemaps.app.App;

import android.app.Activity;
import android.app.Fragment;

public class BaseActivity extends Activity {

	public App getApp() {
		return (App) getApplication();
	}

//    public void showFragment(Fragment fragment, String tag, boolean addToBackStack) {
//
//    }

}
