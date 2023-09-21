package rs.markisha.vibeshuffle.utils;

import java.util.List;

import rs.markisha.vibeshuffle.model.Track;

public class TrackUtils {

    public Track findTrackById(String id, List<Track> tracks) {
        for (Track t : tracks) {
            if (t.getId().equals(id))
                return t;
        }

        return null;
    }

}
