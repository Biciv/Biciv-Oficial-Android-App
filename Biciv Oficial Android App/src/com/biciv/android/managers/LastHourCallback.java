package com.biciv.android.managers;

import com.biciv.android.entities.BikeStation;

public abstract class LastHourCallback {
	public abstract void call(BikeStation.LastHour bikeStationLastHour);
}
