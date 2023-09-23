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

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.fragments.PlayFragment;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.utils.database.DBHelper;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;

public class MainActivity extends AppCompatActivity {

    private Playlist chillPlaylist;
    private Playlist aggressivePlaylist;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

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

        Log.d("mydb", chillPlaylist.getName());

        if (chillPlaylist == null || aggressivePlaylist == null) {
            return;
        }

        PlayFragment pf = new PlayFragment();
        getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_bottom, pf)
                    .commit();

        TextView tvChillPlaylist = findViewById(R.id.chillPlaylist2);
        tvChillPlaylist.setText(chillPlaylist.getName());

        TextView tvAggressivePlaylist = findViewById(R.id.aggressivePlaylist2);
        tvAggressivePlaylist.setText(aggressivePlaylist.getName());
    }

//    private void checkPlaylist(Intent i) {
//        if (!i.hasExtra("chillPlaylist")) {
//            Intent ni = new Intent(this, ChoosePlaylistsActivity.class);
//            startActivity(ni);
//            finish();
//        } else {
//            chillPlaylist = (Playlist) i.getSerializableExtra("chillPlaylist");
//            aggressivePlaylist = (Playlist) i.getSerializableExtra("aggressivePlaylist");
//
//            PlayFragment pf = new PlayFragment();
//
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("chillPlaylist", chillPlaylist);
//            bundle.putSerializable("agroPlaylist", aggressivePlaylist);
//
//            pf.setArguments(bundle);
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.frame_bottom, pf)
//                    .commit();
//
//            if (chillPlaylist != null && aggressivePlaylist != null &&
//                    chillPlaylist.getName() != null && aggressivePlaylist.getName() != null) {
//                TextView tvChillPlaylist = findViewById(R.id.chillPlaylist2);
//                tvChillPlaylist.setText(chillPlaylist.getName());
//
//                TextView tvAggressivePlaylist = findViewById(R.id.aggressivePlaylist2);
//                tvAggressivePlaylist.setText(aggressivePlaylist.getName());
//            }
//        }
//    }

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