package com.vitalapps.bikemaps.data.models;

import android.provider.BaseColumns;

import com.vitalapps.bikemaps.utils.JSONUtils;

import org.json.JSONObject;

public class ParkingModel implements BaseColumns {

    public static final String T_NAME = "parking";
    public static final String CN_ID = "parkingid";
    public static final String CN_TYPE = "type";
    public static final String CN_LNG = "lng";
    public static final String CN_LAT = "lat";
    public static final String CN_IMG_URL = "photourl";
    public static final String CN_DESC = "description";

    public static final String TABLE_PARKING = "CREATE TABLE "
            + T_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CN_ID + " INTEGER,"
            + CN_TYPE + " INTEGER,"
            + CN_LNG + " TEXT,"
            + CN_LAT + " TEXT,"
            + CN_IMG_URL + " TEXT,"
            + CN_DESC + " TEXT);";

    private JSONObject jsonObject;
    private int mId;
    private int mType;
    private String mLat;
    private String mLng;
    private String mPhotoUrl;
    private String mDescription;

    public ParkingModel(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public ParkingModel() {
    }

    public int getParkingId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getParkingType() {
        return mType;
    }

    public void setParkingType(int mType) {
        this.mType = mType;
    }

    public String getParkingLat() {
        return mLat;
    }

    public void setParkingLat(String mLat) {
        this.mLat = mLat;
    }

    public String getParkingLng() {
        return mLng;
    }

    public void setParkingLng(String mLng) {
        this.mLng = mLng;
    }

    public String getParkingPhotoUrl() {
        return mPhotoUrl;
    }

    public void setParkingPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getParkingDescription() {
        return mDescription;
    }

    public void setParkingDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getId() {
        return JSONUtils.getInt(jsonObject, "ID");
    }

    public int getType() {
        return JSONUtils.getInt(jsonObject, "Type");
    }

    public String getLat() {
        return JSONUtils.getString(jsonObject, "Lat");
    }

    public String getLng() {
        return JSONUtils.getString(jsonObject, "Lng");
    }

    public String getPhotoUrl() {
        return JSONUtils.getString(jsonObject, "FullPhotoUrl");
    }

    public String getDescription() {
        return JSONUtils.getString(jsonObject, "Description");
    }
}