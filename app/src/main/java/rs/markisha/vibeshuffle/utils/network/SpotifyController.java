package rs.markisha.vibeshuffle.utils.network;

import android.content.Context;

import java.io.Serializable;

import rs.markisha.vibeshuffle.utils.callbacks.BeatDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.PlaylistDetailsListener;
import rs.markisha.vibeshuffle.utils.network.managers.AlbumManager;
import rs.markisha.vibeshuffle.utils.network.managers.PlaybackManager;
import rs.markisha.vibeshuffle.utils.network.managers.PlaylistManager;
import rs.markisha.vibeshuffle.utils.network.managers.TrackManager;

public class SpotifyController {

    private static SpotifyController instance;

    private final PlaybackManager playbackManager;
    private final AlbumManager albumManager;
    private final PlaylistManager playlistManager;
    private final TrackManager trackManager;

    private SpotifyController(Context context, String accessToken) {
        playbackManager = new PlaybackManager(context, accessToken);
        albumManager = new AlbumManager(context, accessToken);
        playlistManager = new PlaylistManager(context, accessToken);
        trackManager = new TrackManager(context, accessToken);
    }

    public static synchronized SpotifyController getInstance(Context context, String accessToken) {
        if (instance == null) {
            instance = new SpotifyController(context, accessToken);
        }
        return instance;
    }

    public void playAlbum(String albumUri, int position, int positionMs) {
        albumManager.playAlbum(albumUri, position, positionMs);
    }

    public void getCurrentPlaybackState(PlaybackDetailsListener listener) {
        playbackManager.getCurrentPlaybackState(listener);
    }

    public void pausePlayback() {
        playbackManager.pausePlayback();
    }

    public void resumePlayback(String playlistUri, int trackNumber, int positionMs) {
        playbackManager.resumePlayback(playlistUri, trackNumber, positionMs);
    }

    public void getPlaylist(String playlistId, PlaylistDetailsListener listener, String type) {
        playlistManager.getPlaylist(playlistId, listener, type);
    }

    public void getUserPlaylists(PlaylistDetailsListener listener, String id) {
        playlistManager.getUserPlaylists(listener, id);
    }

    public void getAudioAnalysis(String trackId, BeatDetailsListener listener) {
        trackManager.getAudioAnalysis(trackId, listener);
    }

    public void setVolume(int volume) {
        playbackManager.setVolume(volume);
    }

    public void getUserId(PlaylistDetailsListener l) {
        playlistManager.getUserId(l);
    }

}
