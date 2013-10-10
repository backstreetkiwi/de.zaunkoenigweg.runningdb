package de.zaunkoenigweg.runningdb.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import de.zaunkoenigweg.runningdb.domain.RecordDistance;
import de.zaunkoenigweg.runningdb.domain.RecordInfo;
import de.zaunkoenigweg.runningdb.domain.RecordInfo.RecordRun;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;

/**
 * UI showing record distances and fastest runs per distance.
 * 
 * Editing the record distances is also possible in this UI.
 * 
 * @author Nikolaus Winter
 */
public class RecordsUi extends AbstractUi {

    private static final long serialVersionUID = 3021578951350704103L;
    
    private static final int NUMBER_OF_RUNS_PER_RECORD_DISTANCE = 10;

    private ComboBox selectRecordDistance;
    private Panel panelRecordInfo;

    Button buttonAddRecordDistance;
    
    public RecordsUi() {

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setCompositionRoot(layout);

        // choice "record distance"
        this.selectRecordDistance = new ComboBox();
        this.selectRecordDistance.setStyleName("recordsUiSelectRecordDistance");
        layout.addComponent(this.selectRecordDistance);
        this.selectRecordDistance.setWidth("500px");
        this.selectRecordDistance.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        this.selectRecordDistance.setItemCaptionPropertyId("teaser");
        this.selectRecordDistance.setInputPrompt("Bitte auswählen...");
        this.selectRecordDistance.setNullSelectionAllowed(false);
        this.selectRecordDistance.setTextInputAllowed(false);
        this.selectRecordDistance.setImmediate(true);
        
        this.selectRecordDistance.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = 4706299051889826573L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(selectRecordDistance.getValue() instanceof RecordInfo) {
                    showRecordInfo((RecordInfo) selectRecordDistance.getValue());
                }
            }
        });

        this.panelRecordInfo = ComponentFactory.createPanel();
        layout.addComponent(this.panelRecordInfo);

        // button "add new record distance"
        buttonAddRecordDistance = ComponentFactory.createRegularButton("Strecke hinzufügen");
        layout.addComponent(buttonAddRecordDistance);

        buttonAddRecordDistance.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 8949212155808289711L;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                // create and show window to add new record distance
                RecordDistanceInputWindow.show(new RecordDistanceInputWindow.RecordDistanceCreatedListener() {

                    private static final long serialVersionUID = -823267736348626305L;

                    @Override
                    public void recordDistanceCreated(RecordDistance recordDistance) {
                        getTrainingLog().addRecordDistance(recordDistance);
                        fillSelectRecordDistance(recordDistance);
                    }

                });
            }
        });

    }

    @Override
    public void show() {
        fillSelectRecordDistance(null);
    }
    
    /**
     * Fills dropdown to select record distance.
     * If {@link RecordDistance} is given, it is preselected.
     * Otherwise, the first entry is preselected.
     * @param recordDistance preselected {@link RecordDistance}
     */
    private void fillSelectRecordDistance(RecordDistance recordDistance) {
        List<RecordInfo> records = getTrainingLog().getRecords();
        BeanItemContainer<RecordInfo> recordInfoContainer = new BeanItemContainer<RecordInfo>(RecordInfo.class);
        recordInfoContainer.addAll(records);
        this.selectRecordDistance.setContainerDataSource(recordInfoContainer);
        if(recordDistance!=null) {
            for (RecordInfo recordInfo : records) {
                if(recordDistance.equals(recordInfo.getRecordDistance())) {
                    this.selectRecordDistance.setValue(recordInfo);
                }
            }
        } else if (records.size()>0) {
            this.selectRecordDistance.setValue(records.get(0));
        }
    }

    /**
     * Shows given record info panel.
     * If {@link RecordInfo} ist given, its informatios are shown.
     * Otherwise, the first {@link RecordInfo}s informations are shown.
     * @param recordInfo {@link RecordInfo} to be shown
     */
    private void showRecordInfo(RecordInfo recordInfo) {
        
        if(recordInfo==null) {
            List<RecordInfo> records = getTrainingLog().getRecords();
            if(!records.isEmpty()) {
                recordInfo = records.get(0);
            } else {
                this.panelRecordInfo.setContent(null);
                return;
            }
        }

        Panel panel = ComponentFactory.createPanel();
        VerticalLayout panelLayout = new VerticalLayout();
        panel.setContent(panelLayout);
        panelLayout.addComponent(createRecordRunTable(recordInfo));
        panelLayout.addComponent(createButtonRemoveRecordDistance(recordInfo));
        this.panelRecordInfo.setContent(panel);
        
        
    }

    /**
     * Creates table for given record distance using info object.
     * @param recordInfo information regarding record distance
     * @return table for given record distance
     */
    private Table createRecordRunTable(final RecordInfo recordInfo) {

        BeanItemContainer<RecordRun> recordRunContainer = new BeanItemContainer<RecordRun>(RecordRun.class);
        recordRunContainer.addNestedContainerProperty("training.date");

        Table table = new Table(null, recordRunContainer);
        table.setStyleName("recordsUiRecordRunTable");
        table.setColumnHeader("rank", "");
        table.setColumnHeader("training.date", "Datum");
        table.setColumnHeader("time", "Zeit");
        table.setColumnHeader("pace", "Schnitt");
        table.setConverter("training.date", new Converter<String, Date>() {

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
        table.setConverter("time", new TimeConverter());
        table.setSortEnabled(false);
        table.setColumnWidth("rank", 50);
        table.setColumnWidth("training.date", 100);
        table.setColumnWidth("time", 100);
        table.setColumnWidth("pace", 100);
        table.setColumnAlignment("rank", Align.CENTER);
        table.setColumnAlignment("training.date", Align.CENTER);
        table.setColumnAlignment("time", Align.CENTER);
        table.setColumnAlignment("pace", Align.CENTER);
        table.setPageLength(NUMBER_OF_RUNS_PER_RECORD_DISTANCE);

        // rank is calculated into generated column
        table.addGeneratedColumn("rank", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;
            
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                RecordRun recordRun = (RecordRun) itemId;
                int rank = recordInfo.getRecordRuns().indexOf(recordRun);
                if(rank==0) {
                    return new Image("", new ThemeResource("icons/medal-gold.png"));
                } else if(rank==1) {
                    return new Image("", new ThemeResource("icons/medal-silver.png"));
                } else if(rank==2) {
                    return new Image("", new ThemeResource("icons/medal-bronze.png"));
                }
                return String.format("%d", rank+1);
            }
        });
        
        // pace is calculated into generated column
        table.addGeneratedColumn("pace", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                RecordRun recordRun = (RecordRun) itemId;
                return new TimeConverter().convertToPresentation(RunningDbUtil.getPace(recordInfo.getRecordDistance().getDistance(), recordRun.getTime()), String.class, null);
            }
        });
        
        table.setVisibleColumns("rank", "training.date", "time", "pace");

        recordRunContainer.addAll(recordInfo.getRecordRuns());
        
        return table;
    }

    /**
     * Creates a button to delete record distance
     * @param recordInfo information regarding record distance
     * @return button to delete record distance
     */
    private Button createButtonRemoveRecordDistance(final RecordInfo recordInfo) {

        Button button = ComponentFactory.createRegularButton("Löschen");
        button.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 7956107661600031856L;

            @Override
            public void buttonClick(ClickEvent event) {

                ConfirmationDialog.show("Soll die angegebene Bestzeit wirklich gelöscht werden?", new ConfirmationDialog.ConfimationDialogListener() {
                    
                    @Override
                    public void yes() {
                        // delete record time from training log
                        getTrainingLog().removeRecordDistance(recordInfo.getRecordDistance());
                        fillSelectRecordDistance(null);
                        showRecordInfo(null);
                    }
                    
                    @Override
                    public void no() {
                        // do nothing
                    }
                });
                               
            }
        });
        return button;
    }
    
}
