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
     * Creates default {@link Button} with given caption
     * @param caption caption
     * @return default {@link Button}
     */
    public static Button createButton(String caption) {
        Button button = new Button(caption);
        button.setStyleName("defaultButton");
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
