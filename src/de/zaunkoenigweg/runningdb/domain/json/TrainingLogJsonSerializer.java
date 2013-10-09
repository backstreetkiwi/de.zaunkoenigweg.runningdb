package de.zaunkoenigweg.runningdb.domain.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONWriter;

import de.zaunkoenigweg.runningdb.domain.RecordDistance;
import de.zaunkoenigweg.runningdb.domain.RecordInfo;
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
        json.key("shoes").array();
        for (Shoe schuh : trainingstagebuch.getShoes()) {
            writeSchuh(schuh, json);
        }
        json.endArray();
        json.key("recordDistances").array();
        for (RecordDistance strecke : trainingstagebuch.getRecordDistances()) {
            writeBestzeitenStrecke(strecke, json);
        }
        json.endArray();
        json.key("trainings").array();
        List<Training> trainings = trainingstagebuch.getTrainings();
        Collections.sort(trainings, new Comparator<Training>() {
            @Override
            public int compare(Training training1, Training training2) {
                return Long.valueOf(training1.getDate().getTime()).compareTo(Long.valueOf(training2.getDate().getTime()));
            }
        });
        for (Training training : trainings) {
            writeTrainingseinheit(training, json);
        }
        json.endArray();
        json.endObject();
        return new JSONObject(json.toString()).toString(2);
    }
    
    public static TrainingLog readFromJson(String jsonString) throws JSONException, IOException {
        TrainingLog trainingstagebuch = new TrainingLog();
        JSONObject json = new JSONObject(jsonString);
        JSONArray bestzeitenstrecken = json.getJSONArray("recordDistances");
        for (int i = 0; i < bestzeitenstrecken.length(); i++) {
            trainingstagebuch.addRecordDistance(readBestzeitenStrecke(bestzeitenstrecken.getJSONObject(i)));
        }
        JSONArray schuhe = json.getJSONArray("shoes");
        for (int i = 0; i < schuhe.length(); i++) {
            trainingstagebuch.addShoe(readSchuh(schuhe.getJSONObject(i)));
        }
        JSONArray trainingseinheiten = json.getJSONArray("trainings");
        for (int i = 0; i < trainingseinheiten.length(); i++) {
            trainingstagebuch.addTraining(readTrainingseinheit(trainingseinheiten.getJSONObject(i)));
        }
        return trainingstagebuch;
    }
    
    private static void writeBestzeitenStrecke(RecordDistance strecke, JSONWriter json) throws JSONException {
        json.object();
        json.key("distance").value(strecke.getDistance());
        json.key("label").value(strecke.getLabel());
        json.endObject();
    }

    private static RecordDistance readBestzeitenStrecke(JSONObject json) throws JSONException {
        RecordDistance strecke = new RecordDistance();
        strecke.setDistance(json.getInt("distance"));
        strecke.setLabel(json.getString("label"));
        return strecke;
    }
    
    private static void writeSchuh(Shoe schuh, JSONWriter json) throws JSONException {
        json.object();
        json.key("id").value(schuh.getId());
        json.key("brand").value(schuh.getBrand());
        json.key("model").value(schuh.getModel());
        json.key("dateOfPurchase").value(schuh.getDateOfPurchase());
        json.key("price").value(schuh.getPrice());
        json.key("comments").value(schuh.getComments());
        json.key("active").value(schuh.isActive());
        String imageBase64 = "";
        if(schuh.getImage()!=null && schuh.getImage().length>0) {
            imageBase64 = Base64.encodeBase64String(schuh.getImage());
        }
        json.key("image").value(imageBase64);
        json.endObject();
    }
    
    private static Shoe readSchuh(JSONObject json) throws JSONException, IOException {
        Shoe schuh = new Shoe();
        schuh.setId(json.getInt("id"));
        schuh.setBrand(json.getString("brand"));
        schuh.setModel(json.getString("model"));
        schuh.setDateOfPurchase(json.getString("dateOfPurchase"));
        schuh.setPrice(json.getString("price"));
        schuh.setComments(json.getString("comments"));
        schuh.setActive(json.getBoolean("active"));
        String imageBase64 = json.getString("image");
        if(StringUtils.isNotBlank(imageBase64)) {
            schuh.setImage(Base64.decodeBase64(imageBase64));
        }
        return schuh;
    }

    private static void writeTrainingseinheit(Training trainingseinheit, JSONWriter json) throws JSONException {
        json.object();
        json.key("date").value(DATE_YYYY_MM_DD.format(trainingseinheit.getDate()));
        json.key("location").value(trainingseinheit.getLocation());
        json.key("comments").value(trainingseinheit.getComments());
        json.key("shoe").value(trainingseinheit.getShoe());
        json.key("runs").array();
        for (Run lauf : trainingseinheit.getRuns()) {
            writeLauf(lauf, json);
        }
        json.endArray();
        json.endObject();
    }
    
    private static Training readTrainingseinheit(JSONObject json) throws JSONException {
        Training trainingseinheit = new Training();
        String datum = json.getString("date");
        try {
            trainingseinheit.setDate(DATE_YYYY_MM_DD.parse(datum));
        } catch (ParseException e) {
            throw new JSONException(String.format("Konnte kein Datum parsen: '%s'", datum));
        }
        trainingseinheit.setLocation(json.getString("location"));
        trainingseinheit.setComments(json.getString("comments"));
        trainingseinheit.setShoe(json.getInt("shoe"));
        JSONArray laeufe = json.getJSONArray("runs");
        for (int i = 0; i < laeufe.length(); i++) {
            trainingseinheit.getRuns().add(readLauf(laeufe.getJSONObject(i)));
        }
        return trainingseinheit;
    }
    
    private static void writeLauf(Run lauf, JSONWriter json) throws JSONException {
        json.object();
        json.key("distance").value(lauf.getDistance());
        json.key("time").value(lauf.getTime());
        json.endObject();
    }
    
    private static Run readLauf(JSONObject json) throws JSONException {
        Run lauf = new Run();
        lauf.setDistance(json.getInt("distance"));
        lauf.setTime(json.getInt("time"));
        return lauf;
    }
    
    
}
