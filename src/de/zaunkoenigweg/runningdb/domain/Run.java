package de.zaunkoenigweg.runningdb.domain;

/**
 * Single run as part of a training session.
 * 
 * @author Nikolaus Winter
 */
public class Run {

    /**
     * distance [meters]
     */
    private Integer distance;
    
    /**
     * time [seconds]
     */
    private Integer time;

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}
