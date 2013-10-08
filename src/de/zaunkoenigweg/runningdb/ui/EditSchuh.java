package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;

import de.zaunkoenigweg.runningdb.RunningDbUi;
import de.zaunkoenigweg.runningdb.domain.Shoe;
import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * View zur Bearbeitung eines Laufschuhs.
 * 
 * @author Nikolaus Winter
 *
 */
public class EditSchuh extends CustomComponent implements View {

    private static final long serialVersionUID = 3021578951350704103L;

    // Navigator für Seitennavigation
    Navigator navigator;
    
    // UI-Felder
    Label ueberschrift;
    TextField textFieldHersteller;
    TextField textFieldModell;
    TextField textFieldKaufdatum;
    TextField textFieldPreis;
    TextField textFieldBemerkungen;
    CheckBox checkBoxAktiv;
    Button buttonSpeichern;

    // derzeit bearbeiteter Schuh
    BeanItem<Shoe> schuh;
    
    // Referenz auf das bearbeitete Trainingstagebuch
    private TrainingLog trainingstagebuch;

    /**
     * Erzeugt diese View
     */
    public EditSchuh() {
        
        // Formular-Layout
        Layout layout = new FormLayout();
        
        // Label 'Überschrift'
        this.ueberschrift = new Label();
        layout.addComponent(this.ueberschrift);

        // Eingabefeld 'Hersteller'
        this.textFieldHersteller = new TextField("Hersteller");
        this.textFieldHersteller.setWidth("300px");
        this.textFieldHersteller.setImmediate(true);
        layout.addComponent(this.textFieldHersteller);
        
        // Eingabefeld 'Modell'
        this.textFieldModell = new TextField("Modell");
        this.textFieldModell.setWidth("300px");
        this.textFieldModell.setImmediate(true);
        layout.addComponent(this.textFieldModell);
        
        // Eingabefeld 'Kaufdatum'
        this.textFieldKaufdatum = new TextField("Kaufdatum");
        this.textFieldKaufdatum.setWidth("300px");
        this.textFieldKaufdatum.setImmediate(true);
        layout.addComponent(this.textFieldKaufdatum);
        
        // Eingabefeld 'Preis'
        this.textFieldPreis = new TextField("Preis");
        this.textFieldPreis.setWidth("300px");
        this.textFieldPreis.setImmediate(true);
        layout.addComponent(this.textFieldPreis);
        
        // Eingabefeld 'Bemerkungen'
        this.textFieldBemerkungen = new TextField("Bemerkungen");
        this.textFieldBemerkungen.setWidth("300px");
        this.textFieldBemerkungen.setImmediate(true);
        layout.addComponent(this.textFieldBemerkungen);
        
        // Checkbox 'Aktiv'
        this.checkBoxAktiv = new CheckBox("Aktiv?");
        this.checkBoxAktiv.setImmediate(true);
        layout.addComponent(this.checkBoxAktiv);
        
        // Änderung eines der Felder =>
        // Button Speichern muss en/disabled werden.
        Property.ValueChangeListener valueChangeListener = new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = 2102740946962700349L;
            
            public void valueChange(Property.ValueChangeEvent event) {
                buttonSpeichern.setEnabled(schuh.getBean().isValid());
            }
        };
        this.textFieldHersteller.addValueChangeListener(valueChangeListener);        
        this.textFieldModell.addValueChangeListener(valueChangeListener);        
        this.textFieldKaufdatum.addValueChangeListener(valueChangeListener);        
        this.textFieldPreis.addValueChangeListener(valueChangeListener);        
        this.textFieldBemerkungen.addValueChangeListener(valueChangeListener);        
        this.checkBoxAktiv.addValueChangeListener(valueChangeListener);        
        
        // Button 'Speichern'
        buttonSpeichern = new Button("Speichern");
        layout.addComponent(buttonSpeichern);
        
        buttonSpeichern.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 4055963753468181040L;
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                trainingstagebuch.addShoe(schuh.getBean());
                navigator.navigateTo(RunningDbUi.VIEW_START);
            }
        });
        
        
        // Formular(layout) zur Komponente hinzufügen
        setCompositionRoot(layout);
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        
        // Navigator holen für weitere Schritte
        this.navigator = event.getNavigator();
        
        // Trainingstagebuch aus der Session holen
        this.trainingstagebuch = (TrainingLog) VaadinSession.getCurrent().getAttribute("trainingstagebuch");

        // neuen Schuh erzeugen und anzeigen
        this.schuh = new BeanItem<Shoe>(new Shoe());

        // Verknüpfung des UIs mit dem BeanItem(Schuh)
        this.textFieldHersteller.setPropertyDataSource(this.schuh.getItemProperty("brand"));
        this.textFieldModell.setPropertyDataSource(this.schuh.getItemProperty("model"));
        this.textFieldKaufdatum.setPropertyDataSource(this.schuh.getItemProperty("dateOfPurchase"));
        this.textFieldPreis.setPropertyDataSource(this.schuh.getItemProperty("price"));
        this.textFieldBemerkungen.setPropertyDataSource(this.schuh.getItemProperty("comments"));
        this.checkBoxAktiv.setPropertyDataSource(this.schuh.getItemProperty("active"));
        
        // Button Speichern muss en/disabled werden.
        buttonSpeichern.setEnabled(this.schuh.getBean().isValid());
        
    }
    
}
