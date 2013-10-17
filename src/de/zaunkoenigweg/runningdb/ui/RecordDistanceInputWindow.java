package de.zaunkoenigweg.runningdb.ui;

import java.io.Serializable;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.zaunkoenigweg.runningdb.domain.RecordDistance;

/**
 * Window to enter new record distance.
 * 
 * @author Nikolaus Winter
 */
public class RecordDistanceInputWindow extends Window {

    private static final long serialVersionUID = -6228525065980711482L;
    
    private static final DistanceConverter DISTANCE_CONVERTER = new DistanceConverter();

    private TextField textFieldDistance;
    private TextField textFieldLabel;
    private Button buttonSave;
    private Button buttonCancel;
    BeanItem<RecordDistance> recordDistance;


    /**
     * Creates a new window to edit record distance.
     * @param recordDistanceCreatedListener listener to specify actions following this dialog
     */
    private RecordDistanceInputWindow(final RecordDistanceCreatedListener recordDistanceCreatedListener) {
        
        this.recordDistance = new BeanItem<RecordDistance>(new RecordDistance());
        
        setCaption("Eingabe einer Strecke für die Bestzeiten");
        setClosable(false);
        setHeight("400px");
        setWidth("600px");
        setResizable(false);
        setModal(true);
        center();
        setStyleName("popupWindow");

        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        setContent(layout);
        
        // input "distance"
        this.textFieldDistance = ComponentFactory.createTextField("Strecke");
        this.textFieldDistance.setWidth("200px");
        this.textFieldDistance.setPropertyDataSource(this.recordDistance.getItemProperty("distance"));
        this.textFieldDistance.setImmediate(true);
        this.textFieldDistance.setConverter(DISTANCE_CONVERTER);
        this.textFieldDistance.setConversionError("Die Laufstrecke muss zwischen 100 m und 42.195 m liegen.");
        this.textFieldDistance.addValidator(new IntegerRangeValidator("Die Laufstrecke muss zwischen 100 m und 42.195 m liegen.", 100, 42195));
        this.textFieldDistance.setValidationVisible(true);
        this.textFieldDistance.setBuffered(true);
        this.textFieldDistance.focus();
        layout.addComponent(this.textFieldDistance);

        // input "label"
        this.textFieldLabel = ComponentFactory.createTextField("Bezeichnung");
        this.textFieldLabel.setWidth("400px");
        this.textFieldLabel.setPropertyDataSource(this.recordDistance.getItemProperty("label"));
        this.textFieldLabel.setImmediate(true);
        this.textFieldLabel.setBuffered(true);
        layout.addComponent(this.textFieldLabel);

        // button "save"
        buttonSave = ComponentFactory.createButton("Speichern");
        layout.addComponent(buttonSave);
        buttonSave.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                if(textFieldDistance.isValid()) {
                    textFieldDistance.commit();
                    textFieldLabel.commit();
                    if(recordDistance.getBean().getDistance()!=null) {
                        recordDistanceCreatedListener.recordDistanceCreated(recordDistance.getBean());
                        close();
                    }
                }
            }
        });

        // button "cancel"
        buttonCancel = ComponentFactory.createButton("Abbrechen");
        layout.addComponent(buttonCancel);
        buttonCancel.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        
    
    }
    
    /**
     * Opens a new window to edit record distance.
     * @param recordDistanceCreatedListener listener to specify actions following this dialog
     */
    public static void show(RecordDistanceCreatedListener recordDistanceCreatedListener) {
        RecordDistanceInputWindow window = new RecordDistanceInputWindow(recordDistanceCreatedListener);
        UI.getCurrent().addWindow(window);
    }

    /**
     * Listener to specify action to be executed after a record distance is created using this dialog. 
     * @author Nikolaus Winter
     */
    public interface RecordDistanceCreatedListener extends Serializable {
        
        /**
         * Action to be executed after a record distance is created using this dialog.
         */
        public void recordDistanceCreated(RecordDistance recordDistance);
        
    }

}
