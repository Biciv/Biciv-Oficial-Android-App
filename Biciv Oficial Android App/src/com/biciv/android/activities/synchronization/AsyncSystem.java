package com.biciv.android.activities.synchronization;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.util.Log;

import com.biciv.android.managers.BikeStationManager;

public class AsyncSystem {

	private ISyncSystem sync;
	
	private final static long TIME_BETWEEN_FULL_SYNC = 1000*60*60*24;
	private final static long TIME_BETWEEN_LASTHOUR_SYNC = 1000*60;
	
	private FullSyncTask fullSyncTask = null;
	private LastHourTask lastHourTask = null;
	
	public AsyncSystem(ISyncSystem sync) {
		this.sync = sync;
	}
	
	/**
	 * Start asynchronous system to sync cache with a fullUpdate.
	 * */
	public void start(){
		fullSyncTask = new FullSyncTask();
		fullSyncTask.exec();
	}
	
	public void startLastHour(int bikeStationID){
		lastHourTask = new LastHourTask(bikeStationID);
		lastHourTask.exec();
	}
	
	/**
	 * Force an asynchronous call right now.
	 * */
	public void syncNow(){
		close();
		fullSyncTask = new FullSyncTask();
		fullSyncTask.execute();
	}
	
	/**
	 * Close the actual asyncSystem.
	 * Should be called with the aim of stop async thread loop.
	 * */
	public void close(){
		if(fullSyncTask != null) {
			fullSyncTask.cancel(true);
		}
		
		if(lastHourTask != null)
			lastHourTask.cancel(true);
	}
	
	private class FullSyncTask extends AsyncTask<Void, Boolean, Void>{
		

		public FullSyncTask(){
		}
		
		public void exec(){
			
			long now = new Date().getTime();
			long lastUpdated = new BikeStationManager().getLastFullSync();
			long timeToWait = TIME_BETWEEN_FULL_SYNC - (now - lastUpdated);
			
			if(timeToWait > 0){
				Timer t = new Timer();
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						execute();
					}
				}, timeToWait);
			} else {
				execute();
			}
			
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				if(isCancelled())
					return null;
				
				new BikeStationManager().forceSync();
				publishProgress(false);
				
			} catch (BadSynchronization e) {
				publishProgress(true);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Boolean... syncError) {
			if( isCancelled() )
				return;
			
			if(syncError[0]){
				sync.onError(SyncSystemSyncTypes.FULLSYNC);
				return;
			}
			
			fullSyncTask = new FullSyncTask();
			fullSyncTask.exec();
			
			sync.onSync(SyncSystemSyncTypes.FULLSYNC);
		}
	}
	
	private class LastHourTask extends AsyncTask<Void, Boolean, Void>{
		private final int bikeStationID;

		public LastHourTask(int bikeStationID){
			this.bikeStationID = bikeStationID;
		}
		
		public void exec(){
			long now = new Date().getTime();
			long lastUpdated = new BikeStationManager().getLastLastHourSync(bikeStationID);
			long timeToWait = TIME_BETWEEN_LASTHOUR_SYNC - (now - lastUpdated);
			
			if(timeToWait > 0) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {
					
					@Override
					public void run() {
						execute();
					}
				}, timeToWait);
			} else {
				execute();
			}
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				if(isCancelled())
					return null;
				
				new BikeStationManager().lastHourSync(bikeStationID);
				publishProgress(false);
				
			} catch (BadSynchronization e) {
				publishProgress(true);
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Boolean... syncError) {
			if( isCancelled() )
				return;
			
			if(syncError[0]){
				sync.onError(SyncSystemSyncTypes.LASTHOURSYNC);
				return;
			}
			
			lastHourTask = new LastHourTask(bikeStationID);
			lastHourTask.exec();
			
			sync.onSync(SyncSystemSyncTypes.LASTHOURSYNC);
		}
		
	}
}
