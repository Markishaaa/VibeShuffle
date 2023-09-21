package rs.markisha.vibeshuffle.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import rs.markisha.vibeshuffle.utils.callbacks.VolumeButtonListener;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;

public class VolumeButtonReceiver extends BroadcastReceiver {

    private List<VolumeButtonListener> listeners = new ArrayList<>();

    private SpotifyController controller;
    private SharedPreferences sharedPreferences;
    private final Context context;

    public VolumeButtonReceiver(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", "");
        controller = SpotifyController.getInstance(context, token);
    }

    public void registerVolumeButtonListener(VolumeButtonListener listener) {
        listeners.add(listener);
    }

    public void unregisterVolumeButtonListener(VolumeButtonListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP ||
                    keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                Log.d("please", "ksad");
                // Detect volume button press here

                // Notify the listener if it's registered
                for (VolumeButtonListener listener : listeners) {
                    listener.onVolumeButtonPressed();
                }
            }
        }
    }
}
