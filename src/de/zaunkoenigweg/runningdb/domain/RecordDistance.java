package de.zaunkoenigweg.runningdb.domain;

/**
 * Running distance for which records are tracked.
 * 
 * @author Nikolaus Winter
 */
public class RecordDistance {
    
    private Integer distance;
    private String label = "";
    
    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }

}
