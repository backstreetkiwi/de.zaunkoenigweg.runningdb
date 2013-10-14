package de.zaunkoenigweg.runningdb.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * List of  
 * 
 * @author Nikolaus Winter
 */
public class TrainingReport {

    private List<TrainingReportRow> reportRows = new ArrayList<TrainingReportRow>();
    
    private TrainingReportRow sumRow = null;
    
    public List<TrainingReportRow> getReportRows() {
        return reportRows;
    }
    
    public TrainingReportRow getSumRow() {
        return sumRow;
    }

    public void setSumRow(TrainingReportRow sumRow) {
        this.sumRow = sumRow;
    }

    /**
     * Aggregated training data for a single period (year or month). 
     */
    public static class TrainingReportRow {
        
        /**
         * Year.
         */
        private Integer year;
        
        /**
         * Month as counted by Java {@link Calendar}.
         * -1 means: this row holds data of a whole year.
         */
        private Integer month;
        
        /**
         * Number of training sessions in period
         */
        private Integer trainingCount;
        
        /**
         * Total distance
         */
        private Integer distance;
        
        /**
         * Total elapsed time
         */
        private Integer time;

        public TrainingReportRow(Integer year, Integer month, Integer trainingCount, Integer distance, Integer time) {
            super();
            this.year = year;
            this.month = month;
            this.trainingCount = trainingCount;
            this.distance = distance;
            this.time = time;
        }

        public Integer getYear() {
            return year;
        }

        public Integer getMonth() {
            return month;
        }

        public Integer getTrainingCount() {
            return trainingCount;
        }

        public Integer getDistance() {
            return distance;
        }

        public Integer getTime() {
            return time;
        }
        
        
        
    }
    
}
