package com.vitalapps.bikemaps.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.vitalapps.bikemaps.R;
import com.vitalapps.bikemaps.api.VolleyRequestManager;
import com.vitalapps.bikemaps.data.models.ParkingModel;
import com.vitalapps.bikemaps.service.ServiceListener;
import com.vitalapps.bikemaps.utils.IntentUtils;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.*;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class AddParkingActivity extends ServiceBasedActivity implements ServiceListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private final static int REQUEST_CONNECTION_FAILURE_RESOLUTION = 2;

    private static final String TAG = makeLogTag("AddParking");
    private static final String EXTRA_SELECTED_TYPE = "selected_type";
    private static final String EXTRA_DESC = "description";
    private static final String EXTRA_IMAGE = "image";

    private EditText mDescription;
    private Button mPhotoButton;
    private ImageView mCapturedPhoto;
    private Spinner mTypeSpinner;
    private Bitmap mPhotoBitmap;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);
        mTypeSpinner = (Spinner) findViewById(R.id.s_parking_type);
        mTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.parking_types)));
        mCapturedPhoto = (ImageView) findViewById(R.id.iv_captured_photo);
        mPhotoButton = (Button) findViewById(R.id.btn_make_photo);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = IntentUtils.photoCapture();
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        mDescription = (EditText) findViewById(R.id.ed_description);
        mLocationClient = new LocationClient(this, this, this);
        if (savedInstanceState != null) {
            mPhotoBitmap = savedInstanceState.getParcelable(EXTRA_IMAGE);
            mCapturedPhoto.setImageBitmap(mPhotoBitmap);
            mDescription.setText(savedInstanceState.getString(EXTRA_DESC));
            mTypeSpinner.setSelection(savedInstanceState.getInt(EXTRA_SELECTED_TYPE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_parking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            ParkingModel parkingModel = new ParkingModel();
            parkingModel.setParkingDescription(mDescription.getText().toString().trim());
            parkingModel.setParkingType(mTypeSpinner.getSelectedItemPosition());
            Location location = getLocation();
            if (location != null) {
                parkingModel.setParkingLat(Double.toString(location.getLatitude()));
                parkingModel.setParkingLng(Double.toString(location.getLongitude()));
            } else {
                parkingModel.setParkingLat("0");
                parkingModel.setParkingLng("0");
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("Type", Integer.toString(parkingModel.getParkingType() + 1));
            params.put("CityID", "1");
            params.put("PhotoUrl", "qweqw");
            params.put("Lat", parkingModel.getParkingLat());
            params.put("Lng", parkingModel.getParkingLng());
            params.put("Address", "qweqweqwe");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); 
            builder.addBinaryBody("", byteArray);
            HttpEntity entity = builder.build();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
				entity.writeTo(byteArrayOutputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            

            VolleyRequestManager.getInstance().doVolleyRequest().uploadFile(new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    LOGD(TAG, "OK pic " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LOGD(TAG, "onErrorResponse pic ");
                }
            }, byteArrayOutputStream.toByteArray(), entity.getContentType().getValue());

//            VolleyRequestManager.getInstance().doVolleyRequest().postParking(new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    LOGD(TAG, "OK " + response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    LOGD(TAG, "onErrorResponse");
//                }
//            }, params);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, REQUEST_CONNECTION_FAILURE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                // Thrown if Google Play services canceled the original PendingIntent
                e.printStackTrace();
            }
        } else {
            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SELECTED_TYPE, mTypeSpinner.getSelectedItemPosition());
        outState.putString(EXTRA_DESC, mDescription.getText().toString().trim());
        outState.putParcelable(EXTRA_IMAGE, mPhotoBitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mPhotoBitmap = (Bitmap) extras.get("data");
            mCapturedPhoto.setImageBitmap(mPhotoBitmap);
        } else if (requestCode == REQUEST_CONNECTION_FAILURE_RESOLUTION) {
            switch (resultCode) {
                // If Google Play services resolved the problem
                case RESULT_OK:
                    LOGD(TAG, getString(R.string.resolved));
                    // Re-try get location
                    break;
                // If any other result was returned by Google Play services
                default:
                    // Log the result
                    LOGD(TAG, getString(R.string.no_resolution));
                    break;
            }
        }
    }

    private Location getLocation() {
        if (servicesConnected()) {
            return mLocationClient.getLastLocation();
        }
        return null;
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            LOGD(TAG, getString(R.string.play_services_available));
            return true;
        } else {
            // Google Play services was not available for some reason
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getFragmentManager(), TAG);
            }
            return false;
        }
    }

    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, REQUEST_CONNECTION_FAILURE_RESOLUTION);
        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getFragmentManager(), TAG);
        }
    }

    @Override
    public ServiceListener getServiceListener() {
        return this;
    }

    @Override
    public void onProcessFinished(int processId, Bundle args) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }
}
