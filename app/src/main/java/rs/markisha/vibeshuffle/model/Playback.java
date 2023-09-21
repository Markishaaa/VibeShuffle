package rs.markisha.vibeshuffle.model;

import java.io.Serializable;

public class Playback implements Serializable {

    private boolean deviceActive;
    private int timestamp;
    private int progressMs;
    private boolean isPlaying;

    private Track track;

    public Playback(boolean deviceActive, int timestamp, int progressMs, boolean isPlaying, Track track) {
        this.deviceActive = deviceActive;
        this.timestamp = timestamp;
        this.progressMs = progressMs;
        this.isPlaying = isPlaying;
        this.track = track;
    }

    public boolean isDeviceActive() {
        return deviceActive;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getProgressMs() {
        return progressMs;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public Track getTrack() {
        return track;
    }

}
