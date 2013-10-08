package de.zaunkoenigweg.runningdb.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Informationen zu einer Bestzeit
 * 
 * @author Nikolaus Winter
 *
 */
public class RecordInfo {

    private RecordDistance strecke;

    private List<BestzeitLauf> laeufe = new ArrayList<BestzeitLauf>();

    public RecordInfo(RecordDistance strecke) {
        super();
        this.strecke = strecke;
    }

    public RecordDistance getStrecke() {
        return strecke;
    }

    public List<BestzeitLauf> getLaeufe() {
        return laeufe;
    }
    
    public int getAnzahl() {
        return this.laeufe.size();
    }

    public static class BestzeitLauf {
        
        private Integer zeit;
        private Training training;
        
        public BestzeitLauf(Integer zeit, Training training) {
            super();
            this.zeit = zeit;
            this.training = training;
        }
        
        public Integer getZeit() {
            return zeit;
        }
        
        public Training getTraining() {
            return training;
        }
        
    }
    
}
