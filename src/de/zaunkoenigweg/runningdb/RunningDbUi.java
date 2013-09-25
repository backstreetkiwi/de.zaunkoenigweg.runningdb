package de.zaunkoenigweg.runningdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.annotation.WebServlet;

import org.json.JSONException;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;
import de.zaunkoenigweg.runningdb.domain.json.TrainingstagebuchJsonSerializer;
import de.zaunkoenigweg.runningdb.ui.BestzeitenUi;
import de.zaunkoenigweg.runningdb.ui.EditSchuh;
import de.zaunkoenigweg.runningdb.ui.EditTraining;
import de.zaunkoenigweg.runningdb.ui.MainUi;
import de.zaunkoenigweg.runningdb.ui.StartView;

@SuppressWarnings("serial")
@Theme("de_zaunkoenigweg_runningdb")
public class RunningDbUi extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = RunningDbUi.class)
    public static class Servlet extends VaadinServlet {
    }

    public static final String VIEW_START = "";
    public static final String VIEW_EDIT_TRAINING = "VIEW_EDIT_TRAINING";
    public static final String VIEW_EDIT_SCHUH = "VIEW_EDIT_SCHUH";
    public static final String VIEW_BESTZEITENSTRECKEN = "VIEW_BESTZEITENSTRECKEN";

    @Override
    protected void init(VaadinRequest request) {
        
        StringBuffer json = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("C:/dev/vaadin/tagebuch.json")));
            String line = reader.readLine();
            while(line != null) {
                json.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        Trainingstagebuch trainingstagebuch = null;

        try {
            trainingstagebuch = TrainingstagebuchJsonSerializer.readFromJson(json.toString()); 
            VaadinSession.getCurrent().setAttribute("trainingstagebuch", trainingstagebuch);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        getPage().setTitle("RunningDB");
        
        setContent(new MainUi(trainingstagebuch));
    }

}