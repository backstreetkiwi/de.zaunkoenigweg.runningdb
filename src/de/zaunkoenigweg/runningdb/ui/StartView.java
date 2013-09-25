package de.zaunkoenigweg.runningdb.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

import de.zaunkoenigweg.runningdb.RunningDbUi;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;
import de.zaunkoenigweg.runningdb.domain.json.TrainingstagebuchJsonSerializer;

public class StartView extends CustomComponent implements View {

    private static final long serialVersionUID = -72302935704911518L;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");    
    
    Button buttonSchuhHinzufuegen;
    Button buttonTrainingHinzufuegen = new Button("Training Hinzufügen");
    Button buttonBestzeitenStreckenVerwalten = new Button("Verwalten der Bestzeitenstrecken");
    Button buttonTrainingstagebuchSpeichern = new Button("Speichern des Trainingstagebuchs");
    Table tableStatistik;
    Table tableTraining;
    InlineDateField auswahlBerichtsdatum;
    
    Navigator navigator;

    private Trainingstagebuch trainingstagebuch;
    
    public StartView() {
        
        Layout layout = new FormLayout();
        
        buttonTrainingHinzufuegen.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(RunningDbUi.VIEW_EDIT_TRAINING);
                
            }
        });
        
        // Image as a file resource
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        
        FileResource resource1 = new FileResource(new File(basepath + "/WEB-INF/images/footsteps-16.png"));
        FileResource resource2 = new FileResource(new File(basepath + "/WEB-INF/images/shoes.jpg"));

        buttonSchuhHinzufuegen = new Button("Schuh Hinzufügen");
        buttonSchuhHinzufuegen.setWidth("200px");
        buttonSchuhHinzufuegen.setIcon(resource1);
        buttonSchuhHinzufuegen.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(RunningDbUi.VIEW_EDIT_SCHUH);
                
            }
        });
        
        buttonBestzeitenStreckenVerwalten.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(RunningDbUi.VIEW_BESTZEITENSTRECKEN);
                
            }
        });
        
        buttonTrainingstagebuchSpeichern.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ssSSS");
                    String json = TrainingstagebuchJsonSerializer.writeToJson(trainingstagebuch);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:/dev/vaadin/tagebuch.json")));
                    writer.append(json);
                    writer.flush();
                    writer.close();
                    writer = new BufferedWriter(new FileWriter(new File(String.format("C:/dev/vaadin/tagebuch_%s.json", simpleDateFormat.format(new Date())))));
                    writer.append(json);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
        
        tableStatistik = new Table("Statistik");
        tableStatistik.addContainerProperty("info", String.class, "");
        tableStatistik.addContainerProperty("wert", String.class, "");
        tableStatistik.setSortEnabled(false);
        tableStatistik.setColumnHeader("info", "");
        tableStatistik.setColumnHeader("wert", "");
        tableStatistik.setWidth("600px");
        layout.addComponent(tableStatistik);

        layout.addComponent(buttonTrainingHinzufuegen);
        layout.addComponent(buttonSchuhHinzufuegen);
        layout.addComponent(buttonBestzeitenStreckenVerwalten);
        layout.addComponent(buttonTrainingstagebuchSpeichern);
        
        auswahlBerichtsdatum = new InlineDateField("");
        auswahlBerichtsdatum.setDateFormat("MMMM yyyy");
        auswahlBerichtsdatum.setResolution(Resolution.MONTH);
        auswahlBerichtsdatum.setImmediate(true);
        auswahlBerichtsdatum.setWidth("250px");
        auswahlBerichtsdatum.setValue(new Date());
        layout.addComponent(auswahlBerichtsdatum);
        
        auswahlBerichtsdatum.addValueChangeListener(new Property.ValueChangeListener() {
            
            @Override
            public void valueChange(ValueChangeEvent event) {
                fillTableTraining();
            }
        });
        
        tableTraining = new Table("");
        tableTraining.addContainerProperty("datum", String.class, "");
        tableTraining.addContainerProperty("ort", String.class, "");
        tableTraining.addContainerProperty("bemerkungen", String.class, "");
        tableTraining.addContainerProperty("strecke", String.class, "");
        tableTraining.addContainerProperty("zeit", String.class, "");
        tableTraining.addContainerProperty("schnitt", String.class, "");
        tableTraining.setSortEnabled(false);
        tableTraining.setColumnHeader("datum", "Datum");
        tableTraining.setColumnHeader("ort", "Ort");
        tableTraining.setColumnHeader("bemerkungen", "Bemerkungen");
        tableTraining.setColumnHeader("strecke", "Strecke");
        tableTraining.setColumnHeader("zeit", "Zeit");
        tableTraining.setColumnHeader("schnitt", "Schnitt");
        tableTraining.setFooterVisible(true);
        tableTraining.setWidth("1500px");
        layout.addComponent(tableTraining);
        
        setCompositionRoot(layout);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        StreckeConverter streckeConverter = new StreckeConverter();
        ZeitConverter zeitConverter = new ZeitConverter();
        Notification.show("RunningDB by Nikolaus Winter, (c) 2013");
        this.navigator = event.getNavigator();
        trainingstagebuch = (Trainingstagebuch) VaadinSession.getCurrent().getAttribute("trainingstagebuch");

        
        tableStatistik.removeAllItems();
        
        // Statistik anzeigen
        if(trainingstagebuch!=null) {
            
            tableStatistik.addItem(new Object[] { "Anzahl Trainingseinheiten", ""+trainingstagebuch.getTrainingseinheiten().size() }, null);
            tableStatistik.addItem(new Object[] { "gesamt gelaufene Strecke (Meter)", streckeConverter.convertToPresentation(trainingstagebuch.getStrecke(), String.class, null)}, null);
            tableStatistik.addItem(new Object[] { "gesamt gelaufene Zeit (Stunden)", zeitConverter.convertToPresentation(trainingstagebuch.getZeit(), String.class, null)}, null);
            tableStatistik.addItem(new Object[] { "Gesamtschnitt", zeitConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(trainingstagebuch.getStrecke(), trainingstagebuch.getZeit()), String.class, null)}, null);
            tableStatistik.addItem(new Object[] { "Anzahl Bestzeitenstrecken", ""+trainingstagebuch.getBestzeitStrecken().size()}, null);
            tableStatistik.addItem(new Object[] { "Anzahl Schuhe", ""+trainingstagebuch.getSchuhe().size()}, null);
        }

        fillTableTraining();
    }

    private void fillTableTraining(List<Training> trainingseinheiten) {
        StreckeConverter streckeConverter = new StreckeConverter();
        ZeitConverter zeitConverter = new ZeitConverter();
        tableTraining.removeAllItems();

        for (Training training : trainingseinheiten) {
            
            tableTraining.addItem(new Object[] { DATE_FORMATTER.format(training.getDatum()), training.getOrt() , training.getBemerkungen(), streckeConverter.convertToPresentation(training.getStrecke(), String.class, null), zeitConverter.convertToPresentation(training.getZeit(), String.class, null), zeitConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(training.getStrecke(), training.getZeit()), String.class, null) }, null);
        }
        
        tableTraining.setColumnFooter("datum", ""+trainingseinheiten.size());
        
        if(trainingseinheiten.size()>0) {
            Integer summeStrecke = RunningDbUtil.summeStrecke(trainingseinheiten);
            Integer summeZeit = RunningDbUtil.summeZeit(trainingseinheiten);
            tableTraining.setColumnFooter("strecke", streckeConverter.convertToPresentation(summeStrecke, String.class, null));
            tableTraining.setColumnFooter("zeit", zeitConverter.convertToPresentation(summeZeit, String.class, null));
            tableTraining.setColumnFooter("schnitt", zeitConverter.convertToPresentation(RunningDbUtil.berechneSchnitt(summeStrecke, summeZeit), String.class, null));
        } else {
            tableTraining.setColumnFooter("strecke", "");
            tableTraining.setColumnFooter("zeit", "");
            tableTraining.setColumnFooter("schnitt", "");
        }
        
    
    }
    
    private void fillTableTraining() {
        Date berichtsZeitraum = auswahlBerichtsdatum.getValue();
        Calendar calli = Calendar.getInstance();
        calli.setTime(berichtsZeitraum);
        
        fillTableTraining(trainingstagebuch.getTrainingseinheiten(calli.get(Calendar.YEAR), calli.get(Calendar.MONTH)));
    }
    
}
