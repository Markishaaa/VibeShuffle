package rs.markisha.vibeshuffle.utils.network;

import android.content.Context;

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