package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomComponent;

import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * UI for RunningDB.
 * 
 * @author Nikolaus Winter
 */
public abstract class AbstractUi extends CustomComponent {

    private static final long serialVersionUID = 6810094874228420706L;
    
    /**
     * Receives the {@link TrainingLog} for this session.
     * 
     * If the {@link VaadinSession} holds no suitable attribut, a new {@link TrainingLog} is created.
     * 
     * @return {@link TrainingLog} for this session
     */
    public TrainingLog getTrainingLog() {
        Object trainingLog = VaadinSession.getCurrent().getAttribute("trainingLog");
        if(trainingLog instanceof TrainingLog) {
            return (TrainingLog)trainingLog;
        }
        TrainingLog newTrainingLog = new TrainingLog();
        setTrainingLog(newTrainingLog);
        return newTrainingLog;
    }

    /**
     * Sets the {@link TrainingLog} for this session.
     * @param trainingLog {@link TrainingLog}
     */
    public void setTrainingLog(TrainingLog trainingLog) {
        VaadinSession.getCurrent().setAttribute("trainingLog", trainingLog);
    }
    
    /**
     * This callback method is called whenever UI comes to the surface.
     */
    public abstract void show();

}
