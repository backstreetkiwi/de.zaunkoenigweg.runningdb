package de.zaunkoenigweg.runningdb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Eine Trainingseinheit.
 * 
 * @author Nikolaus Winter
 *
 */
public class Training {
    
    private Date datum;
    private String ort = "";
    private String bemerkungen = "";
    private Integer schuh = 0;
    private List<Lauf> laeufe = new ArrayList<Lauf>();
    
    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }
    
    public String getOrt() {
        return ort;
    }
    
    public void setOrt(String ort) {
        this.ort = ort;
    }
    
    public String getBemerkungen() {
        return bemerkungen;
    }
    
    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }
    
    public Integer getSchuh() {
        return schuh;
    }

    public void setSchuh(Integer schuh) {
        this.schuh = schuh;
    }

    public List<Lauf> getLaeufe() {
        return laeufe;
    }
    
    /**
     * Gibt die Summe der Strecke zurück.
     * 
     * @return Summe der Strecke
     */
    public Integer getStrecke() {
        int strecke = 0;
        for (Lauf lauf: this.laeufe) {
            strecke += lauf.getStrecke();
        }
        return strecke;
    }
    
    /**
     * Gibt die Summe der Zeit zurück.
     * 
     * @return Summe der Zeit
     */
    public Integer getZeit() {
        int zeit = 0;
        for (Lauf lauf: this.laeufe) {
            zeit += lauf.getZeit();
        }
        return zeit;
    }
    
    /**
     * Prüft, ob das Training valide ist und gespeichert werden kann.
     * 
     * @return Ist das Training valide ist und kann gespeichert werden?
     */
    public boolean isValid() {
        
        if (StringUtils.isBlank(this.ort)) {
            return false;
        }
        
        if(this.datum==null) {
            return false;
        }
        
        if(this.schuh==null || this.schuh.intValue()<=0) {
            return false;
        }
        
        if(this.laeufe.isEmpty()) {
            return false;
        }
        
        return true;
    }
    
}
