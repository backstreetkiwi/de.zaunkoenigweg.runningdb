package de.zaunkoenigweg.runningdb.domain;



/**
 * Bean containing shoe bean plus statistics.
 * 
 * @author Nikolaus Winter
 */
public class ShoeInfo {

    private Shoe shoe;
    private Integer distance;
    
    public ShoeInfo(Shoe shoe, Integer distance) {
        super();
        this.shoe = shoe;
        this.distance = distance;
    }
    
    public Shoe getShoe() {
        return shoe;
    }
    
    public Integer getDistance() {
        return distance;
    }

}
