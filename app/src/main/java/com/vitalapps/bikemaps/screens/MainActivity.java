package com.vitalapps.bikemaps.screens;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.vitalapps.bikemaps.R;
import com.vitalapps.bikemaps.data.models.UserModel;
import com.vitalapps.bikemaps.screens.fragments.LocationMapFragment;
import com.vitalapps.bikemaps.screens.fragments.UserFragment;
import com.vitalapps.bikemaps.service.AppServiceHelperImpl;
import com.vitalapps.bikemaps.service.ServiceListener;
import static com.vitalapps.bikemaps.utils.LogUtils.*;

public class MainActivity extends ServiceBasedActivity {

    private static final String TAG = makeLogTag("MainAct");
    private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";

    private boolean mIsResumed = false;
    private boolean mUserSkippedLogin = false;
    private UserFragment mUserFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getFragmentManager();
        mUserFragment = (UserFragment) fragmentManager.findFragmentById(R.id.f_user);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.hello_world, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                LOGD(TAG, "onDrawerClosed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                LOGD(TAG, "onDrawerOpened");
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
    public ServiceListener getServiceListener() {
        return new ServiceListener() {
            @Override
            public void onProcessFinished(int processId, Bundle args) {
                LOGD(TAG, "onProcessFinished id = " + Integer.toString(processId));
            }
        };
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
            // Do call
            LOGD(TAG, "start load");
            addListenerToQueue(AppServiceHelperImpl.getInstance().parkingProcess());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (mIsResumed) {
            if (state.equals(SessionState.OPENED)) {
                mDrawerLayout.openDrawer(GravityCompat.START);
                profileRequest(session);
            } else if (state.isClosed()) {
                addFragment(R.id.fl_container, new LocationMapFragment(), "", false, false);
            }
        }
    }

    private void profileRequest(final Session session) {
        // Make an API call to get user data and define a
        // new callback to handle the response.
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                UserModel userModel = new UserModel();
                                userModel.setUserId(user.getId());
                                userModel.setUserFirstName(user.getFirstName());
                                userModel.setUserLastName(user.getLastName());
                                mUserFragment.setUser(userModel);
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                }
        );
        request.executeAsync();
    }

}
