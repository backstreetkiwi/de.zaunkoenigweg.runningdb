package de.zaunkoenigweg.runningdb.domain;


/**
 * Strecke, die in der Bestzeitenliste gewertet wird.
 * 
 * @author Nikolaus Winter
 *
 */
public class RecordDistance {
    
    private Integer strecke;
    private String bezeichnung = "";
    
    public Integer getStrecke() {
        return strecke;
    }
    public void setStrecke(Integer strecke) {
        this.strecke = strecke;
    }
    public String getBezeichnung() {
        return bezeichnung;
    }
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

}
