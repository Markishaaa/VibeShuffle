package rs.markisha.vibeshuffle.payload;

public class TrackDetailsBuilder {

    private String name;
    private String uri;
    private String type;
    private int trackNumber;
    private int durationMs;

    private String albumName;
    private String imageUrl;

    public TrackDetailsBuilder(String name, String uri, String type, int trackNumber, int durationMs, String albumName, String imageUrl) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.trackNumber = trackNumber;
        this.durationMs = durationMs;
        this.albumName = albumName;
        this.imageUrl = imageUrl;
    }
}
