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
import de.zaunkoenigweg.runningdb.domain.RecordInfo.BestzeitLauf;
import de.zaunkoenigweg.runningdb.domain.RecordDistance;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * View zur Bearbeitung der Bestzeitenstrecken.
 * 
 * @author Nikolaus Winter
 * 
 */
public class BestzeitenUi extends AbstractUi {

    private static final int ANZAHL_ZEITEN_PRO_STRECKE = 3;

    private static final long serialVersionUID = 3021578951350704103L;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");    

    Button buttonStreckeHinzufuegen;
    
    /**
     * Erzeugt diese View
     */
    public BestzeitenUi(TrainingLog trainingstagebuch) {
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
        List<RecordInfo> bestzeiten = trainingstagebuch.getBestzeiten();
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
                BestzeitStreckeEingabeWindow bestzeitStreckeEingabe = new BestzeitStreckeEingabeWindow(BestzeitenUi.this);
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
        String strecke = new StreckeConverter().convertToPresentation(bestzeitInfo.getStrecke().getStrecke(), String.class, null);
        if (StringUtils.isNotBlank(bestzeitInfo.getStrecke().getBezeichnung())) {
            caption = String.format("%s: %s Meter (%d mal gelaufen)", bestzeitInfo.getStrecke().getBezeichnung(), strecke, bestzeitInfo.getAnzahl());
        } else {
            caption = String.format("%s Meter (%d mal gelaufen)", strecke, bestzeitInfo.getAnzahl());
        }

        Table table = new Table(caption);

        table.addContainerProperty("datum", String.class, null);
        table.addContainerProperty("zeit", Integer.class, null);
        table.addContainerProperty("schnitt", Integer.class, null);
        table.setColumnHeader("datum", "Datum");
        table.setColumnHeader("zeit", "Zeit");
        table.setColumnHeader("schnitt", "Schnitt");
        table.setConverter("zeit", new ZeitConverter());
        table.setConverter("schnitt", new ZeitConverter());
        table.setSortEnabled(false);
        table.setColumnWidth("datum", 100);
        table.setColumnWidth("zeit", 100);
        table.setColumnWidth("schnitt", 100);

        List<BestzeitLauf> laeufe = bestzeitInfo.getLaeufe();
        int i = 0;
        while (i < ANZAHL_ZEITEN_PRO_STRECKE && i < laeufe.size()) {
            BestzeitLauf lauf = laeufe.get(i);
            table.addItem(new Object[] {DATE_FORMATTER.format(lauf.getTraining().getDatum()), lauf.getZeit(), RunningDbUtil.berechneSchnitt(bestzeitInfo.getStrecke().getStrecke(), lauf.getZeit()) }, null);
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
                        BestzeitenUi.this.trainingstagebuch.removeBestzeitStrecke(bestzeitInfo.getStrecke());
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
     * Callback-Methode für das {@link BestzeitStreckeEingabeWindow}
     * @param bestzeitStrecke BestzeitStrecke, die hinzugefügt werden soll.
     */
    public void addBestzeitenStrecke(RecordDistance bestzeitStrecke) {
        this.trainingstagebuch.addBestzeitStrecke(bestzeitStrecke);
        refreshUi();
    }
}
