package rs.markisha.vibeshuffle.utils.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import rs.markisha.vibeshuffle.payload.PlaybackDetailsBuilder;
import rs.markisha.vibeshuffle.utils.callbacks.PlaybackDetailsListener;

public class SpotifyApiHelper {
    protected static final String BASE_URL = "https://api.spotify.com/v1/";
    private final Context context;
    private final String accessToken;

    public SpotifyApiHelper(Context context, String accessToken) {
        this.context = context;
        this.accessToken = accessToken;
    }

    protected String getAccessToken() {
        return accessToken;
    }

}