package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import de.zaunkoenigweg.runningdb.RunningDbUi;
import de.zaunkoenigweg.runningdb.domain.Lauf;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Schuh;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

/**
 * View zur Bearbeitung einer Trainingseinheit.
 * 
 * @author Nikolaus Winter
 */
public class EditTraining extends CustomComponent implements View {

    private static final long serialVersionUID = -7268302935704911518L;

    // Converter für Strecke und Zeit
    private StreckeConverter streckeConverter = new StreckeConverter();
    private ZeitConverter zeitConverter = new ZeitConverter();

    // Navigator für Seitennavigation
    Navigator navigator;
    
    // UI-Felder
    TextField textFieldOrt;
    DateField dateFieldDatum;
    ComboBox selectSchuhe;
    TextField textFieldGesamtstrecke;
    TextField textFieldGesamtzeit;
    TextField textFieldGesamtschnitt;
    TextArea textAreaBemerkungen;
    Button buttonSpeichern;
    Table tableLaeufe;
    Button buttonLaufHinzufuegen;
    
    // derzeit bearbeitetes Training
    BeanItem<Training> training;
    
    // Referenz auf das bearbeitete Trainingstagebuch
    private Trainingstagebuch trainingstagebuch;

    /**
     * Erzeugt diese View
     */
    public EditTraining() {
        
        // Formular-Layout
        Layout layout = new FormLayout();
        
        // Eingabefeld 'Ort'
        this.textFieldOrt = new TextField("Ort");
        this.textFieldOrt.setWidth("300px");
        this.textFieldOrt.setImmediate(true);
        layout.addComponent(textFieldOrt);

        // Eingabefeld 'Datum'
        this.dateFieldDatum = new DateField("Datum");
        this.dateFieldDatum.setWidth("300px");
        this.dateFieldDatum.setDateFormat("dd.MM.yyyy");
        this.dateFieldDatum.setImmediate(true);
        layout.addComponent(dateFieldDatum);
        
        // Auswahl der Schuhe
        this.selectSchuhe = new ComboBox("Schuhe");
        this.selectSchuhe.setWidth("300px");
        this.selectSchuhe.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        this.selectSchuhe.setItemCaptionPropertyId("kurzbezeichnung");
        this.selectSchuhe.setInputPrompt("Bitte Schuh auswählen...");
        this.selectSchuhe.setImmediate(true);
        layout.addComponent(selectSchuhe);

        this.selectSchuhe.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = -4575515764282188221L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(selectSchuhe.getValue() instanceof Schuh) {
                    training.getBean().setSchuh(((Schuh)selectSchuhe.getValue()).getId());
                } else {
                    training.getBean().setSchuh(0);
                }
            }
        });
        
        // Anzeige der Gesamtstrecke (Summe der Läufe)
        textFieldGesamtstrecke = new TextField("Strecke");
        textFieldGesamtstrecke.setEnabled(false);
        layout.addComponent(textFieldGesamtstrecke);

        
        // Anzeige der Gesamtzeit (Summe der Läufe)
        textFieldGesamtzeit = new TextField("Zeit");
        layout.addComponent(textFieldGesamtzeit);
        textFieldGesamtzeit.setEnabled(false);
        
        // Anzeige des Gesamtschnitts (aller Läufe)
        textFieldGesamtschnitt = new TextField("Schnitt");
        layout.addComponent(textFieldGesamtschnitt);
        textFieldGesamtschnitt.setEnabled(false);

        // Eingabefeld 'Bemerkungen'
        this.textAreaBemerkungen = new TextArea("Bemerkungen");
        this.textAreaBemerkungen.setWidth("500px");
        this.textAreaBemerkungen.setHeight("200px");
        this.textAreaBemerkungen.setWordwrap(false);
        this.textAreaBemerkungen.setImmediate(true);
        layout.addComponent(textAreaBemerkungen);
        
        // Änderung eines der Felder =>
        // Button Speichern muss en/disabled werden.
        Property.ValueChangeListener valueChangeListener = new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = 2102740946962700349L;
            
            public void valueChange(Property.ValueChangeEvent event) {
                buttonSpeichern.setEnabled(training.getBean().isValid());
            }
        };
        this.textFieldOrt.addValueChangeListener(valueChangeListener);        
        this.dateFieldDatum.addValueChangeListener(valueChangeListener);        
        this.selectSchuhe.addValueChangeListener(valueChangeListener);
        this.textAreaBemerkungen.addValueChangeListener(valueChangeListener);        

        // Tabelle mit den Teilstrecken
        tableLaeufe = new Table("Läufe");
        tableLaeufe.addContainerProperty("strecke", String.class, "");
        tableLaeufe.addContainerProperty("zeit", String.class, "");
        tableLaeufe.addContainerProperty("schnitt", String.class, "");
        tableLaeufe.setSortEnabled(false);
        tableLaeufe.setColumnHeader("strecke", "Strecke");
        tableLaeufe.setColumnHeader("zeit", "Zeit");
        tableLaeufe.setColumnHeader("schnitt", "Schnitt");
        tableLaeufe.setWidth("400px");
        layout.addComponent(tableLaeufe);

        // Button 'Lauf hinzufügen'
        buttonLaufHinzufuegen = new Button("Lauf hinzufügen");
        layout.addComponent(buttonLaufHinzufuegen);
        
        buttonLaufHinzufuegen.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 8949212155808289711L;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                LaufEingabeWindow laufEingabe = new LaufEingabeWindow(EditTraining.this);
                UI.getCurrent().addWindow(laufEingabe);
            }
        });
        
        // Button 'Speichern'
        buttonSpeichern = new Button("Speichern");
        layout.addComponent(buttonSpeichern);
        
        buttonSpeichern.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 4055963753468181040L;
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                trainingstagebuch.addTraining(training.getBean());
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
        this.trainingstagebuch = (Trainingstagebuch) VaadinSession.getCurrent().getAttribute("trainingstagebuch");

        // neues Training erzeugen
        this.training = new BeanItem<Training>(new Training());

        // Verknüpfung des UIs mit dem BeanItem(Training)
        this.textFieldOrt.setPropertyDataSource(this.training.getItemProperty("ort"));
        this.dateFieldDatum.setPropertyDataSource(this.training.getItemProperty("datum"));
        this.textAreaBemerkungen.setPropertyDataSource(this.training.getItemProperty("bemerkungen"));
        
        // Initialisierung weiterer, nicht gebundener Felder
        this.textFieldGesamtstrecke.setValue("");
        this.textFieldGesamtzeit.setValue("");
        this.textFieldGesamtschnitt.setValue("");
        this.tableLaeufe.removeAllItems();
        
        // Button Speichern muss en/disabled werden.
        buttonSpeichern.setEnabled(this.training.getBean().isValid());

        // Auswahlliste Schuhe initialisieren
        BeanItemContainer<Schuh> schuhe = new BeanItemContainer<Schuh>(Schuh.class);
        schuhe.addAll(trainingstagebuch.getAktiveSchuhe());
        this.selectSchuhe.setContainerDataSource(schuhe);
    }
    
    public void addLaufToCurrentTraining(Lauf lauf) {
        this.training.getBean().getLaeufe().add(lauf);
        
        // Lauf zur Tabelle hinzufügen
        String streckeAsString = streckeConverter.convertToPresentation(lauf.getStrecke(), String.class, null);
        String zeitAsString = zeitConverter.convertToPresentation(lauf.getZeit(), String.class, null);
        String schnittAsString = zeitConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(lauf.getStrecke(), lauf.getZeit()), String.class, null);
        tableLaeufe.addItem(new Object[]{streckeAsString, zeitAsString, schnittAsString}, null);

        // Strecke des gesamten Trainings aktualisieren
        Integer gesamtstrecke = training.getBean().getStrecke();
        if(gesamtstrecke!=null) {
            textFieldGesamtstrecke.setValue(streckeConverter.convertToPresentation(gesamtstrecke, String.class, null));
        }
        
        // Zeit des gesamten Trainings aktualisieren
        Integer gesamtzeit = training.getBean().getZeit();
        if(gesamtzeit!=null) {
            textFieldGesamtzeit.setValue(zeitConverter.convertToPresentation(gesamtzeit, String.class, null));
        }
        
        // Schnitt des gesamten Trainings aktualisieren
        if(gesamtstrecke!=null && gesamtzeit!=null) {
            textFieldGesamtschnitt.setValue(zeitConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(gesamtstrecke, gesamtzeit), String.class, null));
        }
        
        // Button Speichern muss en/disabled werden.
        buttonSpeichern.setEnabled(this.training.getBean().isValid());
    }
    
}
