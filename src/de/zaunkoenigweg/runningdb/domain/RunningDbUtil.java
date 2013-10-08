package de.zaunkoenigweg.runningdb.domain;

import java.math.BigInteger;
import java.util.List;

/**
 * Util for calculations within running training.
 * 
 * @author Nikolaus Winter
 */
public class RunningDbUtil {

    /**
     * Calculates pace [min/km] for given running distance and time.
     * @param distance running distance
     * @param time elapsed time for run
     * @return running pace [min/km]
     */
    public static Integer getPace(Integer distance, Integer time) {
        if(time==null || time.compareTo(0)<=0) {
            return 0;
        }
        return BigInteger.valueOf(time).multiply(BigInteger.valueOf(1000l)).divide(BigInteger.valueOf(distance)).intValue();
    }
    
    /**
     * Calculates the sum of the distance of all the training sessions in the given list.
     * @param trainings list of training sessions
     * @return sum of the distance
     */
    public static Integer sumDistance(List<Training> trainings) {
        int distance = 0;
        for (Training training : trainings) {
            distance += training.getDistance();
        }
        return distance;
    }
    
    /**
     * Calculates the sum of the elapsed time of all the training sessions in the given list.
     * @param trainings list of training sessions
     * @return sum of the elapsed time
     */
    public static Integer sumTime(List<Training> trainings) {
        int time = 0;
        for (Training training : trainings) {
            time += training.getTime();
        }
        return time;
    }
    
}
