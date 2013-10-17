package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Dialog to confirm an action.
 * 
 * The dialog is shown by calling the static method {@link #show(String)}. This method is provided
 * with the question and a {@link ConfimationDialogListener} to specify the actions to be executed
 * after a choice was made. 
 * 
 * @author Nikolaus Winter
 */
public class ConfirmationDialog extends Window {

    private static final long serialVersionUID = -4847225216811762318L;

    private Label labelQuestion;
    private Button buttonYes;
    private Button buttonNo;
    
    private ConfimationDialogListener confimationDialogListener;

    /**
     * Private Constructor, used only by {@link #show(String)}
     * @param question Question to confirm
     * @param confimationDialogListener Listener containing actions to be executed after a choice was made.
     */
    private ConfirmationDialog(String question, ConfimationDialogListener confimationDialogListener) {
        
        this.confimationDialogListener = confimationDialogListener;

        setCaption("RunningDB");
        setClosable(false);
        setHeight("200px");
        setWidth("400px");
        setResizable(false);
        setModal(true);
        center();
        setStyleName("popupWindow");

        // FormLayout
        Layout layout = new FormLayout();
        setContent(layout);
        
        // Question
        this.labelQuestion = new Label(question);
        layout.addComponent(labelQuestion);
        
        // Button "Yes"
        buttonYes = ComponentFactory.createButton("Ja");
        buttonYes.setWidth("100px");
        layout.addComponent(buttonYes);
        buttonYes.addClickListener(new Button.ClickListener() {
            
            private static final long serialVersionUID = 6452612785261603624L;

            @Override
            public void buttonClick(ClickEvent event) {
                close();
                ConfirmationDialog.this.confimationDialogListener.yes();
            }
        });
        
        // Button "No"
        buttonNo = ComponentFactory.createButton("Nein");
        buttonNo.setWidth("100px");
        layout.addComponent(buttonNo);
        buttonNo.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 2907247592144217208L;

            @Override
            public void buttonClick(ClickEvent event) {
                close();
                ConfirmationDialog.this.confimationDialogListener.no();
            }
        });
        
    }
    
    /**
     * Displays a confirmation dialog.
     * @param question Question to confirm
     * @param confimationDialogListener Listener containing actions to be executed after a choice was made.
     */
    public static void show(String question, ConfimationDialogListener confimationDialogListener) {
        ConfirmationDialog dialog = new ConfirmationDialog(question, confimationDialogListener);
        UI.getCurrent().addWindow(dialog);
    }
    
    /**
     * Listener to specify actions to be executed after a choice is made in the confirmation dialog. 
     * @author Nikolaus Winter
     */
    public interface ConfimationDialogListener {
        
        /**
         * Action to be executed after a positive choice was made, e.g. question was confirmed.
         */
        public void yes();
        
        /**
         * Action to be executed after a negative choice was made, e.g. question was NOT confirmed.
         */
        public void no();
        
    }
    
}
