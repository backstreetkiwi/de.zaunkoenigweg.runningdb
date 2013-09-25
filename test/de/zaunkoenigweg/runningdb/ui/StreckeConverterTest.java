package de.zaunkoenigweg.runningdb.ui;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class StreckeConverterTest {
    
    private StreckeConverter sut;
    
    @Before
    public void setup() {
        this.sut = new StreckeConverter();
    }

    @Test
    public void testConvertToModel() {
        
        assertEquals(100, this.sut.convertToModel("100", Integer.class, Locale.getDefault()).intValue());
        assertEquals(999, this.sut.convertToModel("999", Integer.class, Locale.getDefault()).intValue());
        assertEquals(1000, this.sut.convertToModel("1000", Integer.class, Locale.getDefault()).intValue());
        assertEquals(1000, this.sut.convertToModel("1.000", Integer.class, Locale.getDefault()).intValue());
        assertEquals(1001, this.sut.convertToModel("1.001", Integer.class, Locale.getDefault()).intValue());
        assertEquals(1001, this.sut.convertToModel("1001", Integer.class, Locale.getDefault()).intValue());
        assertEquals(10000, this.sut.convertToModel("10000", Integer.class, Locale.getDefault()).intValue());
        assertEquals(10000, this.sut.convertToModel("10.000", Integer.class, Locale.getDefault()).intValue());
        assertEquals(42195, this.sut.convertToModel("42.195", Integer.class, Locale.getDefault()).intValue());
    }

    @Test
    public void testConvertToPresentation() {
        assertEquals("100", this.sut.convertToPresentation(100, String.class, Locale.getDefault()));
        assertEquals("999", this.sut.convertToPresentation(999, String.class, Locale.getDefault()));
        assertEquals("1.000", this.sut.convertToPresentation(1000, String.class, Locale.getDefault()));
        assertEquals("1.234", this.sut.convertToPresentation(1234, String.class, Locale.getDefault()));
        assertEquals("10.000", this.sut.convertToPresentation(10000, String.class, Locale.getDefault()));
        assertEquals("42.195", this.sut.convertToPresentation(42195, String.class, Locale.getDefault()));
    }

}
