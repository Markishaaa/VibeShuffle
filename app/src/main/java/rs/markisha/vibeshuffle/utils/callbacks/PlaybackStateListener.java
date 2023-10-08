package rs.markisha.vibeshuffle.utils.callbacks;

import rs.markisha.vibeshuffle.model.Playback;

public interface PlaybackStateListener {

    void onPlaybackStateReceived(Playback playback);
    void onPlaybackError();

}
