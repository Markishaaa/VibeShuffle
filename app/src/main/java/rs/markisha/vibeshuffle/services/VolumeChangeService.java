package rs.markisha.vibeshuffle.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import rs.markisha.vibeshuffle.receivers.VolumeButtonReceiver;

public class VolumeChangeService extends Service {
    private VolumeButtonReceiver volumeButtonReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        volumeButtonReceiver = new VolumeButtonReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeButtonReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (volumeButtonReceiver != null) {
            unregisterReceiver(volumeButtonReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
