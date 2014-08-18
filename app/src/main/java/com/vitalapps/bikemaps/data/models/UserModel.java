package com.vitalapps.bikemaps.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private String mUserId;
    private String mUserFirstName;
    private String mUserLastName;

    public UserModel(){}

    public UserModel(Parcel parcel) {
        mUserId = parcel.readString();
        mUserFirstName = parcel.readString();
        mUserLastName = parcel.readString();
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserFirstName() {
        return mUserFirstName;
    }

    public void setUserFirstName(String mUserFirstName) {
        this.mUserFirstName = mUserFirstName;
    }

    public String getUserLastName() {
        return mUserLastName;
    }

    public void setUserLastName(String mUserLastName) {
        this.mUserLastName = mUserLastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserId);
        dest.writeString(mUserFirstName);
        dest.writeString(mUserLastName);
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
