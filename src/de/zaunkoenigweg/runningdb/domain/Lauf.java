package de.zaunkoenigweg.runningdb.domain;

/**
 * Ein Lauf ist ein Teil eines Trainings.
 * 
 * @author Nikolaus Winter
 *
 */
public class Lauf {

    /**
     * Strecke in Metern
     */
    private Integer strecke;
    
    /**
     * Zeit in Sekunden
     */
    private Integer zeit;

    public Integer getStrecke() {
        return strecke;
    }

    public void setStrecke(Integer strecke) {
        this.strecke = strecke;
    }

    public Integer getZeit() {
        return zeit;
    }

    public void setZeit(Integer zeit) {
        this.zeit = zeit;
    }

}
