package de.zaunkoenigweg.runningdb.ui;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window;

/**
 * Splash Screen.
 * 
 * The dialog is shown by calling the static method {@link #show(String)}.
 * 
 * @author Nikolaus Winter
 */
public class SplashScreen extends Window {

    private static final long serialVersionUID = 5295700881097319215L;

    /**
     * Displays the RunningDB splash screen.
     */
    public static void show() {

        Notification splashScreen = new Notification("", Type.HUMANIZED_MESSAGE);
        
        splashScreen.setStyleName("splash");
        splashScreen.setDelayMsec(1000);
        splashScreen.setPosition(Position.MIDDLE_CENTER);
        splashScreen.setIcon(new ThemeResource("images/splash.png"));

        splashScreen.show(Page.getCurrent());
    }

}
