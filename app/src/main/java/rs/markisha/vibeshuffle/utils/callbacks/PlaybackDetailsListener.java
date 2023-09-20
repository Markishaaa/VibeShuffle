package rs.markisha.vibeshuffle.utils.callbacks;

import rs.markisha.vibeshuffle.payload.PlaybackDetailsBuilder;

public interface PlaybackDetailsListener {

    void onPlaybackDetailsReceived(PlaybackDetailsBuilder playbackDetails);

}
