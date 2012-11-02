package com.biciv.android.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.biciv.android.API.BicivAPI_lastHourBikeStation;
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
	private static HashMap<Integer, BikeStation.LastHour> cachedLastHourBikeStation = new HashMap<Integer, BikeStation.LastHour>();
	private static long lastFullCacheTime = -1;
	private static HashMap<Integer, Long> lastLastHourCacheTime = new HashMap<Integer, Long>();

	public BikeStationDAO() {}
	
	public BikeStation getCachedBikeStation(Integer stationID) throws NotCachedBikeStation {
		if(cachedBikeStations == null)
			throw new NotCachedBikeStation(stationID);
		
		BikeStation bikeStation = cachedBikeStations.get(stationID);
		if(bikeStation == null)
			throw new NotCachedBikeStation(stationID);
		
		return bikeStation;
	}
	
	public BikeStation.LastHour getCachedLastHourBikeStation(int bikeStationID) throws NotCachedLastHour {
		BikeStation.LastHour lastHour = cachedLastHourBikeStation.get(bikeStationID);
		if(lastHour == null)
			throw new NotCachedLastHour(bikeStationID);
		
		return lastHour;
	}
	
	public ArrayList<BikeStation> getCachedBikeStations() throws NotCachedBikeStations {
		if(cachedBikeStations == null)
			throw new NotCachedBikeStations();
		
		ArrayList<BikeStation> cachedBikeStationsList = new ArrayList<BikeStation>(cachedBikeStations.values());
		
		return cachedBikeStationsList;
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
	
	public class NotCachedLastHour extends ToastedException{
		public int stationID;
		private NotCachedLastHour(int stationID){
			this.stationID = stationID;
		}
		@Override
		public String toString() {
			return "Not cached lastHour of bike station: "+stationID;
		}
	}
	
	public class NotCachedBikeStations extends ToastedException{
		private NotCachedBikeStations(){
		}
		@Override
		public String toString() {
			return "Not cached bike stations";
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
	
	public void lastHourSync(int bikeStationID) throws BadSynchronization{
		//String exampleLastHour = "{\"start\":\"2012/10/30 00:03:08\",\"capacity\":15,\"data\":[8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,9,9,9,9,9,9,9,9,9,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,12,12,12,12,12,12,13,13,13,13,13,13,13,13,13,13,13,13,14,14]}";		
		
		try {
			String lastHourJSON = new BicivAPI_lastHourBikeStation(bikeStationID).call();
			
			BikeStation.LastHour lastHour = new Gson().fromJson(lastHourJSON, BikeStation.LastHour.class);
			cachedLastHourBikeStation.put(bikeStationID, lastHour);
			lastLastHourCacheTime.put(bikeStationID, new Date().getTime());
			
		} catch(JsonSyntaxException e){
			throw new BadSynchronization();
		} catch (IOException e) {
			throw new BadSynchronization();
		}
	}
	
	public long getLastLastHourSync(int bikeStationID){
		Long lastLastCacheTimeForBikeStation =  lastLastHourCacheTime.get(bikeStationID);
		if(lastLastCacheTimeForBikeStation == null)
			return -1;
		else return lastLastCacheTimeForBikeStation;
	}
}
