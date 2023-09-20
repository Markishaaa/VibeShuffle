package rs.markisha.vibeshuffle.utils.network.managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import rs.markisha.vibeshuffle.payload.PlaybackDetailsBuilder;
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
                    PlaybackDetailsBuilder playbackDetails = rp.parsePlaybackResponse(response);

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


}
