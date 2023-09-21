package rs.markisha.vibeshuffle.utils.network.managers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;

import rs.markisha.vibeshuffle.utils.network.SpotifyApiHelper;

public class TrackManager extends SpotifyApiHelper {

    private final RequestQueue requestQueue;

    public TrackManager(Context context, String accessToken) {
        super(context, accessToken);
        this.requestQueue = Volley.newRequestQueue(context);
    }



}
