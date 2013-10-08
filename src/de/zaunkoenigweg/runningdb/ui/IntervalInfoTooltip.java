package de.zaunkoenigweg.runningdb.ui;

import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.zaunkoenigweg.runningdb.domain.Run;
import de.zaunkoenigweg.runningdb.domain.RunningDbUtil;

/**
 * Tooltip window showing information about training invervals.
 * 
 * @author Nikolaus Winter
 */
public class IntervalInfoTooltip extends Window {
    
    private static final long serialVersionUID = -947776827149609637L;

    private static final DistanceConverter DISTANCE_CONVERTER = new DistanceConverter();
    private static final TimeConverter TIME_CONVERTER = new TimeConverter();

    private Table tableRuns;
    private BeanItemContainer<Run> runContainer;

    /**
     * Private Constructor, used only by {@link #show(List, int, int)}
     * @param runs list of runs to display
     * @param positionX x-coordinate of desired tooltip position
     * @param positionY y-coordinate of desired tooltip position
     */
    private IntervalInfoTooltip(final List<Run> runs, int positionX, int positionY) {
        
        setCaption("Teilstrecken");
        setModal(true);
        setClosable(false);
        setResizable(false);
        setDraggable(false);
        setPositionX(positionX);
        setPositionY(positionY);
        
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        // container with runs of training
        runContainer = new BeanItemContainer<Run>(Run.class);
        runContainer.addAll(runs);

        // table showing all runs of selected training        
        tableRuns = new Table("", runContainer);
        layout.addComponent(tableRuns);
        tableRuns.setColumnHeader("distance", "Strecke");
        tableRuns.setColumnHeader("time", "Zeit");
        tableRuns.setColumnHeader("pace", "Schnitt [min/km]");
        tableRuns.setFooterVisible(false);
        tableRuns.setWidth("341px");
        tableRuns.setColumnWidth("distance", 100);
        tableRuns.setColumnWidth("time", 100);
        tableRuns.setColumnWidth("pace", 100);
        tableRuns.setConverter("distance", DISTANCE_CONVERTER);
        tableRuns.setConverter("time", TIME_CONVERTER);
        tableRuns.setSortEnabled(false);
        tableRuns.setPageLength(runs.size());

        // pace is calculated into generated column
        tableRuns.addGeneratedColumn("pace", new Table.ColumnGenerator() {
            
            private static final long serialVersionUID = -2108447767546846249L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Run lauf = (Run)itemId;
                Integer schnitt = RunningDbUtil.getPace(lauf.getDistance(), lauf.getTime());
                return new Label(TIME_CONVERTER.convertToPresentation(schnitt, String.class, null));
            }
        });
        tableRuns.setVisibleColumns(new Object[] {"distance", "time", "pace"});

    }
    
    
    /**
     * Shows the Tooltip.
     * @param runs list of runs to display
     * @param positionX x-coordinate of desired tooltip position
     * @param positionY y-coordinate of desired tooltip position
     */
    public static void show(List<Run> runs, int positionX, int positionY) {
        final IntervalInfoTooltip window = new IntervalInfoTooltip(runs, positionX, positionY);
        window.addClickListener(new MouseEvents.ClickListener() {
            
            private static final long serialVersionUID = -6476188495645263366L;

            @Override
            public void click(ClickEvent event) {
                window.close();
            }
        });
        UI.getCurrent().addWindow(window);
    }
    
}
