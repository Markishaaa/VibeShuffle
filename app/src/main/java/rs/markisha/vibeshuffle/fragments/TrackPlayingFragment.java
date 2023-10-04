package rs.markisha.vibeshuffle.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.model.Track;
import rs.markisha.vibeshuffle.utils.callbacks.StateChangeListener;

public class TrackPlayingFragment extends Fragment implements StateChangeListener {

    private Track track;

    private static final String ARG_TRACK = "arg_track";

    private View view;

    public static TrackPlayingFragment newInstance(Track track) {
        TrackPlayingFragment fragment = new TrackPlayingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRACK, track);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            track = (Track) getArguments().getSerializable(ARG_TRACK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_track_playing, container, false);

        SharedPreferences preferences = requireContext().getSharedPreferences("SwitchState", Context.MODE_PRIVATE);
        boolean state = preferences.getBoolean("state", false);

        ImageView img = view.findViewById(R.id.imgPlayingTrack);
        TextView tvName = view.findViewById(R.id.tvPlayingTrackName);
        TextView tvArtist = view.findViewById(R.id.tvArtistPlayingTrack);

        Picasso.get().load(track.getImageUrl()).resize(50, 50).centerCrop().into(img);
        tvName.setText(track.getName());
        tvArtist.setText(track.getArtistName());

        return view;
    }

    @Override
    public void onStateChange(boolean state) {
        ConstraintLayout border = view.findViewById(R.id.playing_track_wrapper);
        if (state) {
            border.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.player_playing_aggressive, null));
        } else {
            border.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.player_playing, null));
        }
    }
}