package rs.markisha.vibeshuffle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.payload.PlaybackDetailsBuilder;
import rs.markisha.vibeshuffle.payload.PlaylistDetailsBuilder;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;

public class MainActivity extends AppCompatActivity implements PlaybackDetailsListener {

    private SpotifyController spotifyController;

    private PlaylistDetailsBuilder chillPlaylist;
    private PlaylistDetailsBuilder aggressivePlaylist;
    private boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnChoosePlaylists = findViewById(R.id.btnChoosePlaylists);

        btnChoosePlaylists.setOnClickListener(view -> {
            Intent in = new Intent(MainActivity.this, ChoosePlaylistsActivity.class);
            startActivity(in);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        if (i.hasExtra("TOKEN")) {
            String token = i.getStringExtra("TOKEN");

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("access_token", token);
            editor.apply();

            spotifyController = SpotifyController.getInstance(this, token);
        }
        checkPlaylist(i);
    }

    private void checkPlaylist(Intent i) {
        if (!i.hasExtra("chillPlaylist")) {
            Log.d("hasplaylist", "no");
            Intent ni = new Intent(this, ChoosePlaylistsActivity.class);
            startActivity(ni);
            finish();
        } else {
            Log.d("hasplaylist", "yes");
            chillPlaylist = (PlaylistDetailsBuilder) i.getSerializableExtra("chillPlaylist");
            aggressivePlaylist = (PlaylistDetailsBuilder) i.getSerializableExtra("aggressivePlaylist");

            if (chillPlaylist != null && aggressivePlaylist != null &&
                    chillPlaylist.getName() != null && aggressivePlaylist.getName() != null) {
                TextView tvChillPlaylist = findViewById(R.id.chillPlaylist2);
                tvChillPlaylist.setText(chillPlaylist.getName());

                TextView tvAggressivePlaylist = findViewById(R.id.aggressivePlaylist2);
                tvAggressivePlaylist.setText(aggressivePlaylist.getName());
            }
        }
    }

    public void onTextClick(View view) {
        Intent i = new Intent(this, PlaylistActivity.class);
        Log.d("hasplaylist", "started");

        if (view.getId() == R.id.chillPlaylist2) {
            Log.d("hasplaylist", "????");
            i.putExtra("playlist", chillPlaylist);
        } else if (view.getId() == R.id.aggressivePlaylist2) {
            Log.d("hasplaylist", "??!@!@!?");
            i.putExtra("playlist", aggressivePlaylist);
        }

        startActivity(i);
        finish();
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