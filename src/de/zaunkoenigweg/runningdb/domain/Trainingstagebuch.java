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
public class Trainingstagebuch {
    
    /**
     * Liste aller Trainingseinheiten.
     */
    private List<Training> trainingseinheiten = new ArrayList<Training>();
    
    /**
     * Liste aller Schuhe.
     */
    private List<Schuh> schuhe = new ArrayList<Schuh>();
    
    /**
     * Sortierte Menge der Strecken, die in der Bestzeitenliste geführt werden.
     */
    private SortedSet<BestzeitStrecke> bestzeitStrecken = new TreeSet<BestzeitStrecke>(new Comparator<BestzeitStrecke>() {
        @Override
        public int compare(BestzeitStrecke strecke1, BestzeitStrecke strecke2) {
            return strecke1.getStrecke().compareTo(strecke2.getStrecke());
        }
    });
    
    
    // ------------------------------------------------------------------------------
    // Getter/Setter
    // ------------------------------------------------------------------------------

    public List<Training> getTrainingseinheiten() {
        return new ArrayList<Training>(this.trainingseinheiten);
    }
    
    public List<Schuh> getSchuhe() {
        return new ArrayList<Schuh>(this.schuhe);
    }
    
    public List<Schuh> getAktiveSchuhe() {
        List<Schuh> result = new ArrayList<Schuh>();
        for (Schuh schuh : this.schuhe) {
            if(schuh.isAktiv()) {
                result.add(schuh);
            }
        }
        return result;
    }
    
    public void addSchuh(Schuh schuh) {
        
        // Falls der Schuh schon eine ID mitbringt, wird er nur eingefügt, 
        // falls die ID noch nicht besteht
        if(schuh.getId()!=0) {
            
            // TODO: Test auf doppelte ID
            this.schuhe.add(schuh);
            
        // Falls der Schuh noch keine ID hat, bekommt er die nächste freie ID
        } else {
            
            // Ermitteln der max. ID der bisher existierenden Schuhe
            int maxId = 0;
            for (Schuh bestehenderSchuh : this.schuhe) {
                maxId = Math.max(maxId, bestehenderSchuh.getId());
            }
            
            // Schuh mit neuer ID versehen und speichern
            schuh.setId(maxId+1);
            this.schuhe.add(schuh);

        }
        
    }
    
    public Schuh getSchuh(Integer id) {
        
        if(id==null) {
            return null;
        }
        
        Schuh result = null;
        
        List<Schuh> schuhe = this.getSchuhe();
        int i=0;
        Schuh schuh;
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
    
    public List<BestzeitStrecke> getBestzeitStrecken() {
        return new ArrayList<BestzeitStrecke>(this.bestzeitStrecken);
    }
    
    public void addBestzeitStrecke(BestzeitStrecke bestzeitStrecke) {
        this.bestzeitStrecken.add(bestzeitStrecke);
    }
    
    public void removeBestzeitStrecke(BestzeitStrecke bestzeitStrecke) {
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
    
    public List<BestzeitInfo> getBestzeiten() {
        
        List<BestzeitInfo> result = new ArrayList<BestzeitInfo>();
        
        for (BestzeitStrecke bestzeitStrecke : this.bestzeitStrecken) {
            BestzeitInfo bestzeitInfo = getBestzeitInfo(bestzeitStrecke);
            Collections.sort(bestzeitInfo.getLaeufe(), new Comparator<BestzeitInfo.BestzeitLauf>() {

                @Override
                public int compare(BestzeitInfo.BestzeitLauf lauf1, BestzeitInfo.BestzeitLauf lauf2) {
                    return lauf1.getZeit().compareTo(lauf2.getZeit());
                }
            });
            result.add(bestzeitInfo);
        }
        
        return result;
        
    }
    
    private BestzeitInfo getBestzeitInfo(BestzeitStrecke strecke) {
        
        final BestzeitInfo info = new BestzeitInfo(strecke);
        
        for (Training training : this.getTrainingseinheiten()) {
            for (Lauf lauf : training.getLaeufe()) {
                if(lauf.getStrecke().compareTo(strecke.getStrecke())==0) {
                    info.getLaeufe().add(new BestzeitInfo.BestzeitLauf(lauf.getZeit(), training));
                }
            }
        }
        
        
        return info;
    }
    
}
