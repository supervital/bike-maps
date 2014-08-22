package com.vitalapps.bikemaps.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vitalapps.bikemaps.R;
import com.vitalapps.bikemaps.service.ServiceListener;
import com.vitalapps.bikemaps.utils.IntentUtils;

public class AddParking extends ServiceBasedActivity implements ServiceListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText mDescription;
    private Button mPhotoButton;
    private ImageView mCapturedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);
        Spinner parkingTypes = (Spinner) findViewById(R.id.s_parking_type);
        parkingTypes.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.parking_types)));
        parkingTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        if (savedInstanceState != null) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mCapturedPhoto.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public ServiceListener getServiceListener() {
        return this;
    }

    @Override
    public void onProcessFinished(int processId, Bundle args) {

    }
}
