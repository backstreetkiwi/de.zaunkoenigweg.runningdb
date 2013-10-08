package de.zaunkoenigweg.runningdb.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Ein Trainingstagebuch.
 * 
 * @author Nikolaus Winter
 *
 */
public class TrainingLog {
    
    /**
     * Liste aller Trainingseinheiten.
     */
    private List<Training> trainingseinheiten = new ArrayList<Training>();
    
    /**
     * Liste aller Schuhe.
     */
    private List<Shoe> schuhe = new ArrayList<Shoe>();
    
    /**
     * Sortierte Menge der Strecken, die in der Bestzeitenliste geführt werden.
     */
    private SortedSet<RecordDistance> bestzeitStrecken = new TreeSet<RecordDistance>(new Comparator<RecordDistance>() {
        @Override
        public int compare(RecordDistance strecke1, RecordDistance strecke2) {
            return strecke1.getStrecke().compareTo(strecke2.getStrecke());
        }
    });
    
    
    // ------------------------------------------------------------------------------
    // Getter/Setter
    // ------------------------------------------------------------------------------

    public List<Training> getTrainingseinheiten() {
        return new ArrayList<Training>(this.trainingseinheiten);
    }
    
    public List<Shoe> getSchuhe() {
        return new ArrayList<Shoe>(this.schuhe);
    }
    
    public List<Shoe> getAktiveSchuhe() {
        List<Shoe> result = new ArrayList<Shoe>();
        for (Shoe schuh : this.schuhe) {
            if(schuh.isActive()) {
                result.add(schuh);
            }
        }
        return result;
    }
    
    public List<ShoeInfo> getShoeInfo() {
        List<ShoeInfo> result = new ArrayList<ShoeInfo>();
        for (Shoe shoe : this.schuhe) {
            result.add(new ShoeInfo(shoe, getStrecke(shoe)));
        }
        return result;
    }
    
    public void addSchuh(Shoe schuh) {
        
        // Falls der Schuh schon eine ID mitbringt, wird er nur eingefügt, 
        // falls die ID noch nicht besteht
        if(schuh.getId()!=0) {
            
            // TODO: Test auf doppelte ID
            this.schuhe.add(schuh);
            
        // Falls der Schuh noch keine ID hat, bekommt er die nächste freie ID
        } else {
            
            // Ermitteln der max. ID der bisher existierenden Schuhe
            int maxId = 0;
            for (Shoe bestehenderSchuh : this.schuhe) {
                maxId = Math.max(maxId, bestehenderSchuh.getId());
            }
            
            // Schuh mit neuer ID versehen und speichern
            schuh.setId(maxId+1);
            this.schuhe.add(schuh);

        }
        
    }
    
    public Shoe getSchuh(Integer id) {
        
        if(id==null) {
            return null;
        }
        
        Shoe result = null;
        
        List<Shoe> schuhe = this.getSchuhe();
        int i=0;
        Shoe schuh;
        while(result==null && i<schuhe.size()) {
            schuh = schuhe.get(i++);
            if (id.compareTo(schuh.getId())==0) {
                result=schuh;
            }
        }
        
        return result;
        
    }
    
    public void addTraining(Training training) {

        this.trainingseinheiten.add(training);
        
    }
    
    public List<RecordDistance> getBestzeitStrecken() {
        return new ArrayList<RecordDistance>(this.bestzeitStrecken);
    }
    
    public void addBestzeitStrecke(RecordDistance bestzeitStrecke) {
        this.bestzeitStrecken.add(bestzeitStrecke);
    }
    
    public void removeBestzeitStrecke(RecordDistance bestzeitStrecke) {
        this.bestzeitStrecken.remove(bestzeitStrecke);
    }
    
    
    // ------------------------------------------------------------------------------
    // Reporting-Funktionen
    // ------------------------------------------------------------------------------
    
    /**
     * Gibt die Summe der Strecke aller Trainingseinheiten zurück.
     * 
     * @return Summe der Strecke aller Trainingseinheiten
     */
    public Integer getStrecke() {
        int strecke = 0;
        for (Training training: this.trainingseinheiten) {
            strecke += training.getStrecke();
        }
        return strecke;
    }
    
    /**
     * Gibt die Summe der Strecke aller Trainingseinheiten mit dem angegebenen Schuh zurück.
     * 
     * @return Summe der Strecke aller Trainingseinheiten mit dem angegebenen Schuh
     */
    public Integer getStrecke(Shoe shoe) {
        int strecke = 0;
        for (Training training: this.trainingseinheiten) {
            if(training.getSchuh()!=null && training.getSchuh().compareTo(shoe.getId())==0) {
                strecke += training.getStrecke();
            }
        }
        return strecke;
    }
    
    /**
     * Gibt die Summe der Zeit aller Trainingseinheiten zurück.
     * 
     * @return Summe der Zeit aller Trainingseinheiten
     */
    public Integer getZeit() {
        
        int zeit = 0;
        for (Training training: this.trainingseinheiten) {
            zeit += training.getZeit();
        }
        return zeit;
    }
    
    public List<Training> getTrainingseinheiten(int jahr, int monat) {
        List<Training> result = new ArrayList<Training>();
        
        for (Training training : this.trainingseinheiten) {
            
            Calendar calli = Calendar.getInstance();
            calli.setTime(training.getDatum());
            
            if(calli.get(Calendar.YEAR)==jahr && calli.get(Calendar.MONTH)==monat) {
                result.add(training);
            }
            
        }
        
        return result;
    }
    
    public List<RecordInfo> getBestzeiten() {
        
        List<RecordInfo> result = new ArrayList<RecordInfo>();
        
        for (RecordDistance bestzeitStrecke : this.bestzeitStrecken) {
            RecordInfo bestzeitInfo = getBestzeitInfo(bestzeitStrecke);
            Collections.sort(bestzeitInfo.getLaeufe(), new Comparator<RecordInfo.BestzeitLauf>() {

                @Override
                public int compare(RecordInfo.BestzeitLauf lauf1, RecordInfo.BestzeitLauf lauf2) {
                    return lauf1.getZeit().compareTo(lauf2.getZeit());
                }
            });
            result.add(bestzeitInfo);
        }
        
        return result;
        
    }
    
    private RecordInfo getBestzeitInfo(RecordDistance strecke) {
        
        final RecordInfo info = new RecordInfo(strecke);
        
        for (Training training : this.getTrainingseinheiten()) {
            for (Run lauf : training.getLaeufe()) {
                if(lauf.getStrecke().compareTo(strecke.getStrecke())==0) {
                    info.getLaeufe().add(new RecordInfo.BestzeitLauf(lauf.getZeit(), training));
                }
            }
        }
        
        
        return info;
    }
    
}
