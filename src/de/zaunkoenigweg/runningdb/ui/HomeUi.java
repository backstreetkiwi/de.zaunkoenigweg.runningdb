package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Home UI of RunningDB.
 * 
 * @author Nikolaus Winter
 */
public class HomeUi extends AbstractUi {

    private static final long serialVersionUID = 6470235043474537736L;
    
    /**
     * Create HomeUI.
     */
    public HomeUi() {
        Layout layout = new VerticalLayout();
        setCompositionRoot(layout);
        
        Panel panelTitle = new Panel();
        layout.addComponent(panelTitle);
        
        panelTitle.setContent(new Image(null, new ThemeResource("images/splash.png")));
        panelTitle.setStyleName("panelTitle");
    }

    @Override
    public void show() {
    }

}
