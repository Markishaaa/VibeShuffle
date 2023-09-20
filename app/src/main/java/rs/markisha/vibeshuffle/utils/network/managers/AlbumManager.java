package rs.markisha.vibeshuffle.utils.network.managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import rs.markisha.vibeshuffle.utils.network.SpotifyApiHelper;

public class AlbumManager extends SpotifyApiHelper {

    private final RequestQueue requestQueue;

    public AlbumManager(Context context, String accessToken) {
        super(context, accessToken);
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void playAlbum(String albumUri, int position, int positionMs) {
        String playUrl = BASE_URL + "me/player/play";

        JSONObject requestBody = new JSONObject();
        JSONObject contextUri = new JSONObject();
        JSONObject offset = new JSONObject();

        try {
            contextUri.put("context_uri", albumUri);

            // Set the offset position (optional)
            offset.put("position", position);

            requestBody.put("context_uri", albumUri);
            requestBody.put("offset", offset);

            // Set the playback position in milliseconds (optional)
            requestBody.put("position_ms", positionMs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest playRequest = new JsonObjectRequest(
                Request.Method.PUT,
                playUrl,
                requestBody,
                response -> {
                    // Album should start playing
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
