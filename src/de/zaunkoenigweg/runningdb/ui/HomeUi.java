package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;

import de.zaunkoenigweg.runningdb.domain.Training;

/**
 * Home UI of RunningDB.
 * 
 * @author Nikolaus Winter
 */
@Deprecated
public class HomeUi extends AbstractUi {

    private static final long serialVersionUID = 6470235043474537736L;
    
    Button buttonAddTraining;

    /**
     * Create HomeUI.
     */
    public HomeUi() {
        
        Layout layout = new FormLayout();
        setCompositionRoot(layout);
        
        this.buttonAddTraining = new Button("Training erfassen");
        layout.addComponent(buttonAddTraining);
        
        buttonAddTraining.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 4991055405692970134L;

            @Override
            public void buttonClick(ClickEvent event) {
                EditTrainingWindow.show(getTrainingLog(), new EditTrainingWindow.TrainingCreatedListener() {
                    
                    private static final long serialVersionUID = 414496460728242231L;

                    @Override
                    public void trainingCreated(Training training) {
                        getTrainingLog().addTraining(training);
                    }
                });
            }
        });
    }

    @Override
    public void show() {
    }

}
