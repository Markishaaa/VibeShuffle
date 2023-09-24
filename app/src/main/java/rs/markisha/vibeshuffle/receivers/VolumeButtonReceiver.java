package rs.markisha.vibeshuffle.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class VolumeButtonReceiver extends BroadcastReceiver {

//    private VolumeButtonListener listener;
//
//    public VolumeButtonReceiver(VolumeButtonListener listener) {
//        this.listener = listener;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
            int oldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", -1);
            int newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);

            if (streamType == AudioManager.STREAM_MUSIC) {
                // Check if it's a volume down press
                if (newVolume < oldVolume) {
                    // Volume down press
                    Log.d("VolumeButtonReceiver", "Volume Down Press");
                    Intent volumeDownIntent = new Intent("volume_down");
                    context.sendBroadcast(volumeDownIntent);
                }
            }
            // Check if it's a volume up press
            else if (newVolume > oldVolume) {
                // Volume up press
                Log.d("VolumeButtonReceiver", "Volume Up Press");
                Intent volumeUpIntent = new Intent("volume_up");
                context.sendBroadcast(volumeUpIntent);
            }
        }
    }
}
