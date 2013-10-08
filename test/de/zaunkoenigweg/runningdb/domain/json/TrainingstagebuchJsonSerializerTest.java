package de.zaunkoenigweg.runningdb.domain.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.zaunkoenigweg.runningdb.domain.RecordDistance;
import de.zaunkoenigweg.runningdb.domain.Shoe;
import de.zaunkoenigweg.runningdb.domain.TrainingLog;

public class TrainingstagebuchJsonSerializerTest {
    
    @Test
    public void testWriteToJson() throws Exception {
        
        // to JSON
        TrainingLog trainingstagebuch = new TrainingLog();
        RecordDistance bestzeitStrecke;
        Shoe schuh;

        bestzeitStrecke = new RecordDistance();
        bestzeitStrecke.setStrecke(10000);
        bestzeitStrecke.setBezeichnung("");
        trainingstagebuch.getBestzeitStrecken().add(bestzeitStrecke);
        bestzeitStrecke = new RecordDistance();
        bestzeitStrecke.setStrecke(21100);
        bestzeitStrecke.setBezeichnung("Halbmarathon");
        trainingstagebuch.getBestzeitStrecken().add(bestzeitStrecke);
        bestzeitStrecke = new RecordDistance();
        bestzeitStrecke.setStrecke(42195);
        bestzeitStrecke.setBezeichnung("Marathon");
        trainingstagebuch.getBestzeitStrecken().add(bestzeitStrecke);

        schuh = new Shoe();
        schuh.setId(1);
        schuh.setBrand("Asics");
        schuh.setModel("TN420");
        schuh.setDateOfPurchase("1995");
        schuh.setActive(false);
        trainingstagebuch.addSchuh(schuh);

        schuh = new Shoe();
        schuh.setId(2);
        schuh.setBrand("Adidas");
        schuh.setModel("Response Control");
        schuh.setDateOfPurchase("2003");
        schuh.setActive(true);
        trainingstagebuch.addSchuh(schuh);
        
        schuh = new Shoe();
        schuh.setId(3);
        schuh.setBrand("Asics");
        schuh.setModel("Gel Kayano");
        schuh.setDateOfPurchase("2006");
        schuh.setActive(true);
        trainingstagebuch.addSchuh(schuh);
        
        String json = TrainingLogJsonSerializer.writeToJson(trainingstagebuch);
        System.out.println(json);
        assertNotNull(json);
        
        // from JSON
        trainingstagebuch = TrainingLogJsonSerializer.readFromJson(json);
        assertNotNull(trainingstagebuch);
        assertEquals(3, trainingstagebuch.getBestzeitStrecken().size());
        assertEquals(3, trainingstagebuch.getSchuhe().size());
    }

}
