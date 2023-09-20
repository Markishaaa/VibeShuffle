package rs.markisha.vibeshuffle.utils.network;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rs.markisha.vibeshuffle.payload.PlaybackDetailsBuilder;
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

    public TrackDetailsBuilder parseTrackResponse(JSONObject response) {
        try {

            String trackName = response.getString("name");
            String trackUri = response.getString("uri");
            String trackType = response.getString("type");
            int trackNumber = response.getInt("track_number");
            int durationMs = response.getInt("duration_ms");

            JSONObject album = response.getJSONObject("album");
            String albumName = album.getString("name");
            JSONArray images = album.getJSONArray("images");
            String imageUrl = images.getJSONObject(0).getString("url");

            TrackDetailsBuilder trackDetails = new TrackDetailsBuilder(trackName, trackUri, trackType, trackNumber, durationMs, albumName, imageUrl);

            return trackDetails;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
