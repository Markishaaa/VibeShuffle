package rs.markisha.vibeshuffle.utils.network;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rs.markisha.vibeshuffle.model.Beat;
import rs.markisha.vibeshuffle.model.Playback;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.model.Track;

public class ResponseParser {

    public Playback parsePlaybackResponse(JSONObject response) {
        try {

            boolean deviceIsActive = response.getJSONObject("device").getBoolean("is_active");
            int timestamp = response.getInt("timestamp");
            int progressMs = response.getInt("progress_ms");
            boolean isPlaying = response.getBoolean("is_playing");

            JSONObject track = response.getJSONObject("item");
            Track trackDetails = parseTrackResponse(track);

            return new Playback(deviceIsActive, timestamp, progressMs, isPlaying, trackDetails);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Playlist parsePlaylistResponse(JSONObject response) {
        try {

            String id = response.getString("id");
            String name = response.getString("name");
            String description = response.getString("description");

            JSONArray images = response.getJSONArray("images");
            String imageUrl = images.getJSONObject(0).getString("url");

            String uri = response.getString("uri");

            List<Track> tracks = new ArrayList<>();

            if (response.getJSONObject("tracks").has("items")) {
                JSONArray tracksArray = response.getJSONObject("tracks").getJSONArray("items");

                for (int i = 0; i < tracksArray.length(); i++) {
                    JSONObject trackObject = tracksArray.getJSONObject(i).getJSONObject("track");
                    Track trackDetails = parseTrackResponse(trackObject);
                    tracks.add(trackDetails);
                }
            }

            Playlist playlistDetails = new Playlist(id, name, description, imageUrl, tracks, uri);

            return playlistDetails;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Track parseTrackResponse(JSONObject response) {
        try {

            String id = response.getString("id");
            String trackName = response.getString("name");
            String trackUri = response.getString("uri");
            String trackType = response.getString("type");
            int trackNumber = response.getInt("track_number");
            int durationMs = response.getInt("duration_ms");

            JSONObject album = response.getJSONObject("album");
            String albumName = album.getString("name");
            JSONArray images = album.getJSONArray("images");
            String imageUrl = images.getJSONObject(0).getString("url");

            Track trackDetails = new Track(id, trackName, trackUri, trackType, trackNumber, durationMs, albumName, imageUrl);

            return trackDetails;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Beat> parseBeatResponse(JSONObject response) {
        List<Beat> beatsList = new ArrayList<>();

        try {

            if (response.has("beats")) {
                JSONArray beats = response.getJSONArray("beats");

                for (int i = 0; i < beats.length(); i++) {
                    JSONObject beatObject = beats.getJSONObject(i);

                    double start = beatObject.getDouble("start");
                    double duration = beatObject.getDouble("duration");
                    double confidence = beatObject.getDouble("confidence");

                    Beat beat = new Beat(start, duration, confidence);
                    beatsList.add(beat);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
