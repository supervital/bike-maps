package com.vitalapps.bikemaps.screens;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.vitalapps.bikemaps.R;

public class MainActivity extends BaseActivity {

    private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";

    private boolean mIsResumed = false;
    private boolean mUserSkippedLogin = false;
    private UiLifecycleHelper mUiHelper;
    private Session.StatusCallback mCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUserSkippedLogin = savedInstanceState.getBoolean(USER_SKIPPED_LOGIN_KEY);
        }
        mUiHelper = new UiLifecycleHelper(this, mCallback);
        mUiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.f_user));
//        fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.f_splash));
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUiHelper.onResume();
        mIsResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mUiHelper.onPause();
        mIsResumed = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiHelper.onSaveInstanceState(outState);
        outState.putBoolean(USER_SKIPPED_LOGIN_KEY, mUserSkippedLogin);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (mIsResumed) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment splashFragment = fragmentManager.findFragmentById(R.id.f_splash);
            Fragment userFragment = fragmentManager.findFragmentById(R.id.f_user);
            if (state.equals(SessionState.OPENED)) {
                showFragment(splashFragment, userFragment, false);
            } else if (state.isClosed()) {
                showFragment(userFragment, splashFragment, false);
            }
        }
    }

    private void showFragment(Fragment showFragment, Fragment hideFragment, boolean addToBackStack) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.show(showFragment);
        transaction.hide(hideFragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
