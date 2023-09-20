package rs.markisha.vibeshuffle.utils.network.managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.markisha.vibeshuffle.payload.PlaylistDetailsBuilder;
import rs.markisha.vibeshuffle.utils.callbacks.PlaylistDetailsListener;
import rs.markisha.vibeshuffle.utils.network.ResponseParser;
import rs.markisha.vibeshuffle.utils.network.SpotifyApiHelper;

public class PlaylistManager extends SpotifyApiHelper {

    private final RequestQueue requestQueue;
    private final ResponseParser responseParser;

    public PlaylistManager(Context context, String accessToken) {
        super(context, accessToken);
        this.requestQueue = Volley.newRequestQueue(context);
        this.responseParser = new ResponseParser();
    }

    public void getPlaylist(String playlistId, PlaylistDetailsListener listener) {
        String playbackStateUrl = BASE_URL + "me/playlists/" + playlistId;

        JsonObjectRequest playbackStateRequest = new JsonObjectRequest(
                Request.Method.GET,
                playbackStateUrl,
                null,
                response -> {
                    PlaylistDetailsBuilder playlistDetails = responseParser.parsePlaylistResponse(response);

//                    listener.onPlaylistDetailsRecieved(playlistDetails);
                },
                error -> {
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                return headers;
            }
        };

        requestQueue.add(playbackStateRequest);
    }

    public void getUserPlaylists(PlaylistDetailsListener listener) {
        String playbackStateUrl = BASE_URL + "me/playlists/";

        List<PlaylistDetailsBuilder> playlists = new ArrayList<>();

        JsonObjectRequest playbackStateRequest = new JsonObjectRequest(
                Request.Method.GET,
                playbackStateUrl,
                null,
                response -> {
                    if (response.has("items")) {
                        JSONArray itemsArray = null;
                        try {

                            itemsArray = response.getJSONArray("items");

                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject playlistObject = itemsArray.getJSONObject(i);
                                PlaylistDetailsBuilder playlistDetails = responseParser.parsePlaylistResponse(playlistObject);
                                playlists.add(playlistDetails);
                            }

                            listener.onPlaylistDetailsRecieved(playlists);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> {
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                return headers;
            }
        };

        requestQueue.add(playbackStateRequest);
    }

}
