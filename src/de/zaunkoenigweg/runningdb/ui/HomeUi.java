package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.Layout;
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
    }

    @Override
    public void show() {
    }

}
