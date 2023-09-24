package rs.markisha.vibeshuffle.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import rs.markisha.vibeshuffle.fragments.TrackPlayingFragment;
import rs.markisha.vibeshuffle.fragments.VolumeFragment;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.model.Track;

public class PlayViewModel extends ViewModel {

    private Track currentTrack;

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }
}
