package com.vitalapps.bikemaps.service;

import android.os.Bundle;

public interface ServiceListener {
	
	void onTaskFinished(int taskKey, Bundle args);

}
