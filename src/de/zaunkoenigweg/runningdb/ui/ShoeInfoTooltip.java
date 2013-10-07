package de.zaunkoenigweg.runningdb.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.zaunkoenigweg.runningdb.domain.ShoeInfo;

/**
 * Tooltip window for shoe information.
 * 
 * @author Nikolaus Winter
 */
public class ShoeInfoTooltip extends Window {
    
    private static final long serialVersionUID = 4588565219011528430L;

    /**
     * Private Constructor, used only by {@link #show(ShoeInfo)}
     * @param shoeInfo shoe information bean
     * @param positionX x-coordinate of desired tooltip position
     * @param positionY y-coordinate of desired tooltip position
     */
    private ShoeInfoTooltip(final ShoeInfo shoeInfo, int positionX, int positionY) {
        
        setCaption(shoeInfo.getShoe().getShortname());
        setModal(true);
        setClosable(false);
        setResizable(false);
        setDraggable(false);
        setPositionX(positionX);
        setPositionY(positionY);
        
        final byte[] imageData = shoeInfo.getShoe().getImage();
        if(imageData!=null && imageData.length>0) {
            Image image = new Image("", new StreamResource(new StreamSource() {
                
                private static final long serialVersionUID = 3823439977115618962L;

                @Override
                public InputStream getStream() {
                    return new ByteArrayInputStream(imageData);
                }
            }, "shoe.jpg"));
            
            setContent(image);
        }

    }
    
    
    /**
     * Shows the Tooltip.
     * @param shoeInfo shoe information bean
     * @param positionX x-coordinate of desired tooltip position
     * @param positionY y-coordinate of desired tooltip position
     */
    public static void show(ShoeInfo shoeInfo, int positionX, int positionY) {
        final ShoeInfoTooltip window = new ShoeInfoTooltip(shoeInfo, positionX, positionY);
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
