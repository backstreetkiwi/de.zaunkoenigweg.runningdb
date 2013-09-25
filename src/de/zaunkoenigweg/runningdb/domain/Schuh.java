package de.zaunkoenigweg.runningdb.domain;

import org.apache.commons.lang3.StringUtils;


/**
 * Laufschuh.
 * 
 * @author Nikolaus Winter
 */
public class Schuh {

    /**
     * ID.
     * 0 bedeutet: noch nicht gespeichert
     */
    private int id = 0;

    /**
     * Name des Herstellers.
     */
    private String hersteller = "";
    
    /**
     * Modellbezeichnung.
     */
    private String modell = "";
    
    /**
     * Kaufdatum.
     * Dies ist eine Zeichenkette, weil bei älteren Schuhen lediglich das Jahr bekannt ist.
     */
    private String kaufdatum = "";
    
    /**
     * Preis.
     */
    private String preis = "";
    
    /**
     * Bemerkungen.
     */
    private String bemerkungen = "";
    
    /**
     * Ist der Schuh noch im Dienst?
     */
    private boolean aktiv = true;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public String getKaufdatum() {
        return kaufdatum;
    }

    public void setKaufdatum(String kaufdatum) {
        this.kaufdatum = kaufdatum;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }
    
    public String getPreis() {
        return preis;
    }

    public void setPreis(String preis) {
        this.preis = preis;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }
    
    public String getKurzbezeichnung() {
        return String.format("%s (%s)", this.modell, this.hersteller);
    }

    /**
     * Prüft, ob das Training valide ist und gespeichert werden kann.
     * 
     * @return Ist das Training valide ist und kann gespeichert werden?
     */
    public boolean isValid() {
        
        if (StringUtils.isBlank(this.hersteller)) {
            return false;
        }
        
        if (StringUtils.isBlank(this.modell)) {
            return false;
        }
        
        if (StringUtils.isBlank(this.kaufdatum)) {
            return false;
        }
        
        return true;
    }
}
