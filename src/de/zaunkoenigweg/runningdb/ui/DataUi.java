package de.zaunkoenigweg.runningdb.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;

import de.zaunkoenigweg.runningdb.domain.json.TrainingLogJsonSerializer;

/**
 * UI for up- and downloading data as JSON.
 * 
 * @author Nikolaus Winter
 */
public class DataUi extends AbstractUi {

    private static final long serialVersionUID = 7252833037429831581L;
    
    private Button downloadButton;
    private Upload uploadButton;
    private ByteArrayOutputStream uploadedData;

    public DataUi() {
        
        Layout layout = new FormLayout();
        setCompositionRoot(layout);

        this.downloadButton = new Button("JSON Download");
        this.downloadButton.setStyleName("downloadTrainingLogButton");
        this.downloadButton.setIcon(new ThemeResource("icons/download.png"));
        new FileDownloader(createJsonStreamResource()).extend(downloadButton);
        layout.addComponent(this.downloadButton);
        
        this.uploadedData = new ByteArrayOutputStream();
        
        this.uploadButton = new Upload("", new Upload.Receiver() {
            
            private static final long serialVersionUID = 2365141000996648573L;

            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                return DataUi.this.uploadedData;
            }
        });
        this.uploadButton.setImmediate(true);
        this.uploadButton.setButtonCaption("JSON Upload");
        this.uploadButton.addSucceededListener(new Upload.SucceededListener() {
            
            private static final long serialVersionUID = -2603785567269708936L;

            @Override
            public void uploadSucceeded(SucceededEvent event) {
                String json = null;
                try {
                    json = DataUi.this.uploadedData.toString("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // UTF-8 IS supported. Yes, I'm sure!
                }
                try {
                    setTrainingLog(TrainingLogJsonSerializer.readFromJson(json));
                } catch (JSONException e) {
                    // ignore
                } catch (IOException e) {
                    // ignore
                }
            }
        });
        layout.addComponent(this.uploadButton);
        
    }

    @Override
    public void show() {
    }

    /**
     * Creates {@link StreamResource} containing JSON file representing current training log.
     * @return JSON file representing current training log
     */
    private StreamResource createJsonStreamResource() {
        return new StreamResource(new StreamSource() {

            private static final long serialVersionUID = 9215994727091488902L;

            @Override
            public InputStream getStream() {
                
                String json = "";
                try {
                    json = TrainingLogJsonSerializer.writeToJson(getTrainingLog());
                } catch (JSONException e) {
                    json = e.getMessage();
                }

                byte[] jsonBytes = new byte[0];
                try {
                    jsonBytes = json.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // UTF-8 IS supported. Yes, I'm sure!
                }
                return new ByteArrayInputStream(jsonBytes);

            }
        }, "trainingstagebuch.json");
    }
    
}
