package de.zaunkoenigweg.runningdb.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.zaunkoenigweg.runningdb.ui.DistanceConverter;

/**
 * This object contains a {@link RecordDistance} and a list of {@link RecordRun}s with matching distance.
 * 
 * @author Nikolaus Winter
 */
public class RecordInfo {

    private RecordDistance recordDistance;

    private List<RecordRun> recordRuns = new ArrayList<RecordRun>();

    public RecordInfo(RecordDistance recordDistance) {
        super();
        this.recordDistance = recordDistance;
    }

    public RecordDistance getRecordDistance() {
        return recordDistance;
    }

    public List<RecordRun> getRecordRuns() {
        return recordRuns;
    }
    
    public int getRunCount() {
        return this.recordRuns.size();
    }
    
    public String getTeaser() {
        String teaser;
        String distance = new DistanceConverter().convertToPresentation(recordDistance.getDistance(), String.class, null);
        if (StringUtils.isNotBlank(recordDistance.getLabel())) {
            teaser = String.format("%s: %s Meter (%d mal gelaufen)", recordDistance.getLabel(), distance, getRunCount());
        } else {
            teaser = String.format("%s Meter (%d mal gelaufen)", distance, getRunCount());
        }
        return teaser;
    }

    /**
     * This object contains the time and a reference to the training session of a run that ist
     * kept in a record list.
     */
    public static class RecordRun {
        
        private Integer time;
        private Training training;
        
        public RecordRun(Integer time, Training training) {
            super();
            this.time = time;
            this.training = training;
        }
        
        public Integer getTime() {
            return time;
        }
        
        public Training getTraining() {
            return training;
        }
        
    }
    
}
