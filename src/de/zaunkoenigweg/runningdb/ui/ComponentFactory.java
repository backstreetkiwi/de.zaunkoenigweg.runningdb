package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;

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

    /**
     * Creates default {@link Panel}
     * @return default {@link Panel}
     */
    public static Panel createPanel() {
        Panel panel = new Panel();
        panel.setStyleName("defaultPanel");
        return panel;
    }
    
}
