package rs.markisha.vibeshuffle.utils.network;

import android.content.Context;

import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.callbacks.PlaylistDetailsListener;
import rs.markisha.vibeshuffle.utils.network.managers.AlbumManager;
import rs.markisha.vibeshuffle.utils.network.managers.PlaybackManager;
import rs.markisha.vibeshuffle.utils.network.managers.PlaylistManager;

public class SpotifyController {

    private final PlaybackManager playbackManager;
    private final AlbumManager albumManager;
    private final PlaylistManager playlistManager;

    public SpotifyController(Context context, String accessToken) {
        playbackManager = new PlaybackManager(context, accessToken);
        albumManager = new AlbumManager(context, accessToken);
        playlistManager = new PlaylistManager(context, accessToken);
    }

    public void playAlbum(String albumUri, int position, int positionMs) {
        albumManager.playAlbum(albumUri, position, positionMs);
    }

    public void getCurrentPlaybackState(PlaybackDetailsListener listener) {
        playbackManager.getCurrentPlaybackState(listener);
    }

    public void getPlaylist(String playlistId, PlaylistDetailsListener listener) {
        playlistManager.getPlaylist(playlistId, listener);
    }

    public void getUserPlaylists(PlaylistDetailsListener listener) {
        playlistManager.getUserPlaylists(listener);
    }

}
