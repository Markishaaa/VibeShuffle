package rs.markisha.vibeshuffle.utils.callbacks;

import java.util.List;

import rs.markisha.vibeshuffle.payload.PlaylistDetailsBuilder;

public interface PlaylistDetailsListener {

    void onUserPlaylistsDetailsReceived(List<PlaylistDetailsBuilder> playlists);
    void onPlaylistDetailsReceived(PlaylistDetailsBuilder playlist);

}
