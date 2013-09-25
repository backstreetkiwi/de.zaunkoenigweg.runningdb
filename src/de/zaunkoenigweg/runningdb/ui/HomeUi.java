package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;

import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

public class HomeUi extends AbstractUi {

    private static final long serialVersionUID = 6470235043474537736L;

    Table tableStatistik;
    
    public HomeUi(Trainingstagebuch trainingstagebuch) {
        
        super(trainingstagebuch);
        
        Layout layout = new FormLayout();
        
        tableStatistik = new Table("Statistik");
        tableStatistik.addContainerProperty("info", String.class, "");
        tableStatistik.addContainerProperty("wert", String.class, "");
        tableStatistik.setSortEnabled(false);
        tableStatistik.setColumnHeader("info", "");
        tableStatistik.setColumnHeader("wert", "");
        tableStatistik.setWidth("600px");
        layout.addComponent(tableStatistik);

        setCompositionRoot(layout);
    }

    @Override
    public void show() {
        StreckeConverter streckeConverter = new StreckeConverter();
        ZeitConverter zeitConverter = new ZeitConverter();

        tableStatistik.removeAllItems();
        
        // Statistik anzeigen
        if(this.trainingstagebuch!=null) {
            
            tableStatistik.addItem(new Object[] { "Anzahl Trainingseinheiten", ""+trainingstagebuch.getTrainingseinheiten().size() }, null);
            tableStatistik.addItem(new Object[] { "gesamt gelaufene Strecke (Meter)", streckeConverter.convertToPresentation(trainingstagebuch.getStrecke(), String.class, null)}, null);
            tableStatistik.addItem(new Object[] { "gesamt gelaufene Zeit (Stunden)", zeitConverter.convertToPresentation(trainingstagebuch.getZeit(), String.class, null)}, null);
            tableStatistik.addItem(new Object[] { "Gesamtschnitt", zeitConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(trainingstagebuch.getStrecke(), trainingstagebuch.getZeit()), String.class, null)}, null);
            tableStatistik.addItem(new Object[] { "Anzahl Bestzeitenstrecken", ""+trainingstagebuch.getBestzeitStrecken().size()}, null);
            tableStatistik.addItem(new Object[] { "Anzahl Schuhe", ""+trainingstagebuch.getSchuhe().size()}, null);
        }
    }
}
