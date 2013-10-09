package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.Button;

/**
 * Factory to create new UI components
 * 
 * @author Nikolaus Winter
 *
 */
public class ComponentFactory {
    
    /**
     * Creates regular {@link Button} with given caption
     * @param caption caption
     * @return regular {@link Button}
     */
    public static Button createRegularButton(String caption) {
        Button button = new Button(caption);
        button.setStyleName("regularButton");
        return button;
    }

}
