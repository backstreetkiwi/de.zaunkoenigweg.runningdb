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

import de.zaunkoenigweg.runningdb.domain.Run;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * UI to browse training log
 * 
 * @author Nikolaus Winter
 */
public class TrainingLogUi extends AbstractUi {

    private static final long serialVersionUID = 6470235043474537736L;

    private InlineDateField dateFieldMonth;
    private Table tableTrainingLog;
    private BeanItemContainer<Training> trainingContainer;

    private Table tableRuns;
    private BeanItemContainer<Run> runContainer;
    private Panel panelTrainingDetails;

    private static final StreckeConverter STRECKE_CONVERTER = new StreckeConverter();
    private static final ZeitConverter ZEIT_CONVERTER = new ZeitConverter();

    /**
     * Create TrainingLogUI.
     * 
     * @param trainingLog training log to work with
     */
    public TrainingLogUi(TrainingLog trainingLog) {
        
        super(trainingLog);
        
        Layout layout = new FormLayout();
        setCompositionRoot(layout);

        // choice of month
        dateFieldMonth = new InlineDateField("");
        layout.addComponent(dateFieldMonth);
        dateFieldMonth.setDateFormat("MMMM yyyy");
        dateFieldMonth.setResolution(Resolution.MONTH);
        dateFieldMonth.setImmediate(true);
        dateFieldMonth.setWidth("250px");
        dateFieldMonth.setValue(new Date());

        // change of month selection reloads training table and clears training details 
        dateFieldMonth.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = -1029086726247628718L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                refreshTrainingDetailView(null);
                refreshTrainingTable();
            }
        });
        
        
        // container for all trainings of chosen month 
        trainingContainer = new BeanItemContainer<Training>(Training.class);
        
        // table showing all trainings of chosen month
        tableTrainingLog = new Table("", trainingContainer);
        layout.addComponent(tableTrainingLog);
        tableTrainingLog.setColumnHeader("datum", "Datum");
        tableTrainingLog.setColumnHeader("ort", "Ort");
        tableTrainingLog.setColumnHeader("strecke", "Strecke");
        tableTrainingLog.setColumnHeader("zeit", "Zeit");
        tableTrainingLog.setColumnHeader("schnitt", "Schnitt [min/km]");
        tableTrainingLog.setFooterVisible(true);
        tableTrainingLog.setPageLength(15);
        tableTrainingLog.setWidth("800px");
        tableTrainingLog.setColumnWidth("datum", 100);
        tableTrainingLog.setColumnWidth("ort", 300);
        tableTrainingLog.setColumnWidth("strecke", 100);
        tableTrainingLog.setColumnWidth("zeit", 100);
        tableTrainingLog.setColumnWidth("schnitt", 100);
        tableTrainingLog.setConverter("strecke", STRECKE_CONVERTER);
        tableTrainingLog.setConverter("zeit", ZEIT_CONVERTER);
        tableTrainingLog.setConverter("datum", new Converter<String, Date>() {

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
        tableTrainingLog.setImmediate(true);

        // pace is calculated into generated column
        tableTrainingLog.addGeneratedColumn("schnitt", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Training training = (Training)itemId;
                Integer schnitt = RunningDbUtil.berechneSchnitt(training.getStrecke(), training.getZeit());
                return new Label(ZEIT_CONVERTER.convertToPresentation(schnitt, String.class, null));
            }
        });
        tableTrainingLog.setVisibleColumns(new Object[] {"datum", "ort", "strecke", "zeit", "schnitt"});
        
        
        // show training details for selected training
        tableTrainingLog.setSelectable(true);
        tableTrainingLog.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = -190929343225568472L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Object itemId = tableTrainingLog.getValue();
                if(itemId!=null) {
                    refreshTrainingDetailView((Training)itemId);
                } else {
                    refreshTrainingDetailView(null);
                }
            }
        });
        
        
        // panel showing details of training, initially invisible
        panelTrainingDetails = new Panel();
        layout.addComponent(panelTrainingDetails);
        panelTrainingDetails.setWidth("500px");
        panelTrainingDetails.setVisible(false);
        FormLayout panelTrainingLayout = new FormLayout();
        panelTrainingDetails.setContent(panelTrainingLayout);
        
        runContainer = new BeanItemContainer<Run>(Run.class);
        
        // table showing all runs of selected training        
        tableRuns = new Table("", runContainer);
        panelTrainingLayout.addComponent(tableRuns);
        tableRuns.setColumnHeader("strecke", "Strecke");
        tableRuns.setColumnHeader("zeit", "Zeit");
        tableRuns.setColumnHeader("schnitt", "Schnitt [min/km]");
        tableRuns.setFooterVisible(false);
        tableRuns.setWidth("400px");
        tableRuns.setColumnWidth("strecke", 100);
        tableRuns.setColumnWidth("zeit", 100);
        tableRuns.setColumnWidth("schnitt", 100);
        tableRuns.setConverter("strecke", STRECKE_CONVERTER);
        tableRuns.setConverter("zeit", ZEIT_CONVERTER);
        tableRuns.setSortEnabled(false);
        tableRuns.setWidth("400px");

        // pace is calculated into generated column
        tableRuns.addGeneratedColumn("schnitt", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Run lauf = (Run)itemId;
                Integer schnitt = RunningDbUtil.berechneSchnitt(lauf.getStrecke(), lauf.getZeit());
                return new Label(ZEIT_CONVERTER.convertToPresentation(schnitt, String.class, null));
            }
        });
        tableRuns.setVisibleColumns(new Object[] {"strecke", "zeit", "schnitt"});
    }

    @Override
    public void show() {
        refreshTrainingTable();
    }

    /**
     * Fills training log table with trainings of chosen month.
     */
    private void refreshTrainingTable() {
        Date chosenMonth = dateFieldMonth.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(chosenMonth);
        fillTableTraining(this.trainingstagebuch.getTrainingseinheiten(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)));
    }
    
    /**
     * Fills training log table with given trainings.
     * @param trainings list of trainings to be shown
     */
    private void fillTableTraining(List<Training> trainings) {

        trainingContainer.removeAllItems();
        trainingContainer.addAll(trainings);
        
        // calculate and set footer values
        
        tableTrainingLog.setColumnFooter("datum", ""+trainings.size());
        
        if(trainings.size()>0) {
            Integer summeStrecke = RunningDbUtil.summeStrecke(trainings);
            Integer summeZeit = RunningDbUtil.summeZeit(trainings);
            tableTrainingLog.setColumnFooter("strecke", STRECKE_CONVERTER.convertToPresentation(summeStrecke, String.class, null));
            tableTrainingLog.setColumnFooter("zeit", ZEIT_CONVERTER.convertToPresentation(summeZeit, String.class, null));
            tableTrainingLog.setColumnFooter("schnitt", ZEIT_CONVERTER.convertToPresentation(RunningDbUtil.berechneSchnitt(summeStrecke, summeZeit), String.class, null));
        } else {
            tableTrainingLog.setColumnFooter("strecke", "");
            tableTrainingLog.setColumnFooter("zeit", "");
            tableTrainingLog.setColumnFooter("schnitt", "");
        }
        
    
    }
    
    /**
     * Fills training detail panel
     * @param training training to be shown in detail panel
     */
    private void refreshTrainingDetailView(Training training) {
        
        this.tableRuns.removeAllItems();
        
        boolean showPanel = false;
        if(training!=null && training.getLaeufe().size() > 1) {
            this.runContainer.addAll(training.getLaeufe());
            showPanel = true;
            this.tableRuns.setPageLength(training.getLaeufe().size());
        }
        
        this.panelTrainingDetails.setVisible(showPanel);
    }
    
}
