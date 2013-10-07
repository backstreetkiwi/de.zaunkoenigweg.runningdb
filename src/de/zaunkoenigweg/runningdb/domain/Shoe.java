package de.zaunkoenigweg.runningdb.domain;

import org.apache.commons.lang3.StringUtils;


/**
 * Running shoe.
 * 
 * @author Nikolaus Winter
 */
public class Shoe {

    /**
     * ID.
     * meaning of <=0: not saved
     */
    private int id = 0;

    /**
     * brand (e.g. Adidas, Asics)
     */
    private String brand = "";
    
    /**
     * model.
     */
    private String model = "";
    
    /**
     * date of purchase.
     * This attribute is held as a string of characters to allow vague values.
     */
    private String dateOfPurchase = "";
    
    /**
     * price.
     */
    private String price = "";
    
    /**
     * comments.
     */
    private String comments = "";
    
    /**
     * Is the shoe still used?
     */
    private boolean active = true;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getShortname() {
        return String.format("%s (%s)", this.model, this.brand);
    }

    /**
     * validity check.
     * 
     * @return Is the shoe valid and therefore ready to be saved?
     */
    public boolean isValid() {
        
        if (StringUtils.isBlank(this.brand)) {
            return false;
        }
        
        if (StringUtils.isBlank(this.model)) {
            return false;
        }
        
        if (StringUtils.isBlank(this.dateOfPurchase)) {
            return false;
        }
        
        return true;
    }
}
