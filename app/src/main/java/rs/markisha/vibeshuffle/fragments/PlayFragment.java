package rs.markisha.vibeshuffle.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.model.Beat;
import rs.markisha.vibeshuffle.model.Playback;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.model.Track;
import rs.markisha.vibeshuffle.receivers.VolumeButtonReceiver;
import rs.markisha.vibeshuffle.utils.PlaylistUtils;
import rs.markisha.vibeshuffle.utils.callbacks.BeatDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.VolumeButtonListener;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;


public class PlayFragment extends Fragment implements PlaybackDetailsListener, VolumeButtonListener, BeatDetailsListener {

    private SpotifyController spotifyController;
    private SharedPreferences sharedPreferences;
    private VolumeButtonReceiver volumeButtonReceiver;
    private PlaylistUtils playlistUtils;

    private Button btnState;
    private Button btnPlay;
    private Button btnVolume;

    private boolean playing;

    private Playlist chillPlaylist;
    private Playlist agroPlaylist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", "");
        spotifyController = SpotifyController.getInstance(getContext(), token);

        volumeButtonReceiver = new VolumeButtonReceiver(requireContext());
        playlistUtils = new PlaylistUtils();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            chillPlaylist = (Playlist) bundle.getSerializable("chillPlaylist");
            agroPlaylist = (Playlist) bundle.getSerializable("agroPlaylist");
        }

        btnState = view.findViewById(R.id.button);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnVolume = view.findViewById(R.id.btnVolume);

        btnPlay.setOnClickListener(v -> {
            spotifyController.getCurrentPlaybackState(this);
        });

        btnVolume.setOnClickListener(v -> {
            Log.d("please", "please");
        });

        btnState.setOnClickListener(v -> {
            boolean state = !(sharedPreferences.getBoolean("isChill", true));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isChill", state);
            editor.apply();

            setBtnStateText(state);


            if (!playing) {
                spotifyController.pausePlayback();
                playRandomDropSectionOfTrack();
            }
        });

        return view;
    }

    public void onPlaybackDetailsReceived(Playback playbackDetails) {
        playing = playbackDetails.isPlaying();

        if (playing) {
            // Handle pause logic
            spotifyController.pausePlayback();
            btnPlay.setText(R.string.play_text);
        } else {
            playRandomDropSectionOfTrack();
        }
    }

    private Track currentTrack;
    private Playlist currentPlaylist;

    private void playRandomDropSectionOfTrack() {
        boolean isChill = sharedPreferences.getBoolean("isChill", true);

        if (isChill) {
            currentPlaylist = chillPlaylist;
        } else {
            currentPlaylist = agroPlaylist;
        }

        currentTrack = playlistUtils.findRandomTrack(currentPlaylist);

        spotifyController.getAudioAnalysis(currentTrack.getId(), this);

        btnPlay.setText(R.string.pause_text);
    }

    @Override
    public void onBeatsDetailsReceived(List<Beat> beats) {
        int trackNumber = playlistUtils.findTrackNumber(currentPlaylist, currentTrack);
        Random random = new Random();

        if (beats == null || beats.isEmpty()) {
            int songDuration = currentTrack.getDurationMs() - (35 % currentTrack.getDurationMs());
            int randomStart = random.nextInt(songDuration);

            spotifyController.resumePlayback(currentPlaylist.getUri(), trackNumber, randomStart);

            return;
        }

        List<Integer> beatDropsStart = new ArrayList<>();
        double initialBeatDropThreshold = 1.5; // seconds
        double beatDropThreshold = initialBeatDropThreshold;

        for (Beat b : beats) {
            double beatEndTime = b.getStart() + b.getDuration();

            if (b.getDuration() > beatDropThreshold) {
                beatDropsStart.add((int) (b.getStart() * 1000)); // milliseconds
            }
        }

        if (beatDropsStart.isEmpty()) {
            // If no beats meet the initial threshold, reduce the threshold
            beatDropThreshold = initialBeatDropThreshold * 0.5;

            // Re-check the beats with the new threshold
            for (Beat b : beats) {
                double beatEndTime = b.getStart() + b.getDuration();

                if (b.getDuration() > beatDropThreshold) {
                    beatDropsStart.add((int) (b.getStart() * 1000)); // milliseconds
                }
            }
        }

        // FIX THIS
        int randomInd = beatDropsStart.isEmpty() ?
                (beats.isEmpty() ? 0 : random.nextInt(beats.size())) :
                random.nextInt(beatDropsStart.size());
        int randomBeatStart = beatDropsStart.get(randomInd);

        spotifyController.resumePlayback(currentPlaylist.getUri(), trackNumber, randomBeatStart);
    }

    private void playTrackWithInterruptions(Playback playbackDetails) {
        boolean isChill = sharedPreferences.getBoolean("isChill", true);
        Track currentTrack;
        Playlist currentPlaylist;

        if (isChill) {
            currentPlaylist = chillPlaylist;
        } else {
            currentPlaylist = agroPlaylist;
        }

        // Get the progress of the current track
        int currentProgressMs;

        // Set the progress to the new track
        currentTrack = playbackDetails.getTrack();
        currentProgressMs = playbackDetails.getProgressMs();

        int trackNum = playlistUtils.findTrackNumber(currentPlaylist, currentTrack);

        // Resume playback with the updated progress
        spotifyController.resumePlayback(currentPlaylist.getUri(), trackNum, currentProgressMs);
    }

    @Override
    public void onResume() {
        super.onResume();
        volumeButtonReceiver.registerVolumeButtonListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        volumeButtonReceiver.unregisterVolumeButtonListener(this);
    }

    @Override
    public void onVolumeButtonPressed() {
        if (btnState != null) {
            Log.d("wonder", "wonder");
            boolean isChill = sharedPreferences.getBoolean("isChill", true);
            setBtnStateText(isChill);
        }
    }

    private void setBtnStateText(boolean state) {
        if (state)
            btnState.setText(R.string.chill_text);
        else
            btnState.setText(R.string.agro_text);
    }

}