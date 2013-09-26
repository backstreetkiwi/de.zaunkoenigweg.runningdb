package de.zaunkoenigweg.runningdb.ui;

import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

public class EmptyUi extends AbstractUi {

    private static final long serialVersionUID = 7252833037429831581L;

    public EmptyUi(Trainingstagebuch trainingstagebuch) {
        
        super(trainingstagebuch);
        
    }

    @Override
    public void show() {
    }

}
