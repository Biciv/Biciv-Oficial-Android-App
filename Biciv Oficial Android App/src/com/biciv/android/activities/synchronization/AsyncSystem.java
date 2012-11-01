package com.biciv.android.activities.synchronization;

import java.util.Date;

import android.os.AsyncTask;
import android.util.Log;

import com.biciv.android.managers.BikeStationManager;

public class AsyncSystem {

	private ISyncSystem sync;
	
	private final static long TIME_BETWEEN_FULL_SYNC = 1000*60*60*24;
	
	private FullSyncTask fullSyncTask = null;
	
	public AsyncSystem(ISyncSystem sync) {
		this.sync = sync;
	}
	
	/**
	 * Start asynchronous system to sync cache with a fullUpdate.
	 * */
	public void start(){
		fullSyncTask = new FullSyncTask();
		fullSyncTask.execute();
	}
	
	/**
	 * Force an asynchronous call right now.
	 * */
	public void syncNow(){
		close();
		fullSyncTask = new FullSyncTask(true);
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
	}
	
	private class FullSyncTask extends AsyncTask<Void, Boolean, Void>{
		
		private final boolean executeNow;

		public FullSyncTask(boolean executeNow){
			this.executeNow = executeNow;
		}
		
		public FullSyncTask(){
			this.executeNow = false;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				
				if(!executeNow){
					long now = new Date().getTime();
					long lastUpdated = new BikeStationManager().getLastFullSync();
					long timeToWait = TIME_BETWEEN_FULL_SYNC - (now - lastUpdated);
					
					if(timeToWait > 0)
						Thread.sleep(timeToWait);
				}
				
				if(isCancelled())
					return null;
				
				new BikeStationManager().forceSync();
				publishProgress(false);
				
			} catch (BadSynchronization e) {
				publishProgress(true);
			} catch (InterruptedException e) {
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
			fullSyncTask.execute();
			
			sync.onSync(SyncSystemSyncTypes.FULLSYNC);
		}
	}
}
