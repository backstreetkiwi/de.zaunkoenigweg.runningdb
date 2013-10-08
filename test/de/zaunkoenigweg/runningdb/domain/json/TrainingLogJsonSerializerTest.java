package de.zaunkoenigweg.runningdb.domain.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.zaunkoenigweg.runningdb.domain.RecordDistance;
import de.zaunkoenigweg.runningdb.domain.Shoe;
import de.zaunkoenigweg.runningdb.domain.TrainingLog;

public class TrainingLogJsonSerializerTest {
    
    @Test
    public void testWriteToJson() throws Exception {
        
        // to JSON
        TrainingLog trainingLog = new TrainingLog();
        RecordDistance bestzeitStrecke;
        Shoe schuh;

        bestzeitStrecke = new RecordDistance();
        bestzeitStrecke.setDistance(10000);
        bestzeitStrecke.setLabel("");
        trainingLog.getRecordDistances().add(bestzeitStrecke);
        bestzeitStrecke = new RecordDistance();
        bestzeitStrecke.setDistance(21100);
        bestzeitStrecke.setLabel("Halbmarathon");
        trainingLog.getRecordDistances().add(bestzeitStrecke);
        bestzeitStrecke = new RecordDistance();
        bestzeitStrecke.setDistance(42195);
        bestzeitStrecke.setLabel("Marathon");
        trainingLog.getRecordDistances().add(bestzeitStrecke);

        schuh = new Shoe();
        schuh.setId(1);
        schuh.setBrand("Asics");
        schuh.setModel("TN420");
        schuh.setDateOfPurchase("1995");
        schuh.setActive(false);
        trainingLog.addShoe(schuh);

        schuh = new Shoe();
        schuh.setId(2);
        schuh.setBrand("Adidas");
        schuh.setModel("Response Control");
        schuh.setDateOfPurchase("2003");
        schuh.setActive(true);
        trainingLog.addShoe(schuh);
        
        schuh = new Shoe();
        schuh.setId(3);
        schuh.setBrand("Asics");
        schuh.setModel("Gel Kayano");
        schuh.setDateOfPurchase("2006");
        schuh.setActive(true);
        trainingLog.addShoe(schuh);
        
        String json = TrainingLogJsonSerializer.writeToJson(trainingLog);
        System.out.println(json);
        assertNotNull(json);
        
        // from JSON
        trainingLog = TrainingLogJsonSerializer.readFromJson(json);
        assertNotNull(trainingLog);
        assertEquals(3, trainingLog.getRecordDistances().size());
        assertEquals(3, trainingLog.getShoes().size());
    }

}
