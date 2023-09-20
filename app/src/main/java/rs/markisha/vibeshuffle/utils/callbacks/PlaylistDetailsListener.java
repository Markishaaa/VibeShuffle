package rs.markisha.vibeshuffle.utils.callbacks;

import java.util.List;

import rs.markisha.vibeshuffle.payload.PlaybackDetailsBuilder;
import rs.markisha.vibeshuffle.payload.PlaylistDetailsBuilder;

public interface PlaylistDetailsListener {

    void onPlaylistDetailsRecieved(List<PlaylistDetailsBuilder> playlists);

}
