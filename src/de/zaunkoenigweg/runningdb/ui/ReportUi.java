package de.zaunkoenigweg.runningdb.ui;

import java.util.Locale;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;
import de.zaunkoenigweg.runningdb.domain.TrainingReport;


/**
 * UI showing reports.
 * 
 * @author Nikolaus Winter
 */
public class ReportUi extends AbstractUi {

    private static final long serialVersionUID = 6912936199154704718L;
    
    private Table table;
    private BeanItemContainer<TrainingReport.TrainingReportRow> reportContainer;
    
    private static final DistanceConverter DISTANCE_CONVERTER = new DistanceConverter();
    private static final TimeConverter TIME_CONVERTER = new TimeConverter();
    
    public ReportUi() {

        Layout layout = new FormLayout();
        setCompositionRoot(layout);

        // container w/ report data 
        reportContainer = new BeanItemContainer<TrainingReport.TrainingReportRow>(TrainingReport.TrainingReportRow.class);
        
        // table showing all trainings of chosen month
        table = new Table("", reportContainer);
        layout.addComponent(table);
        table.setColumnHeader("year", "Jahr");
        table.setColumnHeader("trainingCount", "Anzahl TE");
        table.setColumnHeader("distance", "Strecke");
        table.setColumnHeader("time", "Zeit");
        table.setColumnHeader("pace", "Schnitt");
        table.setWidth("434px");
        table.setColumnWidth("year", 40);
        table.setColumnWidth("trainingCount", 100);
        table.setColumnWidth("distance", 75);
        table.setColumnWidth("time", 75);
        table.setColumnWidth("pace", 75);
        table.setConverter("distance", DISTANCE_CONVERTER);
        table.setConverter("time", TIME_CONVERTER);
        table.setColumnAlignment("year", Align.CENTER);
        table.setColumnAlignment("trainingCount", Align.CENTER);
        table.setColumnAlignment("distance", Align.CENTER);
        table.setColumnAlignment("time", Align.CENTER);
        table.setColumnAlignment("pace", Align.CENTER);
        
        table.setConverter("year", new Converter<String, Integer>() {

            private static final long serialVersionUID = -3607638166456188141L;

            @Override
            public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws Converter.ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
                return "" + value;
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
        table.addGeneratedColumn("pace", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = 3014647975729983920L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                TrainingReport.TrainingReportRow trainingReportRow = (TrainingReport.TrainingReportRow)itemId;
                Integer schnitt = RunningDbUtil.getPace(trainingReportRow.getDistance(), trainingReportRow.getTime());
                return new Label(TIME_CONVERTER.convertToPresentation(schnitt, String.class, null));
            }
        });
        
        // footer
        table.setFooterVisible(true);
        
        table.setVisibleColumns(new Object[] {"year", "trainingCount", "distance", "time", "pace"});
           
    }

    @Override
    public void show() {
        TrainingReport traininigReport = getTrainingLog().generateTraininigReport();
        this.reportContainer.removeAllItems();
        this.reportContainer.addAll(traininigReport.getReportRows());
        this.table.setPageLength(this.reportContainer.size());
        
        // footer
        table.setColumnFooter("trainingCount", traininigReport.getSumRow().getTrainingCount()+"");
        table.setColumnFooter("distance", DISTANCE_CONVERTER.convertToPresentation(traininigReport.getSumRow().getDistance(), String.class, null));
        table.setColumnFooter("time", TIME_CONVERTER.convertToPresentation(traininigReport.getSumRow().getTime(), String.class, null));
        table.setColumnFooter("pace", TIME_CONVERTER.convertToPresentation(RunningDbUtil.getPace(traininigReport.getSumRow().getDistance(), traininigReport.getSumRow().getTime()), String.class, null));
        
    }

}
