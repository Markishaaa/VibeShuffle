package rs.markisha.vibeshuffle.utils.network.managers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import rs.markisha.vibeshuffle.utils.network.SpotifyApiHelper;

public class PlaylistManager extends SpotifyApiHelper {

    private final RequestQueue requestQueue;

    public PlaylistManager(Context context, String accessToken) {
        super(context, accessToken);
        this.requestQueue = Volley.newRequestQueue(context);
    }

}
