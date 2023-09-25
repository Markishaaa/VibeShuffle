package rs.markisha.vibeshuffle.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
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
import rs.markisha.vibeshuffle.model.Beat;
import rs.markisha.vibeshuffle.model.Playback;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.services.VolumeChangeService;
import rs.markisha.vibeshuffle.utils.BeatUtils;
import rs.markisha.vibeshuffle.utils.PlaylistUtils;
import rs.markisha.vibeshuffle.utils.callbacks.BeatDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.database.DBHelper;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;
import rs.markisha.vibeshuffle.viewmodels.PlayViewModel;

public class PlayFragment extends Fragment implements PlaybackDetailsListener, BeatDetailsListener {

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

    private final BroadcastReceiver volumeButtonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null) {
                if (intent.getAction() != null) {
                    if (intent.getAction().equals("volume_down")) {
//                        Log.d("PlayFragment", "volume button down pressed");
//                        spotifyController.setVolume(dbHelper.getVolumeOfPlaylistType("chill"));
//                        currentPlaylist = chillPlaylist;

                    } else if (intent.getAction().equals("volume_up")) {
//                        Log.d("PlayFragment", "volume button down pressed");
//                        spotifyController.setVolume(dbHelper.getVolumeOfPlaylistType("agro"));
//                        currentPlaylist = agroPlaylist;
                    }
                }

                playRandomDropSectionOfTrackFromPlaylist(currentPlaylist);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IntentFilter filter = new IntentFilter();
//        filter.addAction("volume_down");
//        filter.addAction("volume_up");
//        requireActivity().registerReceiver(volumeButtonReceiver, filter);

        model = new ViewModelProvider(requireActivity()).get(PlayViewModel.class);
        Log.d("PlayFragment", model.getCurrentTrack() + " ");

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

        if (!btnState.isChecked()) {
            currentPlaylist = agroPlaylist;
        } else {
            currentPlaylist = chillPlaylist;
        }

        if (model.getCurrentTrack() != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            trackPlayingFragment = TrackPlayingFragment.newInstance(model.getCurrentTrack());
            transaction.replace(R.id.track_playing_container, trackPlayingFragment, "trackPlayingFragment");
            transaction.commit();
        }

        btnState.setOnCheckedChangeListener((v, isChecked) -> {
            saveSwitchState("state", isChecked);

            changeViewColor(view);

            if (!isChecked) {
                currentPlaylist = agroPlaylist;
            } else {
                currentPlaylist = chillPlaylist;
            }

            if (btnPlay.isChecked()) {
                spotifyController.pausePlayback();
                playRandomDropSectionOfTrackFromPlaylist(currentPlaylist);
            }
        });

        btnVolume.setOnCheckedChangeListener((v, isChecked) -> {
            saveSwitchState("volume", isChecked);

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            if (btnVolume.isChecked()) {
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
        });

        btnPlay.setOnCheckedChangeListener((v, isChecked) -> {
            saveSwitchState("play", isChecked);

            spotifyController.getCurrentPlaybackState(this);
            // goes to onPlaybackDetailsReceived
        });

        return view;
    }

    private void changeViewColor(View view) {
        ConstraintLayout border = view.findViewById(R.id.player_border);
        if (btnState.isChecked()) {
            border.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.player, null));
        } else {
            border.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.player_aggressive, null));
        }
    }

    private void saveSwitchState(String key, boolean value) {
        SharedPreferences preferences = requireContext().getSharedPreferences("SwitchState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public void onPlaybackDetailsReceived(Playback playbackDetails) {
        btnPlay.setChecked(!playbackDetails.isPlaying());

        if (playbackDetails.isPlaying()) {
            spotifyController.pausePlayback();

            if (serviceIntent != null) {
                requireActivity().stopService(serviceIntent);
            }

            // remove track playing fragment
            if (trackPlayingFragment != null) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.remove(trackPlayingFragment);
                transaction.commit();
            }
        } else {
            playRandomDropSectionOfTrackFromPlaylist(currentPlaylist);
        }
    }

    private void changeVolume() {
        AudioManager mgr = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume;
        if (currentPlaylist == chillPlaylist) {
            volume = dbHelper.getVolumeOfPlaylistType("chill") % maxVolume;
            mgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            spotifyController.setVolume(dbHelper.getVolumeOfPlaylistType("chill"));
        } else {
            volume = dbHelper.getVolumeOfPlaylistType("agro") % maxVolume;
            mgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            spotifyController.setVolume(dbHelper.getVolumeOfPlaylistType("agro"));
        }
    }

    private void playRandomDropSectionOfTrackFromPlaylist(Playlist p) {
        changeVolume();

        if (serviceIntent == null) {
            serviceIntent = new Intent(requireContext(), VolumeChangeService.class);
            requireActivity().startService(serviceIntent);
        }

        model.setCurrentTrack(playlistUtils.findRandomTrack(p));

        //replace track playing fragment
        if (model.getCurrentTrack() != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            trackPlayingFragment = TrackPlayingFragment.newInstance(model.getCurrentTrack());
            transaction.replace(R.id.track_playing_container, trackPlayingFragment, "trackPlayingFragment");
            transaction.commit();
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
    }

    @Override
    public void onPlaybackError() {
        Toast.makeText(requireContext(), "Spotify playback not active", Toast.LENGTH_LONG).show();
        btnPlay.setChecked(false);
    }

}
