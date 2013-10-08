package de.zaunkoenigweg.runningdb.ui;

import de.zaunkoenigweg.runningdb.domain.TrainingLog;

public class EmptyUi extends AbstractUi {

    private static final long serialVersionUID = 7252833037429831581L;

    public EmptyUi(TrainingLog trainingstagebuch) {
        
        super(trainingstagebuch);
        
    }

    @Override
    public void show() {
    }

}
