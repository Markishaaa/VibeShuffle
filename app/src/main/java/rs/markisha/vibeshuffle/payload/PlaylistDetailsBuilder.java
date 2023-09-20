package rs.markisha.vibeshuffle.payload;

import java.util.List;

public class PlaylistDetailsBuilder {

    private String name;
    private String description;
    private String imageUrl;
    private List<TrackDetailsBuilder> tracks;
    private String uri;

    public PlaylistDetailsBuilder(String name, String description, String imageUrl, List<TrackDetailsBuilder> tracks, String uri) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tracks = tracks;
        this.uri = uri;
    }
}
