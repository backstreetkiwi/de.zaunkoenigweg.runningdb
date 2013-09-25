package de.zaunkoenigweg.runningdb.ui;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

import de.zaunkoenigweg.runningdb.RunningDbUi;
import de.zaunkoenigweg.runningdb.domain.Lauf;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

/**
 * View zur Bearbeitung einer Trainingseinheit.
 * 
 * @author Nikolaus Winter
 */
public class EditTrainingAlt extends CustomComponent implements View {

    private static final long serialVersionUID = -7268302935704911518L;

    // Navigator für Seitennavigation
    Navigator navigator;
    
    // UI-Felder
    TextField textFieldOrt;
    DateField dateFieldDatum;
    TextField textFieldGesamtstrecke;
    TextField textFieldGesamtzeit;
    TextField textFieldGesamtschnitt;
    TextField textFieldStrecke;
    TextField textFieldZeit;
    Button buttonAddLauf;
    Button buttonSpeichern;
    Table tableLaeufe;

    // derzeit bearbeitetes Training
    Training training;
    
    // Fachobjekte, die dem derzeitigen Inhalt der Eingabefelder
    // 'Strecke' und 'Zeit' entsprechen
    Integer strecke;
    Integer zeit;

    // Converter für Strecke und Zeit
    private StreckeConverter streckeConverter = new StreckeConverter();
    private ZeitConverter zeitConverter = new ZeitConverter();

    /**
     * Erzeugt diese View
     */
    public EditTrainingAlt() {
        
        // Formular-Layout
        Layout layout = new FormLayout();
        
        // Eingabefeld 'Ort'
        this.textFieldOrt = new TextField("Ort");
        this.textFieldOrt.setWidth("200px");
        layout.addComponent(textFieldOrt);

        textFieldOrt.addBlurListener(new FieldEvents.BlurListener() {
            private static final long serialVersionUID = 8703082579152388296L;

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                training.setOrt(textFieldOrt.getValue());
                enOrDisableSaveButton();
            }
            
        });        
        

        // Eingabefeld 'Datum'
        this.dateFieldDatum = new DateField("Datum");
        this.dateFieldDatum.setWidth("200px");
        this.dateFieldDatum.setDateFormat("dd.MM.yyyy");
        layout.addComponent(dateFieldDatum);

        dateFieldDatum.addBlurListener(new FieldEvents.BlurListener() {
            private static final long serialVersionUID = 9160693944330739747L;

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                training.setDatum(dateFieldDatum.getValue());
                enOrDisableSaveButton();
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

        
        // Eingabefeld 'Strecke' für das Hinzufügen eines Laufs
        textFieldStrecke = new TextField("Strecke");
        layout.addComponent(textFieldStrecke);
        
        textFieldStrecke.addBlurListener(new FieldEvents.BlurListener() {

            private static final long serialVersionUID = -4087106960956026721L;

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                String value = textFieldStrecke.getValue();
                try {
                    strecke = streckeConverter.convertToModel(value, Integer.class, null);
                } catch (ConversionException e) {
                    strecke = null;
                }
                if(strecke!=null) {
                    textFieldStrecke.setValue(streckeConverter.convertToPresentation(strecke, String.class, null));
                } else {
                    textFieldStrecke.setValue("");
                }
            }
        });
        
        
        // Eingabefeld 'Zeit' für das Hinzufügen eines Laufs
        textFieldZeit = new TextField("Zeit");
        layout.addComponent(textFieldZeit);
        textFieldZeit.addBlurListener(new FieldEvents.BlurListener() {

            private static final long serialVersionUID = -683215431507870496L;

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                String value = textFieldZeit.getValue();
                try {
                    zeit = zeitConverter.convertToModel(value, Integer.class, null);
                } catch (ConversionException e) {
                    zeit = null;
                }
                if(zeit!=null) {
                    textFieldZeit.setValue(zeitConverter.convertToPresentation(zeit, String.class, null));
                } else {
                    textFieldZeit.setValue("");
                }
            }
        });

        
        // Berechnung des Schnitts für den Lauf (Listener für beide Felder)
        FieldEvents.BlurListener streckeZeitEingegebenBlurListener = new FieldEvents.BlurListener() {

            private static final long serialVersionUID = -5391756378453953652L;

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                buttonAddLauf.setEnabled(strecke!=null && zeit!=null);
            }
        };
        
        textFieldStrecke.addBlurListener(streckeZeitEingegebenBlurListener);
        textFieldZeit.addBlurListener(streckeZeitEingegebenBlurListener);
        
        
        // Button 'Lauf hinzufügen'
        buttonAddLauf = new Button("Hinzufügen");
        layout.addComponent(buttonAddLauf);
        buttonAddLauf.setEnabled(false);
        buttonAddLauf.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = -6873603722656477321L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                
                // Lauf mit aktueller Strecke/Zeit erzeugen und zum Training hinzufügen
                Lauf lauf = new Lauf();
                lauf.setStrecke(strecke);
                lauf.setZeit(zeit);
                training.getLaeufe().add(lauf);
                
                // Lauf zur Tabelle hinzufügen
                String streckeAsString = streckeConverter.convertToPresentation(strecke, String.class, null);
                String zeitAsString = zeitConverter.convertToPresentation(zeit, String.class, null);
                String schnittAsString = zeitConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(strecke, zeit), String.class, null);
                tableLaeufe.addItem(new Object[]{streckeAsString, zeitAsString, schnittAsString}, null);

                // Strecke des gesamten Trainings aktualisieren
                Integer gesamtstrecke = training.getStrecke();
                if(gesamtstrecke!=null) {
                    textFieldGesamtstrecke.setValue(streckeConverter.convertToPresentation(gesamtstrecke, String.class, null));
                }
                
                // Zeit des gesamten Trainings aktualisieren
                Integer gesamtzeit = training.getZeit();
                if(gesamtzeit!=null) {
                    textFieldGesamtzeit.setValue(zeitConverter.convertToPresentation(gesamtzeit, String.class, null));
                }
                
                // Schnitt des gesamten Trainings aktualisieren
                if(gesamtstrecke!=null && gesamtzeit!=null) {
                    textFieldGesamtschnitt.setValue(zeitConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(gesamtstrecke, gesamtzeit), String.class, null));
                }
                
                // UI zur Eingabe des Laufs leeren
                strecke = null;
                zeit = null;
                textFieldStrecke.setValue("");
                textFieldZeit.setValue("");
                buttonAddLauf.setEnabled(false);
                enOrDisableSaveButton();
            }
        });
        
        
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
        
        
        // Button 'Speichern'
        buttonSpeichern = new Button("Speichern");
        layout.addComponent(buttonSpeichern);
        buttonSpeichern.setEnabled(false);
        
        buttonSpeichern.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 4055963753468181040L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Trainingstagebuch trainingstagebuch = (Trainingstagebuch) VaadinSession.getCurrent().getAttribute("trainingstagebuch");
                trainingstagebuch.getTrainingseinheiten().add(training);
                navigator.navigateTo(RunningDbUi.VIEW_START);
            }
        });
        
        
        // Formular(layout) zur Komponente hinzufügen
        setCompositionRoot(layout);
    }
    
    /**
     * 
     */
    private void enOrDisableSaveButton() {
        buttonSpeichern.setEnabled(this.training.isValid());
    }

    @Override
    public void enter(ViewChangeEvent event) {
        
        // Navigator holen für weitere Schritte
        this.navigator = event.getNavigator();
        
        // neues Training erzeugen und anzeigen
        this.training = new Training();
        this.strecke = null;
        this.zeit = null;
        fillFormFromCurrentTraining();
    }
    
    /**
     * Initialisiert die View mit den Daten des Trainings
     */
    private void fillFormFromCurrentTraining() {
        if(StringUtils.isNotBlank(this.training.getOrt())) {
            this.textFieldOrt.setValue(this.training.getOrt());
        } else {
            this.textFieldOrt.setValue("");
        }
        this.dateFieldDatum.setValue(this.training.getDatum());
        this.tableLaeufe.removeAllItems();
        this.textFieldGesamtstrecke.setValue("");
        this.textFieldGesamtzeit.setValue("");
        this.textFieldGesamtschnitt.setValue("");
        this.textFieldStrecke.setValue("");
        this.textFieldZeit.setValue("");
        enOrDisableSaveButton();
    }

}
