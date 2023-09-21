package rs.markisha.vibeshuffle.model;

public class Beat {

    private double start;
    private double duration;
    private double confidence;

    public Beat(double start, double duration, double confidence) {
        this.start = start;
        this.duration = duration;
        this.confidence = confidence;
    }

    public double getStart() {
        return start;
    }

    public double getDuration() {
        return duration;
    }

    public double getConfidence() {
        return confidence;
    }

}
