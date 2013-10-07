package de.zaunkoenigweg.runningdb.ui;

import java.io.Serializable;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.zaunkoenigweg.runningdb.domain.Lauf;

/**
 * Window to add new run.
 * 
 * @author Nikolaus Winter
 */
public class EditRunWindow extends Window {

    private static final long serialVersionUID = 6366542591098712326L;

    private StreckeConverter distanceConverter = new StreckeConverter();
    private ZeitConverter timeConverter = new ZeitConverter();

    private TextField textFieldDistance;
    private TextField textFieldTime;
    private Button buttonSave;
    private Button buttonCancel;
    
    private BeanItem<Lauf> run;
    
    private RunCreatedListener runCreatedListener;

    /**
     * Private Constructor, used only by {@link #show(RunCreatedListener)}
     * 
     * @param runCreatedListener listener to specify actions following this dialog
     */
    private EditRunWindow(RunCreatedListener runCreatedListener) {
        
        this.runCreatedListener = runCreatedListener;
        
        // create new empty run bean
        this.run = new BeanItem<Lauf>(new Lauf());
        
        setCaption("Eingabe eines Trainingslaufs");
        setClosable(false);
        setHeight("400px");
        setWidth("500px");
        setResizable(false);
        setModal(true);
        center();

        Layout layout = new FormLayout();
        setContent(layout);
        
        
        // input field "distance"
        this.textFieldDistance = new TextField("Strecke");
        layout.addComponent(this.textFieldDistance);
        this.textFieldDistance.setWidth("200px");
        this.textFieldDistance.setPropertyDataSource(this.run.getItemProperty("strecke"));
        this.textFieldDistance.setImmediate(true);
        this.textFieldDistance.setConverter(this.distanceConverter);
        this.textFieldDistance.setConversionError("Die Laufstrecke muss zwischen 100 m und 42.195 m liegen.");
        this.textFieldDistance.addValidator(new IntegerRangeValidator("Die Laufstrecke muss zwischen 100 m und 42.195 m liegen.", 100, 42195));
        this.textFieldDistance.setValidationVisible(true);
        this.textFieldDistance.setBuffered(true);
        this.textFieldDistance.focus();

        
        // input field "time"
        this.textFieldTime = new TextField("Zeit");
        layout.addComponent(this.textFieldTime);
        this.textFieldTime.setWidth("200px");
        this.textFieldTime.setPropertyDataSource(this.run.getItemProperty("zeit"));
        this.textFieldTime.setImmediate(true);
        this.textFieldTime.setConverter(this.timeConverter);
        this.textFieldTime.setConversionError("Bitte geben Sie einen gültigen Zeitwert ein.");
        this.textFieldTime.addValidator(new IntegerRangeValidator("Die Zeit muss zwischen 1:00 Minute und 9:59:59 Stunden liegen.", 60, 35999));
        this.textFieldTime.setValidationVisible(true);

        
        // button "save" calls listener callback and closes window
        buttonSave = new Button("Speichern");
        layout.addComponent(buttonSave);
        buttonSave.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                if(textFieldDistance.isValid() && textFieldTime.isValid()) {
                    textFieldDistance.commit();
                    textFieldTime.commit();
                    if(run.getBean().getStrecke()!=null && run.getBean().getZeit()!=null) {
                        EditRunWindow.this.runCreatedListener.runCreated(run.getBean());
                        close();
                    }
                }
            }
        });

        
        // button "cancel" closes window
        buttonCancel = new Button("Abbrechen");
        layout.addComponent(buttonCancel);
        buttonCancel.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                close();
            }
        });
    
    }
    
    
    /**
     * Opens a new window to edit run.
     * @param runCreatedListener listener to specify actions following this dialog
     */
    public static void show(RunCreatedListener runCreatedListener) {
        EditRunWindow window = new EditRunWindow(runCreatedListener);
        UI.getCurrent().addWindow(window);
    }
    
    
    /**
     * Listener to specify actions to be executed after a run is created using this dialog. 
     */
    public interface RunCreatedListener extends Serializable {
        
        /**
         * Action to be executed after a run is created using this dialog.
         */
        public void runCreated(Lauf run);
        
    }
    
}
