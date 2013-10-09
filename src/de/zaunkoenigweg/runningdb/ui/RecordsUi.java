package de.zaunkoenigweg.runningdb.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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
    
    private Accordion accordion;

    Button buttonAddRecordDistance;
    
    public RecordsUi() {

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setCompositionRoot(layout);

        this.accordion = new Accordion();
        layout.addComponent(this.accordion);

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
                        refreshUi();
                    }

                });
            }
        });

    }

    @Override
    public void show() {
        refreshUi();
    }

    /**
     * Builds/Refreshes UI.
     */
    private void refreshUi() {
        
        this.accordion.removeAllComponents();

        Panel panel = null;
        VerticalLayout panelLayout = null;
        String caption = "";
        
        List<RecordInfo> records = getTrainingLog().getRecords();

        for (RecordInfo recordInfo : records) {
            
            String distance = new DistanceConverter().convertToPresentation(recordInfo.getRecordDistance().getDistance(), String.class, null);
            if (StringUtils.isNotBlank(recordInfo.getRecordDistance().getLabel())) {
                caption = String.format("%s: %s Meter (%d mal gelaufen)", recordInfo.getRecordDistance().getLabel(), distance, recordInfo.getRunCount());
            } else {
                caption = String.format("%s Meter (%d mal gelaufen)", distance, recordInfo.getRunCount());
            }

            panel = new Panel();
            panelLayout = new VerticalLayout();
            panel.setContent(panelLayout);
            
            panelLayout.addComponent(createBestzeitTable(recordInfo));
            panelLayout.addComponent(createButtonRemoveBestzeit(recordInfo));
            
            this.accordion.addTab(panel, caption);
            
        }
        
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
    
}
