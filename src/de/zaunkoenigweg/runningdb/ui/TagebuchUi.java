package de.zaunkoenigweg.runningdb.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import de.zaunkoenigweg.runningdb.domain.Lauf;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Schuh;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

public class TagebuchUi extends AbstractUi {

    private static final long serialVersionUID = 6470235043474537736L;

    // UI-Komponenten
    private Table tableTraining;
    private InlineDateField auswahlMonat;
    private BeanItemContainer<Training> trainingContainer;
    private TextField textFieldSchuh;
    private TextArea textAreaBemerkungen;
    private Table tableLaeufe;
    private BeanItemContainer<Lauf> laufContainer;
    private Panel panelTraining;

    // Converter
    private static final StreckeConverter STRECKE_CONVERTER = new StreckeConverter();
    private static final ZeitConverter ZEIT_CONVERTER = new ZeitConverter();

    public TagebuchUi(Trainingstagebuch trainingstagebuch) {
        
        super(trainingstagebuch);
        
        Layout layout = new FormLayout();
        setCompositionRoot(layout);

        // Auswahl des Monats
        auswahlMonat = new InlineDateField("");
        auswahlMonat.setDateFormat("MMMM yyyy");
        auswahlMonat.setResolution(Resolution.MONTH);
        auswahlMonat.setImmediate(true);
        auswahlMonat.setWidth("250px");
        auswahlMonat.setValue(new Date());
        layout.addComponent(auswahlMonat);

        // Beim Ändern des Monats muss die Tabelle entsprechend neu gefüllt werden
        auswahlMonat.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = -1029086726247628718L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                
                // Details des aktuell selektierten Trainings leeren
                fillTrainingDetailView(null);
                
                // Tabelle der Trainings füllen
                fillTrainingTable();
            }
        });
        
        
        // Container mit den angezeigten Trainingseinheiten 
        trainingContainer = new BeanItemContainer<Training>(Training.class);
        
        // Tabelle zur Anzeige des Trainings
        tableTraining = new Table("", trainingContainer);
        tableTraining.setColumnHeader("datum", "Datum");
        tableTraining.setColumnHeader("ort", "Ort");
        tableTraining.setColumnHeader("strecke", "Strecke");
        tableTraining.setColumnHeader("zeit", "Zeit");
        tableTraining.setColumnHeader("schnitt", "Schnitt [min/km]");
        tableTraining.setFooterVisible(true);
        tableTraining.setWidth("800px");
        tableTraining.setColumnWidth("datum", 100);
        tableTraining.setColumnWidth("ort", 300);
        tableTraining.setColumnWidth("strecke", 100);
        tableTraining.setColumnWidth("zeit", 100);
        tableTraining.setColumnWidth("schnitt", 100);
        tableTraining.setConverter("strecke", STRECKE_CONVERTER);
        tableTraining.setConverter("zeit", ZEIT_CONVERTER);
        tableTraining.setConverter("datum", new Converter<String, Date>() {

            private static final long serialVersionUID = 7733805593148338971L;

            private final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");    

            @Override
            public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
                return DATE_FORMATTER.format(value);
            }

            @Override
            public Class<Date> getModelType() {
                return Date.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
            
        });
        tableTraining.setHeight("300px");
        tableTraining.setImmediate(true);

        tableTraining.addGeneratedColumn("schnitt", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Training training = (Training)itemId;
                Integer schnitt = RunningDbUtil.berechneSchnitt(training.getStrecke(), training.getZeit());
                return new Label(ZEIT_CONVERTER.convertToPresentation(schnitt, String.class, null));
            }
        });
        tableTraining.setVisibleColumns(new Object[] {"datum", "ort", "strecke", "zeit", "schnitt"});
        
        // Selektierung einer Zeile => Details anzeigen
        tableTraining.setSelectable(true);
        tableTraining.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = -190929343225568472L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Object itemId = tableTraining.getValue();
                if(itemId!=null) {
                    fillTrainingDetailView((Training)itemId);
                } else {
                    fillTrainingDetailView(null);
                }
            }
        });
        layout.addComponent(tableTraining);
        
        // Panel mit den Details zu einem Training
        panelTraining = new Panel();
        panelTraining.setWidth("1000px");
        panelTraining.setVisible(false);
        FormLayout panelTrainingLayout = new FormLayout();
        panelTraining.setContent(panelTrainingLayout);
        layout.addComponent(panelTraining);
        
        // Anzeige des Schuhs
        textFieldSchuh = new TextField("Schuhe");
        textFieldSchuh.setEnabled(false);
        textFieldSchuh.setWidth("300px");
        panelTrainingLayout.addComponent(textFieldSchuh);

        // Anzeige der Bemerkungen
        textAreaBemerkungen = new TextArea("Bemerkungen");
        textAreaBemerkungen.setEnabled(false);
        textAreaBemerkungen.setWidth("800px");
        textAreaBemerkungen.setHeight("150px");
        textAreaBemerkungen.setWordwrap(false);
        panelTrainingLayout.addComponent(textAreaBemerkungen);

        // Container mit den Läufen des selektierten Trainings        
        laufContainer = new BeanItemContainer<Lauf>(Lauf.class);
        
        // Tabelle mit den Läufen des selektierten Trainings        
        tableLaeufe = new Table("Läufe", laufContainer);
        tableLaeufe.setColumnHeader("strecke", "Strecke");
        tableLaeufe.setColumnHeader("zeit", "Zeit");
        tableLaeufe.setColumnHeader("schnitt", "Schnitt [min/km]");
        tableLaeufe.setFooterVisible(true);
        tableLaeufe.setWidth("400px");
        tableLaeufe.setColumnWidth("strecke", 100);
        tableLaeufe.setColumnWidth("zeit", 100);
        tableLaeufe.setColumnWidth("schnitt", 100);
        tableLaeufe.setConverter("strecke", STRECKE_CONVERTER);
        tableLaeufe.setConverter("zeit", ZEIT_CONVERTER);
        tableLaeufe.setSortEnabled(false);
        tableLaeufe.setWidth("400px");
        tableLaeufe.setHeight("200px");
        tableLaeufe.addGeneratedColumn("schnitt", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Lauf lauf = (Lauf)itemId;
                Integer schnitt = RunningDbUtil.berechneSchnitt(lauf.getStrecke(), lauf.getZeit());
                return new Label(ZEIT_CONVERTER.convertToPresentation(schnitt, String.class, null));
            }
        });
        tableLaeufe.setVisibleColumns(new Object[] {"strecke", "zeit", "schnitt"});
        panelTrainingLayout.addComponent(tableLaeufe);
    }

    @Override
    public void show() {
        fillTrainingTable();
    }

    /**
     * Füllt die Trainings-Tabelle mit den Trainingseinheiten des aktuell gewählten Monats.
     */
    private void fillTrainingTable() {
        Date berichtsZeitraum = auswahlMonat.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(berichtsZeitraum);
        fillTableTraining(this.trainingstagebuch.getTrainingseinheiten(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)));
    }
    
    /**
     * Füllt die Trainings-Tabelle mit den angegebenen Trainingseinheiten.
     * @param trainingseinheiten Liste der Trainingseinheiten
     */
    private void fillTableTraining(List<Training> trainingseinheiten) {

        // Container leeren
        trainingContainer.removeAllItems();

        // Container neu füllen
        trainingContainer.addAll(trainingseinheiten);
        
        // Footer der Tabelle setzen
        tableTraining.setColumnFooter("datum", ""+trainingseinheiten.size());
        
        if(trainingseinheiten.size()>0) {
            Integer summeStrecke = RunningDbUtil.summeStrecke(trainingseinheiten);
            Integer summeZeit = RunningDbUtil.summeZeit(trainingseinheiten);
            tableTraining.setColumnFooter("strecke", STRECKE_CONVERTER.convertToPresentation(summeStrecke, String.class, null));
            tableTraining.setColumnFooter("zeit", ZEIT_CONVERTER.convertToPresentation(summeZeit, String.class, null));
            tableTraining.setColumnFooter("schnitt", ZEIT_CONVERTER.convertToPresentation(RunningDbUtil.berechneSchnitt(summeStrecke, summeZeit), String.class, null));
        } else {
            tableTraining.setColumnFooter("strecke", "");
            tableTraining.setColumnFooter("zeit", "");
            tableTraining.setColumnFooter("schnitt", "");
        }
        
    
    }
    
    /**
     * Füllt das Panel zur Anzeige der Details eines Trainings
     * @param training Training, dessen Details angezeigt werden sollen. null bedeutet Leeren und Verstecken des Panels
     */
    private void fillTrainingDetailView(Training training) {
        
        // Felder leeren
        this.tableLaeufe.removeAllItems();
        this.textAreaBemerkungen.setValue("");
        this.textFieldSchuh.setValue("");
        
        // Falls ein Training angegeben wurde, werden dessen Daten
        // in das Panel gesetzt.
        if(training!=null) {
            this.textAreaBemerkungen.setValue(training.getBemerkungen());
            this.laufContainer.addAll(training.getLaeufe());
            Schuh schuh = this.trainingstagebuch.getSchuh(training.getSchuh());
            if(schuh!=null) {
                this.textFieldSchuh.setValue(schuh.getKurzbezeichnung());
            }
        }
        
        // Sichtbar, falls ein Training angegeben wurde.
        this.panelTraining.setVisible(training!=null);
    }
    
}
