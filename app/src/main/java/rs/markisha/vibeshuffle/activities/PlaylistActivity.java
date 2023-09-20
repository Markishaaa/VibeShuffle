package rs.markisha.vibeshuffle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.adapters.TrackAdapter;
import rs.markisha.vibeshuffle.payload.PlaylistDetailsBuilder;

public class PlaylistActivity extends AppCompatActivity {

    private PlaylistDetailsBuilder playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Intent i = getIntent();
        if (i.hasExtra("playlist")) {
            playlist = (PlaylistDetailsBuilder) i.getSerializableExtra("playlist");

            ImageView imgView = findViewById(R.id.imageView);
            TextView tvName = findViewById(R.id.tvName);
            TextView tvTrackNumber = findViewById(R.id.tvTrackNumber);

            Picasso.get().load(playlist.getImageUrl()).resize(400, 400).centerCrop().into(imgView);
            tvName.setText(playlist.getName());

            String trackNumber = playlist.getTracks().size() + " songs";
            tvTrackNumber.setText(trackNumber);

            showTracks();
        }
    }

    private void showTracks() {
        RecyclerView recyclerView = findViewById(R.id.rvSongs);
        TrackAdapter adapter = new TrackAdapter(playlist.getTracks());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}