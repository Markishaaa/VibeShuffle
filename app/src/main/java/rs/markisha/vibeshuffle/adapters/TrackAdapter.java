package rs.markisha.vibeshuffle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.model.Track;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private final List<Track> tracks;

    public TrackAdapter(List<Track> tracks) {
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.ViewHolder holder, int position) {
        Track track = tracks.get(position);
        if (track != null) {
            Picasso.get().load(track.getImageUrl()).resize(100, 100).centerCrop().into(holder.trackImage);
            holder.trackName.setText(track.getName());

            int minutes = (track.getDurationMs() / 1000) / 60;
            int seconds = (track.getDurationMs() / 1000) % 60;
            String duration = minutes + ":" + seconds;
            holder.trackDuration.setText(duration);
        }

        holder.itemView.setOnClickListener(v -> {
            // play track
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView trackImage;
        public TextView trackName;
        public TextView trackDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            trackImage = itemView.findViewById(R.id.imageTrack);
            trackName = itemView.findViewById(R.id.tvTrackName);
            trackDuration = itemView.findViewById(R.id.tvTrackDuration);
        }

    }
}
