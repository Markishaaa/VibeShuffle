package rs.markisha.vibeshuffle.utils.callbacks;

import java.util.List;

import rs.markisha.vibeshuffle.model.Playlist;

public interface PlaylistDetailsListener {

    void onUserPlaylistsDetailsReceived(List<Playlist> playlists);
    void onPlaylistDetailsReceived(Playlist playlist, String type);

}
