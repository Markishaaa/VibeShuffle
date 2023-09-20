package rs.markisha.vibeshuffle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.payload.PlaybackDetailsBuilder;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;

public class MainActivity extends AppCompatActivity implements PlaybackDetailsListener {

    private SpotifyController spotifyController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        if (i.hasExtra("TOKEN")) {
            String token = i.getStringExtra("TOKEN");

            Log.d("scopes", token);

            spotifyController = new SpotifyController(this, token);

            spotifyController.getCurrentPlaybackState(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPlaybackDetailsReceived(PlaybackDetailsBuilder playbackDetails) {
        // Handle the playback details here
        // getCurrentPlaybackState() from SpotifyApiHelper

        Log.d("playback", "this is playback");

        if (!playbackDetails.isDeviceActive()) {
            Log.d("playback", "not active");
            Toast.makeText(MainActivity.this, "Spotify playback is not active!", Toast.LENGTH_SHORT).show();
        } else {
            spotifyController.playAlbum("spotify:album:0mwmLhhRlrzg8NWohsXH6h", 0, 0);
        }
    }
}