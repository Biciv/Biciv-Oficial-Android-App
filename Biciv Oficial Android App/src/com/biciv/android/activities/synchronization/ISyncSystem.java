package com.biciv.android.activities.synchronization;

public interface ISyncSystem {
	void onSync(SyncSystemSyncTypes syncType);
	void onError(SyncSystemSyncTypes syncType);
}
