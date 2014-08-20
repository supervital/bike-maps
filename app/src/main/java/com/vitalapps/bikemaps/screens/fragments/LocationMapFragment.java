package com.vitalapps.bikemaps.screens.fragments;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vitalapps.bikemaps.api.request.ParkingRequest;
import com.vitalapps.bikemaps.data.loadres.SQLiteCursorLoader;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class LocationMapFragment extends MapFragment implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMapLoadedCallback,
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = makeLogTag("Map");


    private LocationClient mLocationClient;
    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    // Default radius
    private static final int DEFAULT_RADIUS_VALUE = 50;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleMap map = getMap();
        if (map != null) {

            map.setMyLocationEnabled(true);
            map.setOnMapLoadedCallback(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }

    @Override
    public void onMapLoaded() {
        // init parking
        LOGD(TAG, "Init parking loader");
        getLoaderManager().initLoader(SQLiteCursorLoader.LOADER_PARKING, null, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_RADIUS_VALUE));
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        LOGD(TAG, "Create parking cursor");
        return new SQLiteCursorLoader(getActivity(), "SELECT * FROM " + ParkingRequest.Parking.T_NAME, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        LOGD(TAG, "Cursor load finished");
        if (cursor != null && getMap() != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String latString = cursor.getString(cursor.getColumnIndex(ParkingRequest.Parking.CN_LAT)).trim();
                String lngString = cursor.getString(cursor.getColumnIndex(ParkingRequest.Parking.CN_LNG)).trim();
                double lat = Double.parseDouble(latString);
                double lng = Double.parseDouble(lngString);
                String parkingName = cursor.getString(cursor.getColumnIndex(ParkingRequest.Parking.CN_DESC));
                getMap().addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(parkingName));
                if (mLocationClient != null && mLocationClient.getLastLocation() != null) {
                    getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mLocationClient.getLastLocation().getLatitude(),
                                    mLocationClient.getLastLocation().getLongitude()),
                            getZoomLevel(DEFAULT_RADIUS_VALUE)
                    ));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    getActivity(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }

    private float getZoomLevel(int radius) {
        float zoomLevel = (float) (14 - Math.log(radius) / Math.log(2));
        LOGD(TAG, "Radius is " + Integer.toString(radius));
        LOGD(TAG, "Zoom Level is " + Float.toString(zoomLevel));
        return zoomLevel;
    }
}
