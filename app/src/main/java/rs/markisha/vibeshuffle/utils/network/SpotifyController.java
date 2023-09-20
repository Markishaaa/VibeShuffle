package rs.markisha.vibeshuffle.utils.network;

import android.content.Context;

import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.network.managers.AlbumManager;
import rs.markisha.vibeshuffle.utils.network.managers.PlaybackManager;

public class SpotifyController {

    private final PlaybackManager playbackManager;
    private final AlbumManager albumManager;

    public SpotifyController(Context context, String accessToken) {
        playbackManager = new PlaybackManager(context, accessToken);
        albumManager = new AlbumManager(context, accessToken);
    }

    public void playAlbum(String albumUri, int position, int positionMs) {
        albumManager.playAlbum(albumUri, position, positionMs);
    }

    public void getCurrentPlaybackState(PlaybackDetailsListener listener) {
        playbackManager.getCurrentPlaybackState(listener);
    }

}
