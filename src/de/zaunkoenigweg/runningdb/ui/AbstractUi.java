package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.CustomComponent;

import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

public abstract class AbstractUi extends CustomComponent {

    private static final long serialVersionUID = 6810094874228420706L;
    
    // Trainingstagebuch
    protected Trainingstagebuch trainingstagebuch;

    public AbstractUi(Trainingstagebuch trainingstagebuch) {
        this.trainingstagebuch = trainingstagebuch;
    }

    public abstract void show();

}
