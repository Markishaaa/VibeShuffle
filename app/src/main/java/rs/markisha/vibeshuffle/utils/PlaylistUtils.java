package rs.markisha.vibeshuffle.utils;

import java.util.ArrayList;
import java.util.List;
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

    public List<Playlist> filterPlaylists(List<Playlist> playlists, String id) {
        List<Playlist> ps = new ArrayList<>();

        for (Playlist p : playlists) {
            ps.add(p);
        }

        for (Playlist p : playlists) {
            if (p.getTracks() == null || p.getTracks().size() == 0) {
                ps.remove(p);
            } else if (!p.getAuthorId().equals(id)) {
                ps.remove(p);
            }
        }

        return ps;
    }

}
