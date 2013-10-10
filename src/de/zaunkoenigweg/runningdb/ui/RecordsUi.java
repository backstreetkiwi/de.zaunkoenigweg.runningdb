package de.zaunkoenigweg.runningdb.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import de.zaunkoenigweg.runningdb.domain.RecordDistance;
import de.zaunkoenigweg.runningdb.domain.RecordInfo;
import de.zaunkoenigweg.runningdb.domain.RecordInfo.RecordRun;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;

/**
 * UI showing record distances and fastest runs per distance.
 * 
 * Editing the record distances is also possible in this UI.
 * 
 * @author Nikolaus Winter
 */
public class RecordsUi extends AbstractUi {

    private static final long serialVersionUID = 3021578951350704103L;
    
    private static final int NUMBER_OF_RUNS_PER_RECORD_DISTANCE = 10;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    
    private ComboBox selectRecordDistance;
    private Panel panelRecordInfo;

    Button buttonAddRecordDistance;
    
    public RecordsUi() {

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setCompositionRoot(layout);

        // choice "record distance"
        this.selectRecordDistance = new ComboBox();
        layout.addComponent(this.selectRecordDistance);
        this.selectRecordDistance.setWidth("300px");
        this.selectRecordDistance.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        this.selectRecordDistance.setItemCaptionPropertyId("teaser");
        this.selectRecordDistance.setInputPrompt("Bitte auswählen...");
        this.selectRecordDistance.setNullSelectionAllowed(false);
        this.selectRecordDistance.setTextInputAllowed(false);
        this.selectRecordDistance.setImmediate(true);
        
        this.selectRecordDistance.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = 4706299051889826573L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(selectRecordDistance.getValue() instanceof RecordInfo) {
                    showRecordInfo((RecordInfo) selectRecordDistance.getValue());
                }
            }
        });

        this.panelRecordInfo = new Panel();
        layout.addComponent(this.panelRecordInfo);

        // button "add new record distance"
        buttonAddRecordDistance = ComponentFactory.createRegularButton("Strecke hinzufügen");
        layout.addComponent(buttonAddRecordDistance);

        buttonAddRecordDistance.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 8949212155808289711L;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                // create and show window to add new record distance
                RecordDistanceInputWindow.show(new RecordDistanceInputWindow.RecordDistanceCreatedListener() {

                    private static final long serialVersionUID = -823267736348626305L;

                    @Override
                    public void recordDistanceCreated(RecordDistance recordDistance) {
                        getTrainingLog().addRecordDistance(recordDistance);
                        fillSelectRecordDistance(recordDistance);
                    }

                });
            }
        });

    }

    @Override
    public void show() {
        fillSelectRecordDistance(null);
    }
    
    /**
     * Fills dropdown to select record distance.
     * If {@link RecordDistance} is given, it is preselected.
     * Otherwise, the first entry is preselected.
     * @param recordDistance preselected {@link RecordDistance}
     */
    private void fillSelectRecordDistance(RecordDistance recordDistance) {
        List<RecordInfo> records = getTrainingLog().getRecords();
        BeanItemContainer<RecordInfo> recordInfoContainer = new BeanItemContainer<RecordInfo>(RecordInfo.class);
        recordInfoContainer.addAll(records);
        this.selectRecordDistance.setContainerDataSource(recordInfoContainer);
        if(recordDistance!=null) {
            for (RecordInfo recordInfo : records) {
                if(recordDistance.equals(recordInfo.getRecordDistance())) {
                    this.selectRecordDistance.setValue(recordInfo);
                }
            }
        } else if (records.size()>0) {
            this.selectRecordDistance.setValue(records.get(0));
        }
    }

    /**
     * Shows given record info panel.
     * If {@link RecordInfo} ist given, its informatios are shown.
     * Otherwise, the first {@link RecordInfo}s informations are shown.
     * @param recordInfo {@link RecordInfo} to be shown
     */
    private void showRecordInfo(RecordInfo recordInfo) {
        
        if(recordInfo==null) {
            List<RecordInfo> records = getTrainingLog().getRecords();
            if(!records.isEmpty()) {
                recordInfo = records.get(0);
            } else {
                this.panelRecordInfo.setContent(null);
                return;
            }
        }

        Panel panel = new Panel();
        VerticalLayout panelLayout = new VerticalLayout();
        panel.setContent(panelLayout);
        panelLayout.addComponent(createBestzeitTable(recordInfo));
        panelLayout.addComponent(createButtonRemoveBestzeit(recordInfo));
        this.panelRecordInfo.setContent(panel);
        
        
    }

    /**
     * Creates table for given record distance using info object.
     * @param recordInfo information regarding record distance
     * @return table for given record distance
     */
    private Table createBestzeitTable(RecordInfo recordInfo) {

        Table table = new Table();

        table.addContainerProperty("rank", Integer.class, null);
        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("time", Integer.class, null);
        table.addContainerProperty("pace", Integer.class, null);
        table.setColumnHeader("rank", "");
        table.setColumnHeader("date", "Datum");
        table.setColumnHeader("time", "Zeit");
        table.setColumnHeader("pace", "Schnitt");
        table.setConverter("time", new TimeConverter());
        table.setConverter("pace", new TimeConverter());
        table.setSortEnabled(false);
        table.setColumnWidth("rank", 25);
        table.setColumnWidth("date", 100);
        table.setColumnWidth("time", 100);
        table.setColumnWidth("pace", 100);
        table.setColumnAlignment("rank", Align.CENTER);

        List<RecordRun> runs = recordInfo.getRecordRuns();
        int i = 0;
        while (i < NUMBER_OF_RUNS_PER_RECORD_DISTANCE && i < runs.size()) {
            RecordRun lauf = runs.get(i);
            table.addItem(new Object[] {(i+1), DATE_FORMATTER.format(lauf.getTraining().getDate()), lauf.getTime(), RunningDbUtil.getPace(recordInfo.getRecordDistance().getDistance(), lauf.getTime()) }, null);
            i++;
        }
        table.setPageLength(NUMBER_OF_RUNS_PER_RECORD_DISTANCE);

        return table;
    }

    /**
     * Creates a button to delete record distance
     * @param recordInfo information regarding record distance
     * @return button to delete record distance
     */
    private Button createButtonRemoveBestzeit(final RecordInfo recordInfo) {

        Button button = ComponentFactory.createRegularButton("Löschen");
        button.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 7956107661600031856L;

            @Override
            public void buttonClick(ClickEvent event) {

                ConfirmationDialog.show("Soll die angegebene Bestzeit wirklich gelöscht werden?", new ConfirmationDialog.ConfimationDialogListener() {
                    
                    @Override
                    public void yes() {
                        // delete record time from training log
                        getTrainingLog().removeRecordDistance(recordInfo.getRecordDistance());
                        fillSelectRecordDistance(null);
                        showRecordInfo(null);
                    }
                    
                    @Override
                    public void no() {
                        // do nothing
                    }
                });
                               
            }
        });
        return button;
    }
    
}
