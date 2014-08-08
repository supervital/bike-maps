package com.vitalapps.bikemaps.processor;

import android.os.Parcelable;

public abstract class BaseProcessor implements Parcelable {

	public abstract void executeProcess();

	public abstract void cancelProcess();

}
