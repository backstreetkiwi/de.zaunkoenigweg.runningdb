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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((distance == null) ? 0 : distance.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RecordDistance other = (RecordDistance) obj;
        if (distance == null) {
            if (other.distance != null)
                return false;
        } else if (!distance.equals(other.distance))
            return false;
        return true;
    }

    
    
}
