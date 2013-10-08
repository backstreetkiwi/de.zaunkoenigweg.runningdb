package de.zaunkoenigweg.runningdb.domain;

import java.util.Date;

public class TrainingLogGenerator {
    
    private static final String[] ORTE = new String[] {"Rantzauer Forst", "Alsterwanderweg", "Sportplatz am See", "Stadtpark HH", "Stadtpark Norderstedt", "Straßenrunde Norderstedt", "München"};
    private static final String[] BEMERKUNGEN = new String[] {"super", "Schmerzen im Bein", "schlapp", "Nasenbluten", "etwas besser"};
    private static final int[] STRECKEN = new int[] {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
    private static final int[] ZEITEN = new int[] {300, 600, 900, 1200, 1500, 1800, 2100, 2400, 2700, 3000};

    public static TrainingLog generate() {
        
        TrainingLog trainingstagebuch = new TrainingLog();
        
        long datum = 1180389600000L;
        
        for (int i=0; i<350; i++) {
            
            Training training = new Training();
            training.setDate(new Date(datum + 5 * i * 86400000L));;
            training.setLocation(ORTE[i % ORTE.length]);;
            training.setComments(BEMERKUNGEN[i % BEMERKUNGEN.length]);;
            
            for (int j=0; j<((i%3)+1); j++) {
                int strecke = STRECKEN[(i+j) % STRECKEN.length] + 100 * ((i+3*j)%10);
                int zeit = ZEITEN[(i+j) % ZEITEN.length] + 20 * ((i+3*j)%10);
                Run lauf = new Run();
                lauf.setDistance(strecke);
                lauf.setTime(zeit);
                training.getRuns().add(lauf);
            }
            
            trainingstagebuch.addTraining(training);
            
        }
        
        return trainingstagebuch;
        
    }
    
}
