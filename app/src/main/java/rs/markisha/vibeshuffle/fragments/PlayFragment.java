package rs.markisha.vibeshuffle.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Random;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.VibeShuffle;
import rs.markisha.vibeshuffle.activities.PlaylistActivity;
import rs.markisha.vibeshuffle.model.Beat;
import rs.markisha.vibeshuffle.model.Playback;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.services.VolumeChangeService;
import rs.markisha.vibeshuffle.utils.BeatUtils;
import rs.markisha.vibeshuffle.utils.PlaylistUtils;
import rs.markisha.vibeshuffle.utils.callbacks.BeatDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackStateListener;
import rs.markisha.vibeshuffle.utils.database.DBHelper;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;
import rs.markisha.vibeshuffle.viewmodels.PlayViewModel;

public class PlayFragment extends Fragment implements PlaybackDetailsListener, PlaybackStateListener, BeatDetailsListener {

    private ToggleButton btnPlay;
    private ToggleButton btnVolume;
    private ToggleButton btnState;

    private SpotifyController spotifyController;
    private PlayViewModel model;
    private DBHelper dbHelper;

    private Playlist chillPlaylist;
    private Playlist agroPlaylist;

    private Playlist currentPlaylist;

    private TrackPlayingFragment trackPlayingFragment;
    private VolumeFragment volumeFragment;

    private PlaylistUtils playlistUtils;
    private BeatUtils beatUtils;

    private Intent serviceIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(PlayViewModel.class);

        dbHelper = VibeShuffle.getDBHelper();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", "");
        spotifyController = SpotifyController.getInstance(getContext(), token);

        playlistUtils = new PlaylistUtils();
        beatUtils = new BeatUtils();

        chillPlaylist = dbHelper.getPlaylistOfType("chill");
        agroPlaylist = dbHelper.getPlaylistOfType("agro");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        btnState = view.findViewById(R.id.btnState);
        btnVolume = view.findViewById(R.id.btnVolume);
        btnPlay = view.findViewById(R.id.btnPlay);

        SharedPreferences preferences = requireContext().getSharedPreferences("SwitchState", Context.MODE_PRIVATE);
        btnState.setChecked(preferences.getBoolean("state", false));
        btnVolume.setChecked(preferences.getBoolean("volume", false));
        btnPlay.setChecked(preferences.getBoolean("play", false));

        changeViewColor(view);

        getCurrentPlaylist(btnState.isChecked());

        if (model.getCurrentTrack() != null) {
            loadTrackPlayingFragment();
        }

        btnState.setOnCheckedChangeListener((v, isChecked) -> {
            saveSwitchState("state", isChecked);

            onStateChange(view, isChecked);
        });

        btnVolume.setOnCheckedChangeListener((v, isChecked) -> {
            saveSwitchState("volume", isChecked);

            onVolumeChange(isChecked);
        });

        btnPlay.setOnCheckedChangeListener((v, isChecked) -> {
            saveSwitchState("play", isChecked);

            spotifyController.getCurrentPlaybackState(this, isChecked);
            // goes to onPlaybackDetailsReceived
        });

        spotifyController.checkCurrentPlaybackState(this);

        return view;
    }

    private void saveSwitchState(String key, boolean value) {
        SharedPreferences preferences = requireContext().getSharedPreferences("SwitchState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void onVolumeChange(boolean isChecked) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (isChecked) {
            btnVolume.setTextColor(ResourcesCompat.getColor(getResources(), R.color.teal_200, null));

            volumeFragment = new VolumeFragment();
            transaction.replace(R.id.volume_container, volumeFragment, "volumeFragment");
        } else {
            btnVolume.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));

            if (volumeFragment != null) {
                transaction.remove(volumeFragment);
            }
        }

        transaction.commit();
    }

    private void onStateChange(View view, boolean isChecked) {
        changeViewColor(view);

        getCurrentPlaylist(isChecked);

        if (btnPlay.isChecked()) {
            spotifyController.pausePlayback();
            playRandomDropSectionOfTrackFromPlaylist(currentPlaylist);
        }
    }

    @Override
    public void onPlaybackStateReceived(Playback playback) {
        if (!playback.isPlaying())
            return;
//        if (model.getCurrentTrack() == null)
//            return;
        if (trackPlayingFragment != null)
            return;

        model.setCurrentTrack(playback.getTrack());
//        btnPlay.setChecked(true);

        loadTrackPlayingFragment();
    }

    @Override
    public void onPlaybackDetailsReceived(Playback playbackDetails, boolean isChecked) {
        if (isChecked) {
            if (!playbackDetails.isPlaying()) {
                playRandomDropSectionOfTrackFromPlaylist(currentPlaylist);
            }
        } else {
            if (playbackDetails.isPlaying()) {
                spotifyController.pausePlayback();

                if (serviceIntent != null) {
                    requireActivity().stopService(serviceIntent);
                }

                // remove track playing fragment
                if (trackPlayingFragment != null && getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .remove(trackPlayingFragment)
                            .commit();
                }
            }
        }
    }

    private void playRandomDropSectionOfTrackFromPlaylist(Playlist p) {
        if (serviceIntent == null) {
            serviceIntent = new Intent(requireContext(), VolumeChangeService.class);
            requireActivity().startService(serviceIntent);
        }

        model.setCurrentTrack(playlistUtils.findRandomTrack(p));

        // send track to PlaylistActivity
        if (getActivity() instanceof PlaylistActivity) {
            ((PlaylistActivity) getActivity()).setHighlightedTrack(model.getCurrentTrack());
        }

        // replace track playing fragment
        if (model.getCurrentTrack() != null) {
            loadTrackPlayingFragment();
        }

        if (model.getCurrentTrack() != null) {
            spotifyController.getAudioAnalysis(model.getCurrentTrack().getId(), this);
        }
    }

    @Override
    public void onBeatsDetailsReceived(List<Beat> beats) {
        int trackNumber = playlistUtils.findTrackNumber(currentPlaylist, model.getCurrentTrack());
        Random random = new Random();

        if (beats == null || beats.isEmpty()) {
            int songDuration = model.getCurrentTrack().getDurationMs() - (35 % model.getCurrentTrack().getDurationMs());
            int randomStart = random.nextInt(songDuration);

            spotifyController.resumePlayback(currentPlaylist.getUri(), trackNumber, randomStart);
            changeVolume();

            return;
        }

        List<Integer> beatDropsStart = beatUtils.findBeatDropsStartTimesMs(beats);

        int randInd;
        int randomBeatStart;

        if (beatDropsStart.isEmpty()) {
            randInd = random.nextInt(beats.size());
            randomBeatStart = (int) beats.get(randInd).getStart() * 1000;
        } else {
            randInd = random.nextInt(beatDropsStart.size());
            randomBeatStart = beatDropsStart.get(randInd);
        }

        spotifyController.resumePlayback(currentPlaylist.getUri(), trackNumber, randomBeatStart);
        changeVolume();
    }

    @Override
    public void onPlaybackError() {
        Toast.makeText(requireContext(), "Spotify playback not active", Toast.LENGTH_LONG).show();

        btnPlay.setChecked(false);
        saveSwitchState("play", false);
    }

    private void changeViewColor(View view) {
        ConstraintLayout border = view.findViewById(R.id.player_border);
        int playerColor;

        if (btnState.isChecked()) {
            playerColor = ResourcesCompat.getColor(getResources(), R.color.player_aggressive, null);
        } else {
            playerColor = ResourcesCompat.getColor(getResources(), R.color.player, null);
        }

        border.setBackgroundColor(playerColor);
    }

    private void changeVolume() {
        AudioManager mgr = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
        int spotifyVolume;
        int maxSystemVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int systemVolume;

        if (currentPlaylist == chillPlaylist) {
            spotifyVolume = dbHelper.getVolumeOfPlaylistType("chill");
        } else {
            spotifyVolume = dbHelper.getVolumeOfPlaylistType("agro");
        }

        systemVolume = (int) ((float) spotifyVolume / 100 * maxSystemVolume);

        spotifyController.setVolume(spotifyVolume);
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, systemVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    private void getCurrentPlaylist(boolean btnState) {
        if (btnState) {
            currentPlaylist = agroPlaylist;
        } else {
            currentPlaylist = chillPlaylist;
        }
    }

    private void loadTrackPlayingFragment() {
        trackPlayingFragment = TrackPlayingFragment.newInstance(model.getCurrentTrack());
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.track_playing_container, trackPlayingFragment, "trackPlayingFragment")
                    .commit();
        }
    }

}
