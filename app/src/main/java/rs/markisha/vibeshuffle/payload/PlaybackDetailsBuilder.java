package rs.markisha.vibeshuffle.payload;

import java.io.Serializable;

public class PlaybackDetailsBuilder implements Serializable {

    private boolean deviceActive;
    private int timestamp;
    private int progress_ms;
    private boolean isPlaying;

    private TrackDetailsBuilder track;

    public PlaybackDetailsBuilder(boolean deviceActive, int timestamp, int progress_ms, boolean isPlaying, TrackDetailsBuilder track) {
        this.deviceActive = deviceActive;
        this.timestamp = timestamp;
        this.progress_ms = progress_ms;
        this.isPlaying = isPlaying;
        this.track = track;
    }

    public boolean isDeviceActive() {
        return deviceActive;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getProgress_ms() {
        return progress_ms;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public TrackDetailsBuilder getTrack() {
        return track;
    }

}
