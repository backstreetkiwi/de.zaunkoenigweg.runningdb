package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.CustomComponent;

import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * UI for RunningDB.
 * 
 * @author Nikolaus Winter
 */
public abstract class AbstractUi extends CustomComponent {

    private static final long serialVersionUID = 6810094874228420706L;
    
    protected TrainingLog trainingLog;

    public AbstractUi(TrainingLog trainingLog) {
        this.trainingLog = trainingLog;
    }

    /**
     * This callback method is called whenever UI comes to the surface.
     */
    public abstract void show();

}
