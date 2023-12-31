package rs.markisha.vibeshuffle.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class Track implements Serializable {

    private String id;
    private String name;
    private String uri;
    private String type;
    private int trackNumber;
    private int durationMs;

    private String artistName;
    private String imageUrl;

    private int progressMs;

    public Track(String id, String name, String uri, String type, int trackNumber, int durationMs, String artistName, String imageUrl) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.trackNumber = trackNumber;
        this.durationMs = durationMs;
        this.artistName = artistName;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getType() {
        return type;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getProgressMs() {
        return progressMs;
    }

    public String getArtistName() { return artistName; }
    public void setProgressMs(int progressMs) {
        this.progressMs = progressMs;
    }

}
