package rs.markisha.vibeshuffle.utils.network;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rs.markisha.vibeshuffle.payload.PlaybackDetailsBuilder;
import rs.markisha.vibeshuffle.payload.PlaylistDetailsBuilder;
import rs.markisha.vibeshuffle.payload.TrackDetailsBuilder;

public class ResponseParser {

    public PlaybackDetailsBuilder parsePlaybackResponse(JSONObject response) {
        try {

            boolean deviceIsActive = response.getJSONObject("device").getBoolean("is_active");
            int timestamp = response.getInt("timestamp");
            int progressMs = response.getInt("progress_ms");
            boolean isPlaying = response.getBoolean("is_playing");

            JSONObject track = response.getJSONObject("item");
            TrackDetailsBuilder trackDetails = parseTrackResponse(track);

            return new PlaybackDetailsBuilder(deviceIsActive, timestamp, progressMs, isPlaying, trackDetails);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public PlaylistDetailsBuilder parsePlaylistResponse(JSONObject response) {
        try {

            String id = response.getString("id");
            String name = response.getString("name");
            String description = response.getString("description");

            JSONArray images = response.getJSONArray("images");
            String imageUrl = images.getJSONObject(0).getString("url");

            String uri = response.getString("uri");

            List<TrackDetailsBuilder> tracks = new ArrayList<>();

            if (response.getJSONObject("tracks").has("items")) {
                JSONArray tracksArray = response.getJSONObject("tracks").getJSONArray("items");

                for (int i = 0; i < tracksArray.length(); i++) {
                    JSONObject trackObject = tracksArray.getJSONObject(i).getJSONObject("track");
                    TrackDetailsBuilder trackDetails = parseTrackResponse(trackObject);
                    tracks.add(trackDetails);
                }
            }

            PlaylistDetailsBuilder playlistDetails = new PlaylistDetailsBuilder(id, name, description, imageUrl, tracks, uri);

            return playlistDetails;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public TrackDetailsBuilder parseTrackResponse(JSONObject response) {
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

            TrackDetailsBuilder trackDetails = new TrackDetailsBuilder(id, trackName, trackUri, trackType, trackNumber, durationMs, albumName, imageUrl);

            return trackDetails;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
