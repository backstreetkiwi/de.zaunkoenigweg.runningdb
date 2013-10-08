package de.zaunkoenigweg.runningdb.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import de.zaunkoenigweg.runningdb.domain.RecordInfo;
import de.zaunkoenigweg.runningdb.domain.RecordInfo.RecordRun;
import de.zaunkoenigweg.runningdb.domain.RecordDistance;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * UI showing record distances and fastest runs per distance.
 * 
 * Editing the record distances is also possible in this UI.
 * 
 * @author Nikolaus Winter
 */
public class RecordsUi extends AbstractUi {

    private static final long serialVersionUID = 3021578951350704103L;
    
    private static final int NUMBER_OF_RUNS_PER_RECORD_DISTANCE = 3;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");    

    Button buttonAddRecordDistance;
    
    public RecordsUi(TrainingLog trainingLog) {
        super(trainingLog);
    }

    @Override
    public void show() {
        refreshUi();
    }

    /**
     * Builds/Refreshes UI.
     */
    private void refreshUi() {

        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        setCompositionRoot(layout);

        // one table per record distance is added to ui
        List<RecordInfo> bestzeiten = trainingLog.getRecords();
        for (RecordInfo bestzeitInfo : bestzeiten) {
            layout.addComponent(createBestzeitTable(bestzeitInfo));
            layout.addComponent(createButtonRemoveBestzeit(bestzeitInfo));
        }
        
        // button "add new record distance"
        buttonAddRecordDistance = new Button("Strecke hinzufügen");
        layout.addComponent(buttonAddRecordDistance);

        buttonAddRecordDistance.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 8949212155808289711L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                
                // create and show window to add new record distance
                // TODO: add listener to window
                RecordDistanceInputWindow bestzeitStreckeEingabe = new RecordDistanceInputWindow(RecordsUi.this);
                UI.getCurrent().addWindow(bestzeitStreckeEingabe);
            }
        });
    }

    /**
     * Creates table for given record distance using info object.
     * @param recordInfo information regarding record distance
     * @return table for given record distance
     */
    private Table createBestzeitTable(RecordInfo recordInfo) {

        String caption = "";
        String distance = new DistanceConverter().convertToPresentation(recordInfo.getRecordDistance().getDistance(), String.class, null);
        if (StringUtils.isNotBlank(recordInfo.getRecordDistance().getLabel())) {
            caption = String.format("%s: %s Meter (%d mal gelaufen)", recordInfo.getRecordDistance().getLabel(), distance, recordInfo.getRunCount());
        } else {
            caption = String.format("%s Meter (%d mal gelaufen)", distance, recordInfo.getRunCount());
        }

        Table table = new Table(caption);

        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("time", Integer.class, null);
        table.addContainerProperty("pace", Integer.class, null);
        table.setColumnHeader("date", "Datum");
        table.setColumnHeader("time", "Zeit");
        table.setColumnHeader("pace", "Schnitt");
        table.setConverter("time", new TimeConverter());
        table.setConverter("pace", new TimeConverter());
        table.setSortEnabled(false);
        table.setColumnWidth("date", 100);
        table.setColumnWidth("time", 100);
        table.setColumnWidth("pace", 100);

        List<RecordRun> runs = recordInfo.getRecordRuns();
        int i = 0;
        while (i < NUMBER_OF_RUNS_PER_RECORD_DISTANCE && i < runs.size()) {
            RecordRun lauf = runs.get(i);
            table.addItem(new Object[] {DATE_FORMATTER.format(lauf.getTraining().getDate()), lauf.getTime(), RunningDbUtil.getPace(recordInfo.getRecordDistance().getDistance(), lauf.getTime()) }, null);
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

        Button button = new Button("Löschen");
        button.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 7956107661600031856L;

            @Override
            public void buttonClick(ClickEvent event) {

                ConfirmationDialog.show("Soll die angegebene Bestzeit wirklich gelöscht werden?", new ConfirmationDialog.ConfimationDialogListener() {
                    
                    @Override
                    public void yes() {
                        // delete record time from training log
                        RecordsUi.this.trainingLog.removeRecordDistance(recordInfo.getRecordDistance());
                        refreshUi();
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
    
    /**
     * TODO: use listener (implement it first of all :-)
     * callback for {@link RecordDistanceInputWindow}
     */
    public void addRecordDistance(RecordDistance recordDistance) {
        this.trainingLog.addRecordDistance(recordDistance);
        refreshUi();
    }
}
