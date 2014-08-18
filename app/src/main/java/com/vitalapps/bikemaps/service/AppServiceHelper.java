package com.vitalapps.bikemaps.service;

public interface AppServiceHelper {

	public static final int PROCESS_EXAMPLE = 1;
    public static final int PROCESS_PARKING = 2;

    void cancelProcess(int processId);

	int exampleProcess();
    int parkingProcess();

}
