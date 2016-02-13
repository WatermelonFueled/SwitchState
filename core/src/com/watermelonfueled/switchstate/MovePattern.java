package com.watermelonfueled.switchstate;

import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

/**
 * Pattern of movement for moving enemies/obstacles
 */
public class MovePattern {

    public enum LoopMode { CIRCULAR, PINGPONG, ONCE };  // 1>2>3>1~, 1>2>3>2>1~, 1>2>3.
    private LoopMode loopMode;
    private float duration;
    private Vector2[] points;                           // vertex points of the movement pattern
    private float[] segmentStartTimes, segmentDurations;

    /**
     * Constructor for MovePattern.
     */
    public MovePattern() { }

    /**
     * Sets the duration of the move pattern.
     * @param duration time duration (s)
     * @return the MovePattern for convenient method chaining.
     */
    public MovePattern setDuration(float duration) {
        this.duration = duration;
        if (points != null && segmentStartTimes == null){ calculateSegmentTimes(); }
        return this;
    }

    /**
     * Sets the loop mode to non-looping
     * @return the MovePattern for convenient method chaining.
     */
    public MovePattern setLoopOnce() {
        loopMode = LoopMode.ONCE;
        return this;
    }

    /**
     * Sets the loop mode to circular looping
     * @return the MovePattern for convenient method chaining.
     */
    public MovePattern setLoopCircular() {
        loopMode = LoopMode.CIRCULAR;
        return this;
    }

    /**
     * Sets the loop mode to ping pong looping
     * @return the MovePattern for convenient method chaining.
     */
    public MovePattern setLoopPingpong() {
        loopMode = LoopMode.PINGPONG;
        return this;
    }

    /**
     * Sets the vertices of the move pattern.
     * @param points x and y coordinates in the format of  x1,y1,x2,y2 etc
     * @return the MovePattern for convenient method chaining.
     */
    public MovePattern setPoints(float... points){
        switch (loopMode){
            case CIRCULAR:
                this.points = new Vector2[points.length / 2 + 1];
                break;
            case PINGPONG:
                this.points = new Vector2[points.length - 1];
                break;
            case ONCE:
                this.points = new Vector2[points.length / 2];
                break;
        }
        for (int i = 0; i < points.length; i += 2){
            this.points[i/2] = new Vector2(points[i],points[i+1]);
        }
        switch (loopMode){
            case CIRCULAR:
                this.points[points.length/2] = this.points[0];
                break;
            case PINGPONG:
                for (int i = points.length/2; i < points.length - 1; i++){
                    this.points[i] = this.points[this.points.length - i - 1];
                }
                break;
        }
        if (duration > 0 && segmentStartTimes == null){ calculateSegmentTimes(); }
        return this;
    }

    /**
     * Calculates the durations between points and start times of each segment. Only call after points and duration are set.
     */
    private void calculateSegmentTimes(){
        float distance = 0;
        segmentStartTimes = new float[points.length-1];
        segmentDurations = new float[segmentStartTimes.length];
        for (int i = 0; i < segmentStartTimes.length; i++){
            segmentStartTimes[i] = distance;
            segmentDurations[i] = points[i].dst(points[i+1]);
            distance += segmentDurations[i];
        }
        for (int i = 0; i < segmentStartTimes.length; i++){
            segmentStartTimes[i] *= duration / distance;
            segmentDurations[i] *= duration / distance;
        }
    }

    /**
     * Updates the inputted vector with coordinates of the move pattern according to how much time has past
     * @param time time since move pattern had started
     * @param vec vector to be updated
     * @return false if no update occurred because the move pattern had completed, otherwise true
     */
    public boolean update(float time, Vector2 vec){
        // return false if end of non looping pattern
        if (loopMode == LoopMode.ONCE && time > duration) { return false; }

        float progressTime = time % duration;           // time progressed in move pattern
        int i = segmentIndexSearch(progressTime);       // get index of current segment
        progressTime -= segmentStartTimes[i];           // time progressed in segment
        vec.set(points[i]).lerp(points[i+1], progressTime/segmentDurations[i]);

        /*
        // segments progressed in pattern
        float progress = (time % duration) / duration * (points.length - 1);
        // which segment (also starting vector2 index)
        int segment = MathUtils.floor(progress);
        // progress within segment
        float segmentProgress = progress - segment;
        // linear interpolation from start to end of segment
        vec.set(points[segment]).lerp(points[segment + 1], segmentProgress);
        */
        return true;
    }

    /**
     * Finds the index of the current segment according to the time.
     * @param time current time within pattern duration
     * @return index of current segment
     */
    private int segmentIndexSearch(float time){
        int index = Arrays.binarySearch(segmentStartTimes, time);
        if (index >= 0) {
            return index;
        } else {
            return -index - 2;
        }
    }
}
