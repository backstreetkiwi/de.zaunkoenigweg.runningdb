package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

public class MainUi extends CustomComponent {

    private static final long serialVersionUID = -72302935704911518L;

    // Trainingstagebuch
    Trainingstagebuch trainingstagebuch;
    
    // UIs
    private HomeUi homeUi;
    private TagebuchUi tagebuchUi;
    private BestzeitenUi bestzeitenUi;

    // Panel f�r den Content
    private Panel panelContent;
    
    public MainUi(Trainingstagebuch trainingstagebuch) {
        
        // Trainingstagebuch speichern
        this.trainingstagebuch = trainingstagebuch;
        
        // Layout des kpl. UI
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setHeight("800px");
        setCompositionRoot(layout);
        
        // Panel mit dem Men� (links)
        Panel panelMenu = new Panel();
        VerticalLayout layoutMenu = new VerticalLayout();
        layoutMenu.setMargin(true);
        panelMenu.setContent(layoutMenu);
        panelMenu.setWidth("250px");
        panelMenu.setHeight("100%");
        layout.addComponent(panelMenu);
        
        // Men�-Buttons 
        layoutMenu.addComponent(this.createMenueButton("Home", new Button.ClickListener() {
            
            private static final long serialVersionUID = 145150516505290696L;

            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.homeUi);
            }
        }));

        layoutMenu.addComponent(this.createMenueButton("Tagebuch", new Button.ClickListener() {
            
            private static final long serialVersionUID = 4925786265682557541L;

            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.tagebuchUi);
            }
        }));
        
        layoutMenu.addComponent(this.createMenueButton("Bestzeiten", new Button.ClickListener() {
            
            private static final long serialVersionUID = -5968455239778239160L;
            
            @Override
            public void buttonClick(ClickEvent event) {
                MainUi.this.showUi(MainUi.this.bestzeitenUi);
            }
        }));
        layoutMenu.addComponent(this.createMenueButton("Reports", null));
        layoutMenu.addComponent(this.createMenueButton("Schuhe", null));
        layoutMenu.addComponent(this.createMenueButton("Statistik", null));
        
        panelContent = new Panel();
        panelContent.setSizeFull();
        layout.addComponent(panelContent);
        layout.setExpandRatio(panelContent, 1.0f);
        
        // Alle UIs erzeugen
        initUis();
        
        // Home-UI anzeigen
        showUi(this.homeUi);
        
    }
    
    /**
     * Alle UIs erzeugen
     */
    private void initUis() {
        this.homeUi = new HomeUi(this.trainingstagebuch);
        this.tagebuchUi = new TagebuchUi(this.trainingstagebuch);
        this.bestzeitenUi = new BestzeitenUi(this.trainingstagebuch);
    }
    
    /**
     * Anzeige des UIs "Home".
     */
    private void showUi(AbstractUi ui) {
        ui.show();
        this.panelContent.setContent(ui);
    }
    
    /**
     * Men�-Button erzeugen
     * @param caption Beschriftung
     * @param clickListener Aktion f�r das Dr�cken des Buttons 
     * @return Men�-Button
     */
    private Button createMenueButton(String caption, Button.ClickListener clickListener) {
        Button button = new Button(caption);
        button.setWidth("200px");
        if(clickListener!=null) {
            button.addClickListener(clickListener);
        }
        return button;
    }
    
}
