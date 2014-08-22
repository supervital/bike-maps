package com.vitalapps.bikemaps.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.vitalapps.bikemaps.R;
import com.vitalapps.bikemaps.data.models.UserModel;

public class UserFragment extends BaseFragment {

    public static final String EXTRA_USER = "user";

    private UserModel mUser;
    private ProfilePictureView mProfilePictureView;
    private TextView mUserNameView;

    public UserFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mProfilePictureView = (ProfilePictureView) view.findViewById(R.id.ppv_facebook_avatar);
        mUserNameView = (TextView) view.findViewById(R.id.tv_name);
        if (savedInstanceState != null) {
            mUser = savedInstanceState.getParcelable(EXTRA_USER);
            fillUserView();
        }
        return view;
    }


    private void fillUserView() {
        if (mUser != null) {
            mProfilePictureView.setProfileId(mUser.getUserId());
            mUserNameView.setText(mUser.getUserFirstName());
        }
    }

    public void setUser(UserModel mUser) {
        this.mUser = mUser;
        fillUserView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_USER, mUser);
    }
}
