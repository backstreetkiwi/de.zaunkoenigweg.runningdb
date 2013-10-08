package de.zaunkoenigweg.runningdb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Training session.
 * 
 * @author Nikolaus Winter
 */
public class Training {
    
    private Date date;
    private String location = "";
    private String comments = "";
    private Integer shoe = 0;
    private List<Run> runs = new ArrayList<Run>();
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Integer getShoe() {
        return shoe;
    }

    public void setShoe(Integer shoe) {
        this.shoe = shoe;
    }

    public List<Run> getRuns() {
        return runs;
    }
    
    /**
     * Returns sum of distance of all runs.
     * 
     * @return sum of distance
     */
    public Integer getDistance() {
        int distance = 0;
        for (Run run: this.runs) {
            distance += run.getDistance();
        }
        return distance;
    }
    
    /**
     * Returns sum of elapsed time of all runs.
     * 
     * @return sum of elapsed time
     */
    public Integer getTime() {
        int time = 0;
        for (Run run: this.runs) {
            time += run.getTime();
        }
        return time;
    }
    
    /**
     * Checks training session for validity
     * 
     * @return Is the training session data valid, i.e. ready to be saved.
     */
    public boolean isValid() {
        
        if (StringUtils.isBlank(this.location)) {
            return false;
        }
        
        if(this.date==null) {
            return false;
        }
        
        if(this.shoe==null || this.shoe.intValue()<=0) {
            return false;
        }
        
        if(this.runs.isEmpty()) {
            return false;
        }
        
        return true;
    }
    
}
