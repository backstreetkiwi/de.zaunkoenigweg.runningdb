package de.zaunkoenigweg.runningdb.domain.json;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONWriter;

import de.zaunkoenigweg.runningdb.domain.BestzeitStrecke;
import de.zaunkoenigweg.runningdb.domain.Lauf;
import de.zaunkoenigweg.runningdb.domain.Schuh;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

/**
 * Speichert/Liest das Trainingstagebuch nach/aus JSON. 
 * 
 * @author Nikolaus Winter
 *
 */
public class TrainingstagebuchJsonSerializer {
    
    /**
     * Formatierung/Parsen von Datumswerten.
     */
    private static final DateFormat DATE_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static String writeToJson(Trainingstagebuch trainingstagebuch) throws JSONException {
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("schuhe").array();
        for (Schuh schuh : trainingstagebuch.getSchuhe()) {
            writeSchuh(schuh, json);
        }
        json.endArray();
        json.key("bestzeitenstrecken").array();
        for (BestzeitStrecke strecke : trainingstagebuch.getBestzeitStrecken()) {
            writeBestzeitenStrecke(strecke, json);
        }
        json.endArray();
        json.key("trainingseinheiten").array();
        for (Training training : trainingstagebuch.getTrainingseinheiten()) {
            writeTrainingseinheit(training, json);
        }
        json.endArray();
        json.endObject();
        return new JSONObject(json.toString()).toString(2);
    }
    
    public static Trainingstagebuch readFromJson(String jsonString) throws JSONException {
        Trainingstagebuch trainingstagebuch = new Trainingstagebuch();
        JSONObject json = new JSONObject(jsonString);
        JSONArray bestzeitenstrecken = json.getJSONArray("bestzeitenstrecken");
        for (int i = 0; i < bestzeitenstrecken.length(); i++) {
            trainingstagebuch.addBestzeitStrecke(readBestzeitenStrecke(bestzeitenstrecken.getJSONObject(i)));
        }
        JSONArray schuhe = json.getJSONArray("schuhe");
        for (int i = 0; i < schuhe.length(); i++) {
            trainingstagebuch.addSchuh(readSchuh(schuhe.getJSONObject(i)));
        }
        JSONArray trainingseinheiten = json.getJSONArray("trainingseinheiten");
        for (int i = 0; i < trainingseinheiten.length(); i++) {
            trainingstagebuch.addTraining(readTrainingseinheit(trainingseinheiten.getJSONObject(i)));
        }
        return trainingstagebuch;
    }
    
    private static void writeBestzeitenStrecke(BestzeitStrecke strecke, JSONWriter json) throws JSONException {
        json.object();
        json.key("strecke").value(strecke.getStrecke());
        json.key("bezeichnung").value(strecke.getBezeichnung());
        json.endObject();
    }

    private static BestzeitStrecke readBestzeitenStrecke(JSONObject json) throws JSONException {
        BestzeitStrecke strecke = new BestzeitStrecke();
        strecke.setStrecke(json.getInt("strecke"));
        strecke.setBezeichnung(json.getString("bezeichnung"));
        return strecke;
    }
    
    private static void writeSchuh(Schuh schuh, JSONWriter json) throws JSONException {
        json.object();
        json.key("id").value(schuh.getId());
        json.key("hersteller").value(schuh.getHersteller());
        json.key("modell").value(schuh.getModell());
        json.key("kaufdatum").value(schuh.getKaufdatum());
        json.key("preis").value(schuh.getPreis());
        json.key("bemerkungen").value(schuh.getBemerkungen());
        json.key("aktiv").value(schuh.isAktiv());
        json.endObject();
    }
    
    private static Schuh readSchuh(JSONObject json) throws JSONException {
        Schuh schuh = new Schuh();
        schuh.setId(json.getInt("id"));
        schuh.setHersteller(json.getString("hersteller"));
        schuh.setModell(json.getString("modell"));
        schuh.setKaufdatum(json.getString("kaufdatum"));
        schuh.setPreis(json.getString("preis"));
        schuh.setBemerkungen(json.getString("bemerkungen"));
        schuh.setAktiv(json.getBoolean("aktiv"));
        return schuh;
    }

    private static void writeTrainingseinheit(Training trainingseinheit, JSONWriter json) throws JSONException {
        json.object();
        json.key("datum").value(DATE_YYYY_MM_DD.format(trainingseinheit.getDatum()));
        json.key("ort").value(trainingseinheit.getOrt());
        json.key("bemerkungen").value(trainingseinheit.getBemerkungen());
        json.key("schuh").value(trainingseinheit.getSchuh());
        json.key("laeufe").array();
        for (Lauf lauf : trainingseinheit.getLaeufe()) {
            writeLauf(lauf, json);
        }
        json.endArray();
        json.endObject();
    }
    
    private static Training readTrainingseinheit(JSONObject json) throws JSONException {
        Training trainingseinheit = new Training();
        String datum = json.getString("datum");
        try {
            trainingseinheit.setDatum(DATE_YYYY_MM_DD.parse(datum));
        } catch (ParseException e) {
            throw new JSONException(String.format("Konnte kein Datum parsen: '%s'", datum));
        }
        trainingseinheit.setOrt(json.getString("ort"));
        trainingseinheit.setBemerkungen(json.getString("bemerkungen"));
        trainingseinheit.setSchuh(json.getInt("schuh"));
        JSONArray laeufe = json.getJSONArray("laeufe");
        for (int i = 0; i < laeufe.length(); i++) {
            trainingseinheit.getLaeufe().add(readLauf(laeufe.getJSONObject(i)));
        }
        return trainingseinheit;
    }
    
    private static void writeLauf(Lauf lauf, JSONWriter json) throws JSONException {
        json.object();
        json.key("strecke").value(lauf.getStrecke());
        json.key("zeit").value(lauf.getZeit());
        json.endObject();
    }
    
    private static Lauf readLauf(JSONObject json) throws JSONException {
        Lauf lauf = new Lauf();
        lauf.setStrecke(json.getInt("strecke"));
        lauf.setZeit(json.getInt("zeit"));
        return lauf;
    }
    
    
}
