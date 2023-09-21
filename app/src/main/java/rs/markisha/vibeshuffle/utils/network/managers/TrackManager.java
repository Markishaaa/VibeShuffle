package rs.markisha.vibeshuffle.utils.network.managers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.markisha.vibeshuffle.model.Beat;
import rs.markisha.vibeshuffle.model.Playback;
import rs.markisha.vibeshuffle.utils.callbacks.BeatDetailsListener;
import rs.markisha.vibeshuffle.utils.network.ResponseParser;
import rs.markisha.vibeshuffle.utils.network.SpotifyApiHelper;

public class TrackManager extends SpotifyApiHelper {

    private final RequestQueue requestQueue;

    public TrackManager(Context context, String accessToken) {
        super(context, accessToken);
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void getAudioAnalysis(String songId, BeatDetailsListener listener) {
        String audioUrl = BASE_URL + "audio-analysis/" + songId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                audioUrl,
                null,
                response -> {
                    ResponseParser rp = new ResponseParser();
                    List<Beat> beatDetails = rp.parseBeatResponse(response);

                    listener.onBeatsDetailsReceived(beatDetails);
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

        requestQueue.add(request);
    }

}
