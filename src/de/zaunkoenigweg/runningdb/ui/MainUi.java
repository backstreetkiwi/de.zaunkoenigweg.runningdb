package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * UI with main menu and content panel
 * 
 * @author Nikolaus Winter
 */
public class MainUi extends CustomComponent {

    private static final long serialVersionUID = -72302935704911518L;

    // training log
    TrainingLog trainingLog;
    
    // UIs
    private HomeUi homeUi;
    private TrainingLogUi tagebuchUi;
    private RecordsUi bestzeitenUi;
    private ShoeUi shoeUi;
    private ReportUi reportUi;
    private DataUi dataUi;
    private EmptyUi emptyUi;

    // content panel
    private Panel panelContent;
    
    /**
     * Create MainUI.
     */
    public MainUi() {
        
        // overall layout
        HorizontalLayout layout = new HorizontalLayout();
        layout.setStyleName("mainLayout");
        setCompositionRoot(layout);
        
        // main menu panel
        Panel panelMenu = new Panel();
        VerticalLayout layoutMenu = new VerticalLayout();
        panelMenu.setContent(layoutMenu);
        panelMenu.setStyleName("panelMainMenue");
        layout.addComponent(panelMenu);
        
        // button "training log"
        layoutMenu.addComponent(this.createMenueButton("Tagebuch", new Button.ClickListener() {
            
            private static final long serialVersionUID = 4925786265682557541L;

            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.tagebuchUi);
            }
        }, "menueButtonTagebuch"));
        
        // button "records"
        layoutMenu.addComponent(this.createMenueButton("Bestzeiten", new Button.ClickListener() {
            
            private static final long serialVersionUID = -5968455239778239160L;
            
            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.bestzeitenUi);
            }
        }, "menueButtonBestzeiten"));
        
        // button "reports"
        layoutMenu.addComponent(this.createMenueButton("Reports", new Button.ClickListener() {
            
            private static final long serialVersionUID = -5968455239778239160L;
            
            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.reportUi);
            }
        }, "menueButtonReports"));
        
        // button "shoes"
        layoutMenu.addComponent(this.createMenueButton("Schuhe", new Button.ClickListener() {
            
            private static final long serialVersionUID = -5968455239778239160L;
            
            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.shoeUi);
            }
        }, "menueButtonSchuhe"));
        
        // button "statistics"
        layoutMenu.addComponent(this.createMenueButton("Statistik", new Button.ClickListener() {
            
            private static final long serialVersionUID = -5968455239778239160L;
            
            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.emptyUi);
            }
        }, "menueButtonStatistik"));
        
        // button "data"
        layoutMenu.addComponent(this.createMenueButton("Daten", new Button.ClickListener() {
            
            private static final long serialVersionUID = -948084043318183201L;

            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.dataUi);
            }
        }, "menueButtonData"));
        
        // content panel
        panelContent = new Panel();
        panelContent.setSizeFull();
        panelContent.setStyleName("panelAppContent");
        layout.addComponent(panelContent);
        
        createUis();
        
        showUi(this.homeUi);
    }
    
    /**
     * Create all UIs
     */
    private void createUis() {
        this.homeUi = new HomeUi();
        this.tagebuchUi = new TrainingLogUi();
        this.bestzeitenUi = new RecordsUi();
        this.shoeUi = new ShoeUi();
        this.reportUi = new ReportUi();
        this.dataUi = new DataUi();
        this.emptyUi = new EmptyUi();
    }
    
    /**
     * Show given UI.
     * 
     * @param ui UI to show
     */
    private void showUi(AbstractUi ui) {
        
        // notify UI
        ui.show();
        
        // set content panel to show UI
        this.panelContent.setContent(ui);
    }
    
    /**
     * Create menue button.
     * @param caption button Caption
     * @param clickListener action for button click
     * @param styleName button style (im RunningDB-Theme) 
     * @return button
     */
    private Button createMenueButton(String caption, Button.ClickListener clickListener, String styleName) {
        Button button = new Button(caption);
        button.setStyleName(styleName);
        if(clickListener!=null) {
            button.addClickListener(clickListener);
        }
        return button;
    }
    
}
