package de.zaunkoenigweg.runningdb.domain.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONWriter;

import sun.misc.BASE64Decoder;
import de.zaunkoenigweg.runningdb.domain.RecordDistance;
import de.zaunkoenigweg.runningdb.domain.Run;
import de.zaunkoenigweg.runningdb.domain.Shoe;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.TrainingLog;

/**
 * Speichert/Liest das Trainingstagebuch nach/aus JSON. 
 * 
 * @author Nikolaus Winter
 *
 */
public class TrainingLogJsonSerializer {
    
    /**
     * Formatierung/Parsen von Datumswerten.
     */
    private static final DateFormat DATE_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static String writeToJson(TrainingLog trainingstagebuch) throws JSONException {
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("schuhe").array();
        for (Shoe schuh : trainingstagebuch.getShoes()) {
            writeSchuh(schuh, json);
        }
        json.endArray();
        json.key("bestzeitenstrecken").array();
        for (RecordDistance strecke : trainingstagebuch.getRecordDistances()) {
            writeBestzeitenStrecke(strecke, json);
        }
        json.endArray();
        json.key("trainingseinheiten").array();
        for (Training training : trainingstagebuch.getTrainings()) {
            writeTrainingseinheit(training, json);
        }
        json.endArray();
        json.endObject();
        return new JSONObject(json.toString()).toString(2);
    }
    
    public static TrainingLog readFromJson(String jsonString) throws JSONException, IOException {
        TrainingLog trainingstagebuch = new TrainingLog();
        JSONObject json = new JSONObject(jsonString);
        JSONArray bestzeitenstrecken = json.getJSONArray("bestzeitenstrecken");
        for (int i = 0; i < bestzeitenstrecken.length(); i++) {
            trainingstagebuch.addRecordDistance(readBestzeitenStrecke(bestzeitenstrecken.getJSONObject(i)));
        }
        JSONArray schuhe = json.getJSONArray("schuhe");
        for (int i = 0; i < schuhe.length(); i++) {
            trainingstagebuch.addShoe(readSchuh(schuhe.getJSONObject(i)));
        }
        JSONArray trainingseinheiten = json.getJSONArray("trainingseinheiten");
        for (int i = 0; i < trainingseinheiten.length(); i++) {
            trainingstagebuch.addTraining(readTrainingseinheit(trainingseinheiten.getJSONObject(i)));
        }
        return trainingstagebuch;
    }
    
    private static void writeBestzeitenStrecke(RecordDistance strecke, JSONWriter json) throws JSONException {
        json.object();
        json.key("strecke").value(strecke.getDistance());
        json.key("bezeichnung").value(strecke.getLabel());
        json.endObject();
    }

    private static RecordDistance readBestzeitenStrecke(JSONObject json) throws JSONException {
        RecordDistance strecke = new RecordDistance();
        strecke.setDistance(json.getInt("strecke"));
        strecke.setLabel(json.getString("bezeichnung"));
        return strecke;
    }
    
    private static void writeSchuh(Shoe schuh, JSONWriter json) throws JSONException {
        json.object();
        json.key("id").value(schuh.getId());
        json.key("hersteller").value(schuh.getBrand());
        json.key("modell").value(schuh.getModel());
        json.key("kaufdatum").value(schuh.getDateOfPurchase());
        json.key("preis").value(schuh.getPrice());
        json.key("bemerkungen").value(schuh.getComments());
        json.key("aktiv").value(schuh.isActive());
        json.endObject();
    }
    
    private static Shoe readSchuh(JSONObject json) throws JSONException, IOException {
        Shoe schuh = new Shoe();
        schuh.setId(json.getInt("id"));
        schuh.setBrand(json.getString("hersteller"));
        schuh.setModel(json.getString("modell"));
        schuh.setDateOfPurchase(json.getString("kaufdatum"));
        schuh.setPrice(json.getString("preis"));
        schuh.setComments(json.getString("bemerkungen"));
        schuh.setActive(json.getBoolean("aktiv"));
        String imageBase64 = json.getString("image");
        BASE64Decoder decoder = new BASE64Decoder();
        if(StringUtils.isNotBlank(imageBase64)) {
            schuh.setImage(decoder.decodeBuffer(imageBase64));
        }
        return schuh;
    }

    private static void writeTrainingseinheit(Training trainingseinheit, JSONWriter json) throws JSONException {
        json.object();
        json.key("datum").value(DATE_YYYY_MM_DD.format(trainingseinheit.getDate()));
        json.key("ort").value(trainingseinheit.getLocation());
        json.key("bemerkungen").value(trainingseinheit.getComments());
        json.key("schuh").value(trainingseinheit.getShoe());
        json.key("laeufe").array();
        for (Run lauf : trainingseinheit.getRuns()) {
            writeLauf(lauf, json);
        }
        json.endArray();
        json.endObject();
    }
    
    private static Training readTrainingseinheit(JSONObject json) throws JSONException {
        Training trainingseinheit = new Training();
        String datum = json.getString("datum");
        try {
            trainingseinheit.setDate(DATE_YYYY_MM_DD.parse(datum));
        } catch (ParseException e) {
            throw new JSONException(String.format("Konnte kein Datum parsen: '%s'", datum));
        }
        trainingseinheit.setLocation(json.getString("ort"));
        trainingseinheit.setComments(json.getString("bemerkungen"));
        trainingseinheit.setShoe(json.getInt("schuh"));
        JSONArray laeufe = json.getJSONArray("laeufe");
        for (int i = 0; i < laeufe.length(); i++) {
            trainingseinheit.getRuns().add(readLauf(laeufe.getJSONObject(i)));
        }
        return trainingseinheit;
    }
    
    private static void writeLauf(Run lauf, JSONWriter json) throws JSONException {
        json.object();
        json.key("strecke").value(lauf.getDistance());
        json.key("zeit").value(lauf.getTime());
        json.endObject();
    }
    
    private static Run readLauf(JSONObject json) throws JSONException {
        Run lauf = new Run();
        lauf.setDistance(json.getInt("strecke"));
        lauf.setTime(json.getInt("zeit"));
        return lauf;
    }
    
    
}
