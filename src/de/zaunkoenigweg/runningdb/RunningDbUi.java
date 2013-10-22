package de.zaunkoenigweg.runningdb;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import de.zaunkoenigweg.runningdb.ui.MainUi;

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
        
        getPage().setTitle("RunningDB");
        setWidth("1050px");
        setHeight("825px");
        
        setContent(new MainUi());
    }

}