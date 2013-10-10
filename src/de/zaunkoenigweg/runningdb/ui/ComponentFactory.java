package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

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
    
    /**
     * Creates light looking {@link Table}
     * @param caption caption
     * @param container Vaadin Data Container
     * @return light looking {@link Table}
     */
    public static Table createLightTable(String caption, Container container) {
        Table table = new Table(caption, container);
        table.setStyleName("lightTable");
        return table;
    }

}
