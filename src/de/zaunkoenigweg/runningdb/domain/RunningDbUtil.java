package de.zaunkoenigweg.runningdb.domain;

import java.math.BigInteger;
import java.util.List;

public class RunningDbUtil {

    public static Integer berechneSchnitt(Integer strecke, Integer zeit) {
        if(zeit==null || zeit.compareTo(0)<=0) {
            return 0;
        }
        return BigInteger.valueOf(zeit).multiply(BigInteger.valueOf(1000l)).divide(BigInteger.valueOf(strecke)).intValue();
    }
    
    public static Integer summeStrecke(List<Training> trainingseinheiten) {
        int strecke = 0;
        for (Training training : trainingseinheiten) {
            strecke += training.getStrecke();
        }
        return strecke;
    }
    
    public static Integer summeZeit(List<Training> trainingseinheiten) {
        int zeit = 0;
        for (Training training : trainingseinheiten) {
            zeit += training.getZeit();
        }
        return zeit;
    }
    
}
