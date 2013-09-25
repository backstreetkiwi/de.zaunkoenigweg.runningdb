package de.zaunkoenigweg.runningdb.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.zaunkoenigweg.runningdb.domain.BestzeitInfo;
import de.zaunkoenigweg.runningdb.domain.BestzeitInfo.BestzeitLauf;
import de.zaunkoenigweg.runningdb.domain.BestzeitStrecke;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

/**
 * View zur Bearbeitung der Bestzeitenstrecken.
 * 
 * @author Nikolaus Winter
 * 
 */
public class BestzeitenUi extends AbstractUi {

    private static final long serialVersionUID = 3021578951350704103L;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");    

    Button buttonStreckeHinzufuegen;
    
    /**
     * Erzeugt diese View
     */
    public BestzeitenUi(Trainingstagebuch trainingstagebuch) {

        super(trainingstagebuch);

    }

    @Override
    public void show() {
        fillUi();
    }

    private void fillUi() {

        // Formular-Layout
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        setCompositionRoot(layout);

        List<BestzeitInfo> bestzeiten = trainingstagebuch.getBestzeiten();
        for (BestzeitInfo bestzeitInfo : bestzeiten) {

            layout.addComponent(createBestzeitTable(bestzeitInfo));
            layout.addComponent(createButtonRemoveBestzeit(bestzeitInfo));

        }
        
        // Button 'Strecke hinzufügen'
        buttonStreckeHinzufuegen = new Button("Strecke hinzufügen");
        layout.addComponent(buttonStreckeHinzufuegen);
        
        buttonStreckeHinzufuegen.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 8949212155808289711L;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                BestzeitStreckeEingabeWindow bestzeitStreckeEingabe = new BestzeitStreckeEingabeWindow(BestzeitenUi.this);
                UI.getCurrent().addWindow(bestzeitStreckeEingabe);
            }
        });
        
        

    }

    private Table createBestzeitTable(BestzeitInfo bestzeitInfo) {

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
        table.setHeight("250px");
        table.setSortEnabled(false);
        table.setColumnWidth("datum", 100);
        table.setColumnWidth("zeit", 100);
        table.setColumnWidth("schnitt", 100);

        List<BestzeitLauf> laeufe = bestzeitInfo.getLaeufe();
        int i = 0;
        while (i < 3 && i < laeufe.size()) {
            BestzeitLauf lauf = laeufe.get(i);
            table.addItem(new Object[] {DATE_FORMATTER.format(lauf.getTraining().getDatum()), lauf.getZeit(), RunningDbUtil.berechneSchnitt(bestzeitInfo.getStrecke().getStrecke(), lauf.getZeit()) }, null);
            i++;
        }
        table.setHeight("87px");
        table.setPageLength(3);

        return table;

    }

    private Button createButtonRemoveBestzeit(final BestzeitInfo bestzeitInfo) {

        Button buttonremoveBestzeit = new Button("Löschen");
        buttonremoveBestzeit.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 7956107661600031856L;

            @Override
            public void buttonClick(ClickEvent event) {
                BestzeitenUi.this.trainingstagebuch.removeBestzeitStrecke(bestzeitInfo.getStrecke());
                fillUi();
            }
        });
        return buttonremoveBestzeit;
        
    }
    
    public void addBestzeitenStrecke(BestzeitStrecke bestzeitStrecke) {
        this.trainingstagebuch.addBestzeitStrecke(bestzeitStrecke);
        fillUi();
    }
}
