package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;

import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * Home UI of RunningDB.
 * 
 * @author Nikolaus Winter
 */
public class HomeUi extends AbstractUi {

    private static final long serialVersionUID = 6470235043474537736L;
    
    Button buttonAddTraining;

    /**
     * Create HomeUI.
     * 
     * @param trainingLog training log to work with
     */
    public HomeUi(TrainingLog trainingLog) {
        
        super(trainingLog);
        
        Layout layout = new FormLayout();
        setCompositionRoot(layout);
        
        this.buttonAddTraining = new Button("Training erfassen");
        layout.addComponent(buttonAddTraining);
        
        buttonAddTraining.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 4991055405692970134L;

            @Override
            public void buttonClick(ClickEvent event) {
                EditTrainingWindow.show(HomeUi.this.trainingstagebuch, new EditTrainingWindow.TrainingCreatedListener() {
                    
                    private static final long serialVersionUID = 414496460728242231L;

                    @Override
                    public void trainingCreated(Training training) {
                        HomeUi.this.trainingstagebuch.addTraining(training);
                    }
                });
            }
        });
    }

    @Override
    public void show() {
    }

}
