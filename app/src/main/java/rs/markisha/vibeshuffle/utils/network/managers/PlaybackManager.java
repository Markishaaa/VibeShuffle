package rs.markisha.vibeshuffle.utils.network.managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rs.markisha.vibeshuffle.model.Playback;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;
import rs.markisha.vibeshuffle.utils.network.ResponseParser;
import rs.markisha.vibeshuffle.utils.network.SpotifyApiHelper;

public class PlaybackManager extends SpotifyApiHelper {

    private final RequestQueue requestQueue;

    public PlaybackManager(Context context, String accessToken) {
        super(context, accessToken);
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void getCurrentPlaybackState(PlaybackDetailsListener listener) {
        String playbackStateUrl = BASE_URL + "me/player";

        JsonObjectRequest playbackStateRequest = new JsonObjectRequest(
                Request.Method.GET,
                playbackStateUrl,
                null,
                response -> {
                    ResponseParser rp = new ResponseParser();
                    Playback playbackDetails = rp.parsePlaybackResponse(response);

                    listener.onPlaybackDetailsReceived(playbackDetails);
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

    public void toggleShuffle(boolean state) {
        String shuffleUrl = BASE_URL + "me/player/shuffle";

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest playRequest = new JsonObjectRequest(
                Request.Method.PUT,
                shuffleUrl,
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

    public void setVolume(int volume) {
        if (volume > 100 || volume < 0) {
            return;
        }

        String volumeUrl = BASE_URL + "me/player/volume";

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("volume", volume);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest playRequest = new JsonObjectRequest(
                Request.Method.PUT,
                volumeUrl,
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

    public void resumePlayback(String playlistUri, int trackNumber, int positionMs) {
        String playUrl = BASE_URL + "me/player/play";

        JSONObject requestBody = new JSONObject();


        try {
            JSONObject offset = new JSONObject();
            offset.put("position", trackNumber);

            requestBody.put("context_uri", playlistUri);
            requestBody.put("offset", offset);
            requestBody.put("position_ms", positionMs);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest playRequest = new JsonObjectRequest(
                Request.Method.PUT,
                playUrl,
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

    public void pausePlayback() {
        String pauseUrl = BASE_URL + "me/player/pause";

        StringRequest pauseRequest = new StringRequest(
                Request.Method.PUT,
                pauseUrl,
                (Response.Listener<String>) response -> {
                    // Playback paused successfully
                },
                (Response.ErrorListener) error -> {
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                return headers;
            }
        };

        requestQueue.add(pauseRequest);
    }

}
