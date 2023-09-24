package rs.markisha.vibeshuffle.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Random;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.model.Beat;
import rs.markisha.vibeshuffle.model.Playback;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.model.Track;
import rs.markisha.vibeshuffle.receivers.VolumeButtonReceiver;
import rs.markisha.vibeshuffle.utils.BeatUtils;
import rs.markisha.vibeshuffle.utils.PlaylistUtils;
import rs.markisha.vibeshuffle.utils.callbacks.BeatDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.VolumeButtonListener;
import rs.markisha.vibeshuffle.utils.database.DBHelper;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;
import rs.markisha.vibeshuffle.viewmodels.PlayViewModel;


public class PlayFragment extends Fragment implements PlaybackDetailsListener, VolumeButtonListener, BeatDetailsListener {

    private SpotifyController spotifyController;
    private SharedPreferences sharedPreferences;
    private VolumeButtonReceiver volumeButtonReceiver;

    private PlaylistUtils playlistUtils;
    private BeatUtils beatUtils;

    private DBHelper dbHelper;

    private Button btnState;
    private Button btnPlay;
    private Button btnVolume;

    private Track currentTrack;
    private Playlist currentPlaylist;

    private boolean playing;
    private boolean state = true;

    private Playlist chillPlaylist;
    private Playlist agroPlaylist;

    private TrackPlayingFragment trackPlayingFragment;
    private FragmentTransaction transaction;
    private PlayViewModel model;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("saveInstance", "saving");

        outState.putSerializable("chillPlaylist", chillPlaylist);
        outState.putSerializable("agroPlaylist", agroPlaylist);
        outState.putSerializable("currentPlaylist", currentPlaylist);
        outState.putSerializable("currentTrack", currentTrack);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("uichange", "this is ondestroy");
        model.setUiState(currentTrack, currentPlaylist, playing, state);

        dbHelper.close();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Log.d("saveInstance", "retrieving");
            chillPlaylist = (Playlist) savedInstanceState.get("chillPlaylist");
            agroPlaylist = (Playlist) savedInstanceState.get("agroPlaylist");
            currentTrack = (Track) savedInstanceState.get("currentTrack");
            currentPlaylist = (Playlist) savedInstanceState.get("currentPlaylist");
        }

        model = new ViewModelProvider(this).get(PlayViewModel.class);
        model.getUiState().observe(this, uiState -> {
            currentTrack = uiState.getCurrentTrack();
            currentPlaylist = uiState.getCurrentPlaylist();
            playing = uiState.isPlaying();
            state = uiState.getState();

            Log.d("uichange", playing + " " + state);
        });

        dbHelper = new DBHelper(requireContext());

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", "");
        spotifyController = SpotifyController.getInstance(getContext(), token);

        volumeButtonReceiver = new VolumeButtonReceiver(requireContext());
        playlistUtils = new PlaylistUtils();
        beatUtils = new BeatUtils();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        chillPlaylist = dbHelper.getPlaylistOfType("chill");
        agroPlaylist = dbHelper.getPlaylistOfType("agro");

        btnState = view.findViewById(R.id.button);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnVolume = view.findViewById(R.id.btnVolume);

        checkForUpdates();

        btnPlay.setOnClickListener(v -> {
            spotifyController.getCurrentPlaybackState(this);
            // goes to onPlaybackDetailsReceived
        });

        btnVolume.setOnClickListener(v -> {
            spotifyController.resumePlayback("spotify:playlist:3A9mgJfKfHf1ogOzX9V54m", 0, 0);
        });

        btnState.setOnClickListener(v -> {
            state = !(sharedPreferences.getBoolean("isChill", true));
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

    private void checkForUpdates() {
        if (playing) {
            btnPlay.setText(R.string.play_text);
        } if (!state) {
            btnState.setText(R.string.agro_text);
        }
    }


    public void onPlaybackDetailsReceived(Playback playbackDetails) {
        playing = playbackDetails.isPlaying();

        if (playing) {
            // Handle pause logic
            spotifyController.pausePlayback();
            btnPlay.setText(R.string.play_text);

            if (trackPlayingFragment != null) {
                transaction = getChildFragmentManager().beginTransaction();
                transaction.remove(trackPlayingFragment);
                transaction.commit();
            }
        } else {
            playRandomDropSectionOfTrack();
        }
    }

    @Override
    public void onPlaybackError() {
        Toast.makeText(requireContext(), "Spotify playback not active", Toast.LENGTH_LONG).show();
    }

    private void playRandomDropSectionOfTrack() {
        state = sharedPreferences.getBoolean("isChill", true);

        if (state) {
            currentPlaylist = chillPlaylist;
        } else {
            currentPlaylist = agroPlaylist;
        }

        currentTrack = playlistUtils.findRandomTrack(currentPlaylist);

        trackPlayingFragment = TrackPlayingFragment.newInstance(currentTrack);
        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.track_playing_container, trackPlayingFragment);
        transaction.commit();

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

        List<Integer> beatDropsStart = beatUtils.findBeatDropsStartTimesMs(beats);

        int randInd = 0;
        int randomBeatStart = 0;

        if (beatDropsStart.isEmpty()) {
            randInd = random.nextInt(beats.size());
            randomBeatStart = (int) beats.get(randInd).getStart() * 1000;
        } else {
            randInd = random.nextInt(beatDropsStart.size());
            randomBeatStart = beatDropsStart.get(randInd);
        }

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
            boolean isChill = sharedPreferences.getBoolean("isChill", true);
            setBtnStateText(isChill);
        }
    }

    private void setBtnStateText(boolean isChill) {
        if (isChill)
            btnState.setText(R.string.chill_text);
        else
            btnState.setText(R.string.agro_text);
    }

}