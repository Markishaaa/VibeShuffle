package rs.markisha.vibeshuffle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import rs.markisha.vibeshuffle.BuildConfig;
import rs.markisha.vibeshuffle.R;

public class AuthActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(BuildConfig.SPOTIFY_CLIENT_ID, AuthorizationResponse.Type.TOKEN, BuildConfig.SPOTIFY_REDIRECT_URI);

        builder.setScopes(new String[]{
                "streaming",
                "user-read-playback-state",
                "user-modify-playback-state",
                "playlist-modify-public",
                "playlist-modify-private",
                "playlist-read-private"
        });
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    String accessToken = response.getAccessToken();

                    Log.d("SpotifyAuth", "redirection to main");

                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("TOKEN", accessToken);
                    startActivity(i);

                    finish();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.e("SpotifyAuth", response.getError());
                    Toast.makeText(this, "Spotify auth unavailable", Toast.LENGTH_LONG).show();
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d("SpotifyAuth", "other");
                    // Handle other cases
            }
        }
    }
}