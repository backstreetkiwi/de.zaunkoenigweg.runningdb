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
 * View zur Bearbeitung der Bestzeitenstrecken.
 * 
 * @author Nikolaus Winter
 * 
 */
public class RecordsUi extends AbstractUi {

    private static final int ANZAHL_ZEITEN_PRO_STRECKE = 3;

    private static final long serialVersionUID = 3021578951350704103L;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");    

    Button buttonStreckeHinzufuegen;
    
    /**
     * Erzeugt diese View
     */
    public RecordsUi(TrainingLog trainingstagebuch) {
        super(trainingstagebuch);
    }

    @Override
    public void show() {
        refreshUi();
    }

    /**
     * Füllt diese UI mit den Daten aus dem Trainingstagebuch.
     */
    private void refreshUi() {

        // Formular-Layout
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        setCompositionRoot(layout);

        // Je Bestzeitstrecke im Tagebuch wird eine Tabelle mit den besten
        // drei Zeiten sowie ein Button zum Löschen des Eintrags hinzugefügt.
        List<RecordInfo> bestzeiten = trainingstagebuch.getRecords();
        for (RecordInfo bestzeitInfo : bestzeiten) {
            layout.addComponent(createBestzeitTable(bestzeitInfo));
            layout.addComponent(createButtonRemoveBestzeit(bestzeitInfo));
        }
        
        // Button 'Neue Strecke hinzufügen'
        buttonStreckeHinzufuegen = new Button("Strecke hinzufügen");
        layout.addComponent(buttonStreckeHinzufuegen);

        buttonStreckeHinzufuegen.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 8949212155808289711L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                
                // neues Fenster zur Eingabe einer neuen Bestzeitenstrecke erzeugen und anzeigen.
                RecordDistanceInputWindow bestzeitStreckeEingabe = new RecordDistanceInputWindow(RecordsUi.this);
                UI.getCurrent().addWindow(bestzeitStreckeEingabe);
            }
        });
    }

    /**
     * Erzeugt eine Tabelle mit den Informationen zur angegebenen Bestzeit
     * @param bestzeitInfo Informationen über die Bestzeit, die angezeigt werden soll.
     * @return Tabelle mit den Informationen zur angegebenen Bestzeit
     */
    private Table createBestzeitTable(RecordInfo bestzeitInfo) {

        String caption = "";
        String strecke = new DistanceConverter().convertToPresentation(bestzeitInfo.getRecordDistance().getDistance(), String.class, null);
        if (StringUtils.isNotBlank(bestzeitInfo.getRecordDistance().getLabel())) {
            caption = String.format("%s: %s Meter (%d mal gelaufen)", bestzeitInfo.getRecordDistance().getLabel(), strecke, bestzeitInfo.getRunCount());
        } else {
            caption = String.format("%s Meter (%d mal gelaufen)", strecke, bestzeitInfo.getRunCount());
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

        List<RecordRun> laeufe = bestzeitInfo.getRecordRuns();
        int i = 0;
        while (i < ANZAHL_ZEITEN_PRO_STRECKE && i < laeufe.size()) {
            RecordRun lauf = laeufe.get(i);
            table.addItem(new Object[] {DATE_FORMATTER.format(lauf.getTraining().getDate()), lauf.getTime(), RunningDbUtil.getPace(bestzeitInfo.getRecordDistance().getDistance(), lauf.getTime()) }, null);
            i++;
        }
        table.setPageLength(ANZAHL_ZEITEN_PRO_STRECKE);

        return table;
    }

    /**
     * Erzeugt einen Button zum Löschen der Bestzeitenstrecke aus der Liste der
     * anzuzeigenden Bestzeiten.
     * @param bestzeitInfo Informationen über die Bestzeit, die gelöscht werden soll.
     * @return
     */
    private Button createButtonRemoveBestzeit(final RecordInfo bestzeitInfo) {

        Button buttonremoveBestzeit = new Button("Löschen");
        buttonremoveBestzeit.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 7956107661600031856L;

            @Override
            public void buttonClick(ClickEvent event) {

                ConfirmationDialog.show("Soll die angegebene Bestzeit wirklich gelöscht werden?", new ConfirmationDialog.ConfimationDialogListener() {
                    
                    @Override
                    public void yes() {
                        // delete record time from training log
                        RecordsUi.this.trainingstagebuch.removeRecordDistance(bestzeitInfo.getRecordDistance());
                        refreshUi();
                    }
                    
                    @Override
                    public void no() {
                        // do nothing
                    }
                });
                               
            }
        });
        return buttonremoveBestzeit;
    }
    
    /**
     * Callback-Methode für das {@link RecordDistanceInputWindow}
     * @param bestzeitStrecke BestzeitStrecke, die hinzugefügt werden soll.
     */
    public void addBestzeitenStrecke(RecordDistance bestzeitStrecke) {
        this.trainingstagebuch.addRecordDistance(bestzeitStrecke);
        refreshUi();
    }
}
