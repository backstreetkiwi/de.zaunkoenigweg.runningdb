package de.zaunkoenigweg.runningdb.domain;

import java.util.ArrayList;
import java.util.List;

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
