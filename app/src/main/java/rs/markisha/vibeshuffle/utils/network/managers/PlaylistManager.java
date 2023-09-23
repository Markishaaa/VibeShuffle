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

import rs.markisha.vibeshuffle.model.Playlist;
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

    public void getPlaylist(String playlistId, PlaylistDetailsListener listener, String type) {
        String url = BASE_URL + "playlists/" + playlistId;

        JsonObjectRequest playbackStateRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Playlist playlistDetails = responseParser.parsePlaylistResponse(response);

                    listener.onPlaylistDetailsReceived(playlistDetails, type);
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
        String url = BASE_URL + "me/playlists/";

        List<Playlist> playlists = new ArrayList<>();

        JsonObjectRequest playbackStateRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    if (response.has("items")) {
                        JSONArray itemsArray = null;
                        try {

                            itemsArray = response.getJSONArray("items");

                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject playlistObject = itemsArray.getJSONObject(i);
                                Playlist playlistDetails = responseParser.parseUserPlaylistResponse(playlistObject);
                                playlists.add(playlistDetails);
                            }

                            listener.onUserPlaylistsDetailsReceived(playlists);

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

    public void addToPlaylist(String playlistId, List<String> trackUris, int position) {
        String addToPlaylistUrl = "playlists/" + playlistId + "/tracks";

        JSONObject requestBody = new JSONObject();

        try {

            JSONArray urisArray = new JSONArray(trackUris);
            requestBody.put("uris", urisArray);
            requestBody.put("position", position);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest playRequest = new JsonObjectRequest(
                Request.Method.PUT,
                addToPlaylistUrl,
                requestBody,
                response -> {
                },
                error -> {
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(playRequest);
    }

    public void removeFromPlaylist(String playlistId, List<String> trackUris) {
        String removeFromPlaylistUrl = "playlists/" + playlistId + "/tracks";

        JSONObject requestBody = new JSONObject();

        try {

            JSONArray tracksArray = new JSONArray();
            for (String uri : trackUris) {
                JSONObject trackObject = new JSONObject();
                trackObject.put("uri", uri);
                tracksArray.put(trackObject);
            }
            requestBody.put("tracks", tracksArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest playRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                removeFromPlaylistUrl,
                requestBody,
                response -> {
                },
                error -> {
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(playRequest);
    }

}
