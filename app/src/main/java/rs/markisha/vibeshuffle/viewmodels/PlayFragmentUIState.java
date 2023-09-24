package rs.markisha.vibeshuffle.viewmodels;

import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.model.Track;

public class PlayFragmentUIState {

    private Track currentTrack;
    private Playlist currentPlaylist;

    private boolean playing;
    private boolean state;

    public PlayFragmentUIState(Track currentTrack, Playlist currentPlaylist, boolean playing, boolean state) {
        this.currentTrack = currentTrack;
        this.currentPlaylist = currentPlaylist;
        this.playing = playing;
        this.state = state;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean getState() {
        return state;
    }

}
