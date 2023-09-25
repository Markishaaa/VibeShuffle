package rs.markisha.vibeshuffle.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.VibeShuffle;
import rs.markisha.vibeshuffle.fragments.PlayFragment;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.utils.database.DBHelper;

public class MainActivity extends AppCompatActivity {

    private Playlist chillPlaylist;
    private Playlist aggressivePlaylist;
    private DBHelper dbHelper;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("dbHelper", "onDestroy");
        dbHelper.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("dbHelper", "onCreate");
        dbHelper = VibeShuffle.getDBHelper();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_bottom, new PlayFragment())
                    .commit();
        }

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

        Log.d("dbHelper", "onStart");
        Intent i = getIntent();
        if (i.hasExtra("TOKEN")) {
            String token = i.getStringExtra("TOKEN");

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("access_token", token);
            editor.putBoolean("isChill", true);
            editor.apply();
        }
        checkPlaylist();
    }

    private void checkPlaylist() {
        chillPlaylist = dbHelper.getPlaylistOfType("chill");
        aggressivePlaylist = dbHelper.getPlaylistOfType("agro");

        if (chillPlaylist == null || aggressivePlaylist == null) {
            Intent ni = new Intent(this, ChoosePlaylistsActivity.class);
            startActivity(ni);
            finish();
        }

        PlayFragment pf = new PlayFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_bottom, pf)
                .commit();

        if (chillPlaylist.getName() != null) {
            TextView tvChillPlaylist = findViewById(R.id.chillPlaylist2);
            tvChillPlaylist.setText(chillPlaylist.getName());
        }

        if (aggressivePlaylist.getName() != null) {
            TextView tvAggressivePlaylist = findViewById(R.id.aggressivePlaylist2);
            tvAggressivePlaylist.setText(aggressivePlaylist.getName());
        }
    }

    public void onTextClick(View view) {
        Intent i = new Intent(this, PlaylistActivity.class);

        if (view.getId() == R.id.chillPlaylist2) {
            i.putExtra("playlist", chillPlaylist);
        } else if (view.getId() == R.id.aggressivePlaylist2) {
            i.putExtra("playlist", aggressivePlaylist);
        }

        startActivity(i);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}