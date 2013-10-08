package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.CustomComponent;

import de.zaunkoenigweg.runningdb.domain.TrainingLog;

public abstract class AbstractUi extends CustomComponent {

    private static final long serialVersionUID = 6810094874228420706L;
    
    // Trainingstagebuch
    protected TrainingLog trainingstagebuch;

    public AbstractUi(TrainingLog trainingstagebuch) {
        this.trainingstagebuch = trainingstagebuch;
    }

    public abstract void show();

}
