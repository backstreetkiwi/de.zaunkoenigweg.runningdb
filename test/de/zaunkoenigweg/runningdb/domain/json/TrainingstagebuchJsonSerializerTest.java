package de.zaunkoenigweg.runningdb.domain.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.zaunkoenigweg.runningdb.domain.BestzeitStrecke;
import de.zaunkoenigweg.runningdb.domain.Schuh;
import de.zaunkoenigweg.runningdb.domain.Trainingstagebuch;

public class TrainingstagebuchJsonSerializerTest {
    
    @Test
    public void testWriteToJson() throws Exception {
        
        // to JSON
        Trainingstagebuch trainingstagebuch = new Trainingstagebuch();
        BestzeitStrecke bestzeitStrecke;
        Schuh schuh;

        bestzeitStrecke = new BestzeitStrecke();
        bestzeitStrecke.setStrecke(10000);
        bestzeitStrecke.setBezeichnung("");
        trainingstagebuch.getBestzeitStrecken().add(bestzeitStrecke);
        bestzeitStrecke = new BestzeitStrecke();
        bestzeitStrecke.setStrecke(21100);
        bestzeitStrecke.setBezeichnung("Halbmarathon");
        trainingstagebuch.getBestzeitStrecken().add(bestzeitStrecke);
        bestzeitStrecke = new BestzeitStrecke();
        bestzeitStrecke.setStrecke(42195);
        bestzeitStrecke.setBezeichnung("Marathon");
        trainingstagebuch.getBestzeitStrecken().add(bestzeitStrecke);

        schuh = new Schuh();
        schuh.setId(1);
        schuh.setHersteller("Asics");
        schuh.setModell("TN420");
        schuh.setKaufdatum("1995");
        schuh.setAktiv(false);
        trainingstagebuch.addSchuh(schuh);

        schuh = new Schuh();
        schuh.setId(2);
        schuh.setHersteller("Adidas");
        schuh.setModell("Response Control");
        schuh.setKaufdatum("2003");
        schuh.setAktiv(true);
        trainingstagebuch.addSchuh(schuh);
        
        schuh = new Schuh();
        schuh.setId(3);
        schuh.setHersteller("Asics");
        schuh.setModell("Gel Kayano");
        schuh.setKaufdatum("2006");
        schuh.setAktiv(true);
        trainingstagebuch.addSchuh(schuh);
        
        String json = TrainingstagebuchJsonSerializer.writeToJson(trainingstagebuch);
        System.out.println(json);
        assertNotNull(json);
        
        // from JSON
        trainingstagebuch = TrainingstagebuchJsonSerializer.readFromJson(json);
        assertNotNull(trainingstagebuch);
        assertEquals(3, trainingstagebuch.getBestzeitStrecken().size());
        assertEquals(3, trainingstagebuch.getSchuhe().size());
    }

}
