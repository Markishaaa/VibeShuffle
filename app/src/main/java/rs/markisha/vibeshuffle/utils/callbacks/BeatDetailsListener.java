package rs.markisha.vibeshuffle.utils.callbacks;

import java.util.List;

import rs.markisha.vibeshuffle.model.Beat;

public interface BeatDetailsListener {

    void onBeatsDetailsReceived(List<Beat> beats);

}
