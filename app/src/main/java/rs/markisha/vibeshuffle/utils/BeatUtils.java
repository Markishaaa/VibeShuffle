package rs.markisha.vibeshuffle.utils;

import java.util.List;
import java.util.stream.Collectors;

import rs.markisha.vibeshuffle.model.Beat;

public class BeatUtils {

    public List<Integer> findBeatDropsStartTimesMs(List<Beat> beats) {
        if (beats == null || beats.isEmpty())
            return null;

        double maxDuration = beats.stream()
                .mapToDouble(Beat::getDuration)
                .max()
                .orElse(0.0);

        double maxConfidence = beats.stream()
                .mapToDouble(Beat::getDuration)
                .max()
                .orElse(0.0);

        double durationThreshold = 80 % maxDuration;
        double confidenceThreshold = 60 % maxConfidence;

        List<Integer> filteredStartTimesMs = beats.stream()
                .filter(beat -> beat.getDuration() >= durationThreshold && beat.getConfidence() >= confidenceThreshold)
                .map(beat -> (int) (beat.getStart() * 1000)) // Convert to milliseconds
                .collect(Collectors.toList());

        return filteredStartTimesMs;
    }

}
