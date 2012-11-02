package com.biciv.android.API;

public class BicivAPI_lastHourBikeStation extends BicivBase {

	public BicivAPI_lastHourBikeStation(int bikeStationID) {
		super("/station/"+bikeStationID+"/lasthour/");
	}
}
