package com.biciv.android.managers;

import com.biciv.android.activities.synchronization.BadSynchronization;
import java.util.ArrayList;
import java.util.HashMap;

import com.biciv.android.dao.BikeStationDAO;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStation;
import com.biciv.android.dao.BikeStationDAO.NotCachedLastHour;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStations;
import com.biciv.android.entities.BikeStation;

public class BikeStationManager {
	
	public BikeStationManager(){}
	
	public long getLastFullSync(){
		return new BikeStationDAO().getLastFullSync();
	}
	
	public long getLastLastHourSync(int bikeStationID){
		return new BikeStationDAO().getLastLastHourSync(bikeStationID);
	}
	
	public void forceSync() throws BadSynchronization{
		new BikeStationDAO().forceSync();
	}
	
	public BikeStation getBikeStation(int bikeStationID) throws NotCachedBikeStation{
		return new BikeStationDAO().getCachedBikeStation(bikeStationID);
	}
	
	public HashMap<Integer, BikeStation> getBikeStations() throws NotCachedBikeStations{
		return new BikeStationDAO().getCachedBikeStations();
	}
	
	public void lastHourSync(int bikeStationID) throws BadSynchronization {
		new BikeStationDAO().lastHourSync(bikeStationID);
	}
	
	public BikeStation.LastHour getLastHour(int bikeStationID) throws NotCachedLastHour{
		return new BikeStationDAO().getCachedLastHourBikeStation(bikeStationID);
	}
	
	public void setBikeStationFavourite(int bikeStationID) throws NotCachedBikeStation{
		new BikeStationDAO().setBikeStationAsFavourite(bikeStationID);
	}
	public void removeBikeStationFavourite(int bikeStationID) throws NotCachedBikeStation{
		new BikeStationDAO().removeBikeStationAsFavourite(bikeStationID);
	}
}
