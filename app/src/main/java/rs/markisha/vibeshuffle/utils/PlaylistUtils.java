package rs.markisha.vibeshuffle.utils;

import java.util.Random;

import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.model.Track;

public class PlaylistUtils {

    public int findTrackNumber(Playlist p, Track t) {
        for (int i = 0; i < p.getTracks().size(); i++) {
            if (t.equals(p.getTracks().get(i)))
                return i;
        }

        return -1;
    }

    public Track findRandomTrack(Playlist p) {
        Random random = new Random();

        int randomNumber = random.nextInt(p.getTracks().size());

        return p.getTracks().get(randomNumber);
    }

}
