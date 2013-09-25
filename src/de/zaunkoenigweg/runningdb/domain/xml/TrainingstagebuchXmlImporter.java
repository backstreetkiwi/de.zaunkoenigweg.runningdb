package de.zaunkoenigweg.runningdb.domain.xml;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.zaunkoenigweg.runningdb.domain.Lauf;
import de.zaunkoenigweg.runningdb.domain.Training;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;
import de.zaunkoenigweg.runningdb.domain.json.TrainingstagebuchJsonSerializer;

public class TrainingstagebuchXmlImporter {
    
    private static final DateFormat DATE_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {

        try {
            
            Trainingstagebuch trainingstagebuch = new Trainingstagebuch();

            File stocks = new File("c:/dev/vaadin/bis_2012_Februar.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stocks);
            doc.getDocumentElement().normalize();

            NodeList trainingseinheiten = doc.getElementsByTagName("training");
            
            for (int i = 0; i < trainingseinheiten.getLength(); i++) {
                Node node = trainingseinheiten.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Training training = new Training();
                    trainingstagebuch.addTraining(training);
                    
                    Element element = (Element) node;
                    
                    
                    
                    training.setDatum(DATE_YYYY_MM_DD.parse(getStringValue("datum", element)));
                    training.setOrt(getStringValue("ort", element));
                    training.setBemerkungen(getStringValue("beschreibung", element));
                    training.setSchuh(getIntValue("schuh", element));
                    
                    NodeList laeufe = ((Node)element.getElementsByTagName("laeufe").item(0)).getChildNodes();
                    
                    for (int j = 0; j < laeufe.getLength(); j++) {
                        if("lauf".equals(laeufe.item(j).getNodeName())) {
                            
                            Lauf lauf = new Lauf();
                            lauf.setStrecke(getIntValue("strecke", (Element)laeufe.item(j)));
                            lauf.setZeit(getIntValue("zeit", (Element)laeufe.item(j)));
                            training.getLaeufe().add(lauf);
                        };
                    }
                    
                }
            }
            
            System.out.println(TrainingstagebuchJsonSerializer.writeToJson(trainingstagebuch));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static String getStringValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        if(node==null) {
            return "";
        }
        return node.getNodeValue();
    }

    private static int getIntValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        if(node==null) {
            return 0;
        }
        return Integer.valueOf(node.getNodeValue());
    }
}
