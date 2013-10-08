package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.zaunkoenigweg.runningdb.domain.RecordDistance;

/**
 * Fenster zur Eingabe einer Bestzeitenstrecke.
 * 
 * @author Nikolaus Winter
 */
public class BestzeitStreckeEingabeWindow extends Window {

    private static final long serialVersionUID = -6228525065980711482L;
    
    // Converter für Strecke und Zeit
    private StreckeConverter streckeConverter = new StreckeConverter();

    private TextField textFieldStrecke;
    private TextField textFieldBezeichnung;
    private Button buttonSpeichern;
    private Button buttonAbbrechen;
    BeanItem<RecordDistance> bestzeitStrecke;


    public BestzeitStreckeEingabeWindow(final BestzeitenUi bestzeitenUi) {
        
        // Lauf-Objekt erstellen
        this.bestzeitStrecke = new BeanItem<RecordDistance>(new RecordDistance());
        
        // allg. Eigenschaften des Fensters 
        setCaption("Eingabe einer Strecke für die Bestzeiten");
        setClosable(false);
        setHeight("400px");
        setWidth("600px");
        setResizable(false);
        setModal(true);
        center();

        // Formular-Layout
        Layout layout = new FormLayout();
        setContent(layout);
        
        // Eingabe der Strecke
        this.textFieldStrecke = new TextField("Strecke");
        this.textFieldStrecke.setWidth("200px");
        this.textFieldStrecke.setPropertyDataSource(this.bestzeitStrecke.getItemProperty("strecke"));
        this.textFieldStrecke.setImmediate(true);
        this.textFieldStrecke.setConverter(this.streckeConverter);
        this.textFieldStrecke.setConversionError("Die Laufstrecke muss zwischen 100 m und 42.195 m liegen.");
        this.textFieldStrecke.addValidator(new IntegerRangeValidator("Die Laufstrecke muss zwischen 100 m und 42.195 m liegen.", 100, 42195));
        this.textFieldStrecke.setValidationVisible(true);
        this.textFieldStrecke.setBuffered(true);
        this.textFieldStrecke.focus();
        layout.addComponent(this.textFieldStrecke);

        // Eingabe der Zeit
        this.textFieldBezeichnung = new TextField("Bezeichnung");
        this.textFieldBezeichnung.setWidth("400px");
        this.textFieldBezeichnung.setPropertyDataSource(this.bestzeitStrecke.getItemProperty("bezeichnung"));
        this.textFieldBezeichnung.setImmediate(true);
        this.textFieldBezeichnung.setBuffered(true);
        layout.addComponent(this.textFieldBezeichnung);

        // Button speichern
        buttonSpeichern = new Button("Speichern");
        layout.addComponent(buttonSpeichern);
        buttonSpeichern.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                if(textFieldStrecke.isValid()) {
                    textFieldStrecke.commit();
                    textFieldBezeichnung.commit();
                    if(bestzeitStrecke.getBean().getStrecke()!=null) {
                        bestzeitenUi.addBestzeitenStrecke(bestzeitStrecke.getBean());
                        close();
                    }
                }
            }
        });

        // Button Abbrechen
        buttonAbbrechen = new Button("Abbrechen");
        layout.addComponent(buttonAbbrechen);
        buttonAbbrechen.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        
    
    }
    
}
