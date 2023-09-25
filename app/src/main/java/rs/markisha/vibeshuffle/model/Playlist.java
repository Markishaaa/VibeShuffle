package rs.markisha.vibeshuffle.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class Playlist implements Serializable {

    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<Track> tracks;
    private String uri;
    private String authorId;

    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Playlist(String id, String name, String description, String imageUrl, List<Track> tracks, String uri, String authorId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tracks = tracks;
        this.uri = uri;
        this.authorId = authorId;
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

    public List<Track> getTracks() {
        return tracks;
    }

    public String getUri() {
        return uri;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorId() {
        return authorId;
    }
}
