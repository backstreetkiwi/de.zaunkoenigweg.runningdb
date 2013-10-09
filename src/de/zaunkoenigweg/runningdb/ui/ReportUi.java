package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;


/**
 * UI showing reports.
 * 
 * @author Nikolaus Winter
 */
public class ReportUi extends AbstractUi {

    private static final long serialVersionUID = 6912936199154704718L;
    
    private Label labelTotalDistance;
    
    public ReportUi() {

        Layout layout = new FormLayout();
        setCompositionRoot(layout);

        labelTotalDistance = new Label("");
        layout.addComponent(labelTotalDistance);
    }

    @Override
    public void show() {
        labelTotalDistance.setCaption(String.format("Gesamtstrecke: %s Meter", new DistanceConverter().convertToPresentation(getTrainingLog().getDistance(), String.class, null)));
    }

}
