package rs.markisha.vibeshuffle.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.model.Track;

public class PlayViewModel extends ViewModel {

    private final MutableLiveData<PlayFragmentUIState> uiState;

    public PlayViewModel() {
        this.uiState = new MutableLiveData(
                new PlayFragmentUIState(null,
                        null,
                        true,
                        false
                ));
    }

    public MutableLiveData<PlayFragmentUIState> getUiState() {
        return uiState;
    }

    public void setUiState(Track currentTrack, Playlist currentPlaylist, boolean playing, boolean state) {
        uiState.setValue(new PlayFragmentUIState(
                currentTrack,
                currentPlaylist,
                playing,
                state
        ));
    }

}
