package rs.markisha.vibeshuffle.model;

import java.io.Serializable;

public class Playback implements Serializable {

    private boolean deviceActive;
    private int timestamp;
    private int progress_ms;
    private boolean isPlaying;

    private Track track;

    public Playback(boolean deviceActive, int timestamp, int progress_ms, boolean isPlaying, Track track) {
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

    public Track getTrack() {
        return track;
    }

}
