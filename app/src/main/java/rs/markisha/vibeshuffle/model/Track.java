package rs.markisha.vibeshuffle.model;

import java.io.Serializable;

public class Track implements Serializable {

    private String id;
    private String name;
    private String uri;
    private String type;
    private int trackNumber;
    private int durationMs;

    private String albumName;
    private String imageUrl;

    public Track(String id, String name, String uri, String type, int trackNumber, int durationMs, String albumName, String imageUrl) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.trackNumber = trackNumber;
        this.durationMs = durationMs;
        this.albumName = albumName;
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

    public String getAlbumName() {
        return albumName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
