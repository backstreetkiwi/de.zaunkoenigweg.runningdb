package de.zaunkoenigweg.runningdb.ui;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class ZeitConverterTest {
    
    private ZeitConverter sut;
    
    @Before
    public void setup() {
        this.sut = new ZeitConverter();
    }

    @Test
    public void testConvertToModel() {
        
        assertEquals(null, this.sut.convertToModel("0:00", Integer.class, Locale.getDefault()));
        assertEquals(null, this.sut.convertToModel("0:0", Integer.class, Locale.getDefault()));
        assertEquals(null, this.sut.convertToModel("0:00:00", Integer.class, Locale.getDefault()));
        assertEquals(null, this.sut.convertToModel("0:0:0", Integer.class, Locale.getDefault()));
        
        assertEquals(1, this.sut.convertToModel("0:1", Integer.class, Locale.getDefault()).intValue());
        assertEquals(1, this.sut.convertToModel("0:01", Integer.class, Locale.getDefault()).intValue());
        assertEquals(1, this.sut.convertToModel("0:0:1", Integer.class, Locale.getDefault()).intValue());
        assertEquals(1, this.sut.convertToModel("0:0:01", Integer.class, Locale.getDefault()).intValue());
        assertEquals(1, this.sut.convertToModel("0:00:01", Integer.class, Locale.getDefault()).intValue());
        assertEquals(17, this.sut.convertToModel("0:17", Integer.class, Locale.getDefault()).intValue());
        assertEquals(17, this.sut.convertToModel("0:0:17", Integer.class, Locale.getDefault()).intValue());
        assertEquals(17, this.sut.convertToModel("0:00:17", Integer.class, Locale.getDefault()).intValue());
        assertEquals(59, this.sut.convertToModel("0:59", Integer.class, Locale.getDefault()).intValue());
        assertEquals(60, this.sut.convertToModel("1:00", Integer.class, Locale.getDefault()).intValue());
        assertEquals(60, this.sut.convertToModel("0:1:00", Integer.class, Locale.getDefault()).intValue());
        assertEquals(2354, this.sut.convertToModel("39:14", Integer.class, Locale.getDefault()).intValue());
        assertEquals(2354, this.sut.convertToModel("0:39:14", Integer.class, Locale.getDefault()).intValue());
        assertEquals(3599, this.sut.convertToModel("59:59", Integer.class, Locale.getDefault()).intValue());
        assertEquals(3599, this.sut.convertToModel("0:59:59", Integer.class, Locale.getDefault()).intValue());
        assertEquals(3600, this.sut.convertToModel("1:00:00", Integer.class, Locale.getDefault()).intValue());
        assertEquals(3600, this.sut.convertToModel("1:0:00", Integer.class, Locale.getDefault()).intValue());
        assertEquals(3600, this.sut.convertToModel("1:00:0", Integer.class, Locale.getDefault()).intValue());
        assertEquals(3600, this.sut.convertToModel("1:0:0", Integer.class, Locale.getDefault()).intValue());
        assertEquals(12678, this.sut.convertToModel("3:31:18", Integer.class, Locale.getDefault()).intValue());
    }

    @Test
    public void testConvertToPresentation() {
        assertEquals("0:01", this.sut.convertToPresentation(1, String.class, Locale.getDefault()));
        assertEquals("0:17", this.sut.convertToPresentation(17, String.class, Locale.getDefault()));
        assertEquals("0:59", this.sut.convertToPresentation(59, String.class, Locale.getDefault()));
        assertEquals("1:00", this.sut.convertToPresentation(60, String.class, Locale.getDefault()));
        assertEquals("39:14", this.sut.convertToPresentation(2354, String.class, Locale.getDefault()));
        assertEquals("59:59", this.sut.convertToPresentation(3599, String.class, Locale.getDefault()));
        assertEquals("1:00:00", this.sut.convertToPresentation(3600, String.class, Locale.getDefault()));
        assertEquals("3:31:18", this.sut.convertToPresentation(12678, String.class, Locale.getDefault()));
    }

}
