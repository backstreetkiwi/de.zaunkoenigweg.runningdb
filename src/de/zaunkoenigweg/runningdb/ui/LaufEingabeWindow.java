package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.zaunkoenigweg.runningdb.domain.Lauf;

/**
 * Fenster zur Eingabe eines Laufs.
 * 
 * @author Nikolaus Winter
 */
public class LaufEingabeWindow extends Window {

    private static final long serialVersionUID = 6366542591098712326L;

    // Converter für Strecke und Zeit
    private StreckeConverter streckeConverter = new StreckeConverter();
    private ZeitConverter zeitConverter = new ZeitConverter();

    TextField textFieldStrecke;
    TextField textFieldZeit;
    private Button buttonSpeichern;
    private Button buttonAbbrechen;
    BeanItem<Lauf> lauf;


    public LaufEingabeWindow(final EditTraining editTrainingView) {
        
        // Lauf-Objekt erstellen
        this.lauf = new BeanItem<Lauf>(new Lauf());
        
        // allg. Eigenschaften des Fensters 
        setCaption("Eingabe eines Trainingslaufs");
        setClosable(false);
        setHeight("400px");
        setWidth("500px");
        setResizable(false);
        setModal(true);
        center();

        // Formular-Layout
        Layout layout = new FormLayout();
        setContent(layout);
        
        // Eingabe der Strecke
        this.textFieldStrecke = new TextField("Strecke");
        this.textFieldStrecke.setWidth("200px");
        this.textFieldStrecke.setPropertyDataSource(this.lauf.getItemProperty("strecke"));
        this.textFieldStrecke.setImmediate(true);
        this.textFieldStrecke.setConverter(this.streckeConverter);
        this.textFieldStrecke.setConversionError("Die Laufstrecke muss zwischen 100 m und 42.195 m liegen.");
        this.textFieldStrecke.addValidator(new IntegerRangeValidator("Die Laufstrecke muss zwischen 100 m und 42.195 m liegen.", 100, 42195));
        this.textFieldStrecke.setValidationVisible(true);
        this.textFieldStrecke.setBuffered(true);
        this.textFieldStrecke.focus();
        layout.addComponent(this.textFieldStrecke);

        // Eingabe der Zeit
        this.textFieldZeit = new TextField("Zeit");
        this.textFieldZeit.setWidth("200px");
        this.textFieldZeit.setPropertyDataSource(this.lauf.getItemProperty("zeit"));
        this.textFieldZeit.setImmediate(true);
        this.textFieldZeit.setConverter(this.zeitConverter);
        this.textFieldZeit.setConversionError("Bitte geben Sie einen gültigen Zeitwert ein.");
        this.textFieldZeit.addValidator(new IntegerRangeValidator("Die Zeit muss zwischen 1:00 Minute und 9:59:59 Stunden liegen.", 60, 35999));
        this.textFieldZeit.setValidationVisible(true);
        layout.addComponent(this.textFieldZeit);

        // Button speichern
        buttonSpeichern = new Button("Speichern");
        layout.addComponent(buttonSpeichern);
        buttonSpeichern.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                if(textFieldStrecke.isValid() && textFieldZeit.isValid()) {
                    textFieldStrecke.commit();
                    textFieldZeit.commit();
                    if(lauf.getBean().getStrecke()!=null && lauf.getBean().getZeit()!=null) {
                        editTrainingView.addLaufToCurrentTraining(lauf.getBean());
                        close();
                    }
                }
            }
        });

        // Button Abbrechen
        buttonAbbrechen = new Button("Abbrechen");
        layout.addComponent(buttonAbbrechen);
        buttonAbbrechen.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -7145011427729445317L;

            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        
    
    }
    
}
