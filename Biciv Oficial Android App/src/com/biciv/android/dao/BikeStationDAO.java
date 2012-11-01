package com.biciv.android.dao;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import com.biciv.android.API.BicivApi_fullBikeStations;
import com.biciv.android.activities.synchronization.BadSynchronization;
import com.biciv.android.entities.BikeStation;
import com.biciv.android.exceptions.ToastedException;
import com.biciv.android.managers.Callback;
import com.biciv.android.managers.LastHourCallback;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

//TODO
public class BikeStationDAO {

	private static HashMap<Integer, BikeStation> cachedBikeStations = null;
	private static long lastFullCacheTime = -1;

	public BikeStationDAO() {}
	
	public BikeStation getCachedBikeStation(Integer stationID) throws NotCachedBikeStation {
		if(cachedBikeStations == null)
			throw new NotCachedBikeStation(stationID);
		
		return cachedBikeStations.get(stationID);
	}
	
	public class NotCachedBikeStation extends ToastedException{
		public int stationID;
		private NotCachedBikeStation(int stationID){
			this.stationID = stationID;
		}
		@Override
		public String toString() {
			return "Not cached bike station: "+stationID;
		}
	}
	
	
	/*
	 * TODO
	 * */
	public void forceSync() throws BadSynchronization{
		try {
			String syncObject = new BicivApi_fullBikeStations().call();
		
			TypeToken<HashMap<Integer, BikeStation>> token = new TypeToken<HashMap<Integer, BikeStation>>() {};
			HashMap<Integer, BikeStation> bikeStations = new Gson().fromJson(syncObject, token.getType());
			
			cachedBikeStations = bikeStations;
			lastFullCacheTime = new Date().getTime();
			
		} catch(JsonSyntaxException e){
			throw new BadSynchronization();
		} catch (IOException e) {
			throw new BadSynchronization();
		}
	}
	
	public long getLastFullSync(){
		return lastFullCacheTime;
	}
	
	public void getLastHour(int bikeStationID, LastHourCallback onCall , Callback onError){
		String exampleLastHour = "{\"start\":\"2012/10/30 00:03:08\",\"capacity\":15,\"data\":[8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,9,9,9,9,9,9,9,9,9,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,12,12,12,12,12,12,13,13,13,13,13,13,13,13,13,13,13,13,14,14]}";		
		
		try {
			BikeStation.LastHour lastHour = new Gson().fromJson(exampleLastHour, BikeStation.LastHour.class);
			onCall.call(lastHour);
		} catch(JsonSyntaxException e){
			onError.call();
		}
	}
}
