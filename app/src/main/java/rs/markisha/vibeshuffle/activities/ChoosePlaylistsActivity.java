package rs.markisha.vibeshuffle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.adapters.PlaylistAdapter;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.utils.callbacks.PlaylistDetailsListener;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;

public class ChoosePlaylistsActivity extends AppCompatActivity implements PlaylistDetailsListener {

    private Spinner sChillPlaylist, sAggressivePlaylist;
    private Playlist chillPlaylist;
    private Playlist aggressivePlaylist;
    private SpotifyController spotifyController;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_playlists);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", "");
        spotifyController = SpotifyController.getInstance(this, token);

        spotifyController.getUserPlaylists(this);
    }

    @Override
    public void onUserPlaylistsDetailsReceived(List<Playlist> playlists) {
        if (playlists.isEmpty()) {
            Toast.makeText(this, "Playlist is empty", Toast.LENGTH_LONG).show();
            return;
        }

        PlaylistAdapter adapter = new PlaylistAdapter(this, playlists);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sChillPlaylist = findViewById(R.id.spinnerChillPlaylist);
        sChillPlaylist.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sAggressivePlaylist = findViewById(R.id.spinnerAggressivePlaylist);
        sAggressivePlaylist.setAdapter(adapter);

        Button btnChoosePlaylists = findViewById(R.id.btnChoosePlaylists);

        btnChoosePlaylists.setOnClickListener(view -> {
            String chillPlaylistId = ((Playlist) sChillPlaylist.getSelectedItem()).getId();
            String aggressivePlaylistId = ((Playlist) sAggressivePlaylist.getSelectedItem()).getId();

            intent = new Intent(ChoosePlaylistsActivity.this, MainActivity.class);

            spotifyController.getPlaylist(chillPlaylistId, this);
            spotifyController.getPlaylist(aggressivePlaylistId, this);
        });
    }

    @Override
    public void onPlaylistDetailsReceived(Playlist playlist) {
        if (chillPlaylist == null) {
            chillPlaylist = playlist;
            intent.putExtra("chillPlaylist", chillPlaylist);
        } else {
            aggressivePlaylist = playlist;
            intent.putExtra("aggressivePlaylist", aggressivePlaylist);

            startActivity(intent);

            finish();
        }
    }
}