package rs.markisha.vibeshuffle.payload;

import java.io.Serializable;
import java.util.List;

public class PlaylistDetailsBuilder implements Serializable {

    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<TrackDetailsBuilder> tracks;
    private String uri;

    public PlaylistDetailsBuilder(String id, String name, String description, String imageUrl, List<TrackDetailsBuilder> tracks, String uri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tracks = tracks;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<TrackDetailsBuilder> getTracks() {
        return tracks;
    }

    public String getUri() {
        return uri;
    }
}
