package de.zaunkoenigweg.runningdb.ui;

import java.io.Serializable;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.zaunkoenigweg.runningdb.domain.Lauf;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Shoe;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

/**
 * Window to add new training to log.
 * 
 * @author Nikolaus Winter
 */
public class EditTrainingWindow extends Window {

    private static final int TABLE_RUNS_MIN_PAGE_LENGTH = 3;

    private static final long serialVersionUID = 6366542591098712326L;

    private StreckeConverter distanceConverter = new StreckeConverter();
    private ZeitConverter timeConverter = new ZeitConverter();

    private TextField textFieldLocation;
    private DateField dateFieldDate;
    private ComboBox selectShoes;
    private TextArea textAreaComments;
    private Button buttonSave;
    private Button buttonCancel;
    private Table tableRuns;
    private Button buttonAddRun;

    private BeanItem<Training> training;
    
    private Trainingstagebuch trainingLog;
    private TrainingCreatedListener trainingCreatedListener;    

    /**
     * Private Constructor, used only by {@link #show(Trainingstagebuch, TrainingCreatedListener)}
     * @param trainingLog training log to work with
     * @param trainingCreatedListener listener to specify actions following this dialog
     */
    private EditTrainingWindow(Trainingstagebuch trainingLog, TrainingCreatedListener trainingCreatedListener) {
        
        this.trainingLog = trainingLog;
        this.trainingCreatedListener = trainingCreatedListener;
        
        setCaption("Eingabe eines Trainings");
        setClosable(false);
        setHeight("800px");
        setWidth("800px");
        setResizable(false);
        setModal(true);
        center();

        Layout layout = new FormLayout();
        setContent(layout);
        
        // input field "location"
        this.textFieldLocation = new TextField("Ort");
        layout.addComponent(textFieldLocation);
        this.textFieldLocation.setWidth("300px");
        this.textFieldLocation.setImmediate(true);

        // input field "date"
        this.dateFieldDate = new DateField("Datum");
        layout.addComponent(dateFieldDate);
        this.dateFieldDate.setWidth("300px");
        this.dateFieldDate.setDateFormat("dd.MM.yyyy");
        this.dateFieldDate.setImmediate(true);
        
        // choice "shoes"
        this.selectShoes = new ComboBox("Schuhe");
        layout.addComponent(selectShoes);
        this.selectShoes.setWidth("300px");
        this.selectShoes.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        this.selectShoes.setItemCaptionPropertyId("shortname");
        this.selectShoes.setInputPrompt("Bitte Schuh auswählen...");
        this.selectShoes.setImmediate(true);

        this.selectShoes.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = -4575515764282188221L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(selectShoes.getValue() instanceof Shoe) {
                    training.getBean().setSchuh(((Shoe)selectShoes.getValue()).getId());
                } else {
                    training.getBean().setSchuh(0);
                }
            }
        });

        // input field "comments"
        this.textAreaComments = new TextArea("Bemerkungen");
        this.textAreaComments.setWidth("300px");
        this.textAreaComments.setHeight("100px");
        this.textAreaComments.setWordwrap(false);
        this.textAreaComments.setImmediate(true);
        layout.addComponent(textAreaComments);
        
        
        // after each value change, the save button has to be en- or disabled
        // according to validation result of training bean
        Property.ValueChangeListener valueChangeListener = new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = 2102740946962700349L;
            
            public void valueChange(Property.ValueChangeEvent event) {
                buttonSave.setEnabled(training.getBean().isValid());
            }
        };
        this.textFieldLocation.addValueChangeListener(valueChangeListener);        
        this.dateFieldDate.addValueChangeListener(valueChangeListener);        
        this.selectShoes.addValueChangeListener(valueChangeListener);
        this.textAreaComments.addValueChangeListener(valueChangeListener);        

        // table containing runs of currently edited training session
        tableRuns = new Table("Läufe");
        layout.addComponent(tableRuns);
        tableRuns.addContainerProperty("strecke", String.class, "");
        tableRuns.addContainerProperty("zeit", String.class, "");
        tableRuns.addContainerProperty("schnitt", String.class, "");
        tableRuns.setSortEnabled(false);
        tableRuns.setColumnHeader("strecke", "Strecke");
        tableRuns.setColumnHeader("zeit", "Zeit");
        tableRuns.setColumnHeader("schnitt", "Schnitt");
        tableRuns.setWidth("400px");
        tableRuns.setPageLength(TABLE_RUNS_MIN_PAGE_LENGTH);
        tableRuns.setFooterVisible(true);

        // button "add run"
        buttonAddRun = new Button("Lauf hinzufügen");
        layout.addComponent(buttonAddRun);
        
        buttonAddRun.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 8949212155808289711L;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                EditRunWindow.show(new EditRunWindow.RunCreatedListener() {
                    
                    private static final long serialVersionUID = -575130266743356570L;

                    @Override
                    public void runCreated(Lauf run) {
                        
                        // add run to currently edited training session
                        EditTrainingWindow.this.training.getBean().getLaeufe().add(run);

                        refreshTableRuns();
                        
                        // save button en- or disabled according to validation result of training bean
                        buttonSave.setEnabled(EditTrainingWindow.this.training.getBean().isValid());
                        
                    }
                });
            }
        });
        
        // button "save"
        buttonSave = new Button("Speichern");
        layout.addComponent(buttonSave);
        buttonSave.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                EditTrainingWindow.this.trainingCreatedListener.trainingCreated(EditTrainingWindow.this.training.getBean());
                close();
            }
        });

        // button "cancel"
        buttonCancel = new Button("Abbrechen");
        layout.addComponent(buttonCancel);
        buttonCancel.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        
        // init ui: create new training bean
        this.training = new BeanItem<Training>(new Training());
        
        // init ui: save button en- or disabled according to validation result of training bean
        buttonSave.setEnabled(this.training.getBean().isValid());

        // init ui: bind ui to training bean
        this.textFieldLocation.setPropertyDataSource(this.training.getItemProperty("ort"));
        this.dateFieldDate.setPropertyDataSource(this.training.getItemProperty("datum"));
        this.textAreaComments.setPropertyDataSource(this.training.getItemProperty("bemerkungen"));
        
        // init ui: fill shoe list
        BeanItemContainer<Shoe> listOfActiveShoes = new BeanItemContainer<Shoe>(Shoe.class);
        listOfActiveShoes.addAll(this.trainingLog.getAktiveSchuhe());
        this.selectShoes.setContainerDataSource(listOfActiveShoes);
    
        
        
    }
    
    /**
     * Refreshes table that contains time, distance and pace of all single runs of the training
     */
    private void refreshTableRuns() {
        
        this.tableRuns.removeAllItems();

        String distance;
        String time;
        String pace;

        for (Lauf run : this.training.getBean().getLaeufe()) {
            distance = distanceConverter.convertToPresentation(run.getStrecke(), String.class, null);
            time = timeConverter.convertToPresentation(run.getZeit(), String.class, null);
            pace = timeConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(run.getStrecke(), run.getZeit()), String.class, null);
            this.tableRuns.addItem(new Object[]{distance, time, pace}, null);
        }
        
        // increment page size if necessary
        tableRuns.setPageLength(Math.max(TABLE_RUNS_MIN_PAGE_LENGTH, this.training.getBean().getLaeufe().size()));

        // footer: distance sum of training
        Integer trainingDistance = training.getBean().getStrecke();
        if(trainingDistance!=null) {
            this.tableRuns.setColumnFooter("strecke", distanceConverter.convertToPresentation(trainingDistance, String.class, null));
        }
        
        // footer: time sum of training
        Integer trainingTime = training.getBean().getZeit();
        if(trainingTime!=null) {
            this.tableRuns.setColumnFooter("zeit", timeConverter.convertToPresentation(trainingTime, String.class, null));
        }
        
        // footer: avg. pace of training
        if(trainingDistance!=null && trainingTime!=null) {
            this.tableRuns.setColumnFooter("schnitt", timeConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(trainingDistance, trainingTime), String.class, null));
        }

    }
    
    /**
     * Opens a new window to edit training.
     * @param trainingLog training log to work with
     * @param trainingCreatedListener listener to specify actions following this dialog
     */
    public static void show(Trainingstagebuch trainingLog, TrainingCreatedListener trainingCreatedListener) {
        EditTrainingWindow window = new EditTrainingWindow(trainingLog, trainingCreatedListener);
        UI.getCurrent().addWindow(window);
    }
    
    /**
     * Listener to specify actions to be executed after a training is created using this dialog. 
     * @author Nikolaus Winter
     */
    public interface TrainingCreatedListener extends Serializable {
        
        /**
         * Action to be executed after a training is created using this dialog.
         */
        public void trainingCreated(Training training);
        
    }
    
}
