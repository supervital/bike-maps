package com.vitalapps.bikemaps.screens;

import android.os.Bundle;

import com.vitalapps.bikemaps.R;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;

public class ExampleActivity extends ServiceBasedActivity {

	private static final String TAG = "ExampleActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		findViewById(R.id.btn_start_service).setOnClickListener(
//				new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						LOGD(TAG, mClassHash + "Start Load");
//						int processId = getApp().getServiceHelper()
//								.exampleProcess();
//						addListenerToQueue(processId);
//						mBinder.addServiceListener(processId,
//								ExampleActivity.this);
//					}
//				});
	}

	@Override
	public void onTaskFinished(int taskKey, Bundle args) {
		LOGD(TAG, mClassHash + "onTaskFinished");
//		((TextView) findViewById(R.id.tv_status)).setText("Process done id - "
//				+ Integer.toString(taskKey));
	}
}
