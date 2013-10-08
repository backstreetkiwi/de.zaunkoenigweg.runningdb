package de.zaunkoenigweg.runningdb.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

import de.zaunkoenigweg.runningdb.domain.Run;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.Shoe;
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

    private static final DistanceConverter STRECKE_CONVERTER = new DistanceConverter();
    private static final TimeConverter ZEIT_CONVERTER = new TimeConverter();

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
        tableTrainingLog.setColumnHeader("date", "Datum");
        tableTrainingLog.setColumnHeader("location", "Ort");
        tableTrainingLog.setColumnHeader("distance", "Strecke");
        tableTrainingLog.setColumnHeader("time", "Zeit");
        tableTrainingLog.setColumnHeader("pace", "Schnitt");
        tableTrainingLog.setColumnHeader("shoe", "Schuhe");
        tableTrainingLog.setColumnHeader("comments", "");
        tableTrainingLog.setFooterVisible(true);
        tableTrainingLog.setPageLength(20);
        tableTrainingLog.setWidth("900px");
        tableTrainingLog.setColumnWidth("date", 75);
        tableTrainingLog.setColumnWidth("location", 250);
        tableTrainingLog.setColumnWidth("distance", 75);
        tableTrainingLog.setColumnWidth("time", 75);
        tableTrainingLog.setColumnWidth("pace", 75);
        tableTrainingLog.setColumnWidth("shoe", 200);
        tableTrainingLog.setColumnWidth("comments", 20);
        tableTrainingLog.setConverter("distance", STRECKE_CONVERTER);
        tableTrainingLog.setConverter("time", ZEIT_CONVERTER);
        tableTrainingLog.setConverter("date", new Converter<String, Date>() {

            private static final long serialVersionUID = 7733805593148338971L;

            private final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");    

            @Override
            public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale) throws Converter.ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
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
        
        tableTrainingLog.setConverter("shoe", new Converter<String, Integer>() {

            private static final long serialVersionUID = -4400228635403939493L;
            
            @Override
            public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws Converter.ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
                String result = "";
                if(value!=null && value>0) {
                    Shoe shoe = TrainingLogUi.this.trainingLog.getShoe(value);
                    if(shoe!=null) {
                        result = shoe.getShortname();
                    }
                }
                return result;
            }

            @Override
            public Class<Integer> getModelType() {
                return Integer.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
            
        });
        
        // pace is calculated into generated column
        tableTrainingLog.addGeneratedColumn("pace", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Training training = (Training)itemId;
                Integer schnitt = RunningDbUtil.getPace(training.getDistance(), training.getTime());
                return new Label(ZEIT_CONVERTER.convertToPresentation(schnitt, String.class, null));
            }
        });
        
        tableTrainingLog.addGeneratedColumn("comments", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -6512627057346466980L;
            
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Training training = (Training)itemId;
                if(StringUtils.isNotBlank(training.getComments())) {
                    Image image = new Image("", new ThemeResource("icons/bubble.png"));
                    image.setDescription(StringUtils.replace(training.getComments(), "\n", "<br/>"));
                    return image;
                }
                return null;
            }
        });
        
        tableTrainingLog.setImmediate(true);
        
        tableTrainingLog.setVisibleColumns(new Object[] {"date", "location", "distance", "time", "pace", "shoe", "comments"});
        
        
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
        tableRuns.setColumnHeader("distance", "Strecke");
        tableRuns.setColumnHeader("time", "Zeit");
        tableRuns.setColumnHeader("pace", "Schnitt [min/km]");
        tableRuns.setFooterVisible(false);
        tableRuns.setWidth("400px");
        tableRuns.setColumnWidth("distance", 100);
        tableRuns.setColumnWidth("time", 100);
        tableRuns.setColumnWidth("pace", 100);
        tableRuns.setConverter("distance", STRECKE_CONVERTER);
        tableRuns.setConverter("time", ZEIT_CONVERTER);
        tableRuns.setSortEnabled(false);
        tableRuns.setWidth("400px");

        // pace is calculated into generated column
        tableRuns.addGeneratedColumn("pace", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Run lauf = (Run)itemId;
                Integer schnitt = RunningDbUtil.getPace(lauf.getDistance(), lauf.getTime());
                return new Label(ZEIT_CONVERTER.convertToPresentation(schnitt, String.class, null));
            }
        });
        tableRuns.setVisibleColumns(new Object[] {"distance", "time", "pace"});
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
        fillTableTraining(this.trainingLog.getTrainings(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)));
    }
    
    /**
     * Fills training log table with given trainings.
     * @param trainings list of trainings to be shown
     */
    private void fillTableTraining(List<Training> trainings) {

        trainingContainer.removeAllItems();
        trainingContainer.addAll(trainings);
        
        // calculate and set footer values
        
        tableTrainingLog.setColumnFooter("date", ""+trainings.size());
        
        if(trainings.size()>0) {
            Integer summeStrecke = RunningDbUtil.sumDistance(trainings);
            Integer summeZeit = RunningDbUtil.sumTime(trainings);
            tableTrainingLog.setColumnFooter("distance", STRECKE_CONVERTER.convertToPresentation(summeStrecke, String.class, null));
            tableTrainingLog.setColumnFooter("time", ZEIT_CONVERTER.convertToPresentation(summeZeit, String.class, null));
            tableTrainingLog.setColumnFooter("pace", ZEIT_CONVERTER.convertToPresentation(RunningDbUtil.getPace(summeStrecke, summeZeit), String.class, null));
        } else {
            tableTrainingLog.setColumnFooter("distance", "");
            tableTrainingLog.setColumnFooter("time", "");
            tableTrainingLog.setColumnFooter("pace", "");
        }
        
    
    }
    
    /**
     * Fills training detail panel
     * @param training training to be shown in detail panel
     */
    private void refreshTrainingDetailView(Training training) {
        
        this.tableRuns.removeAllItems();
        
        boolean showPanel = false;
        if(training!=null && training.getRuns().size() > 1) {
            this.runContainer.addAll(training.getRuns());
            showPanel = true;
            this.tableRuns.setPageLength(training.getRuns().size());
        }
        
        this.panelTrainingDetails.setVisible(showPanel);
    }
    
}
