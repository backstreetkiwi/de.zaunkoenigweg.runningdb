package de.zaunkoenigweg.runningdb.ui;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.util.converter.Converter;

/**
 * Converts running distance.
 *
 * @author Nikolaus Winter
 */
public class DistanceConverter implements Converter<String, Integer> {

    private static final long serialVersionUID = -5944663904981260796L;
    private static final String REGEX = "^(\\d{1,2})?\\.?(\\d{3})$";

    @Override
    public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws Converter.ConversionException {
        if(StringUtils.isBlank(value)) {
            return null;
        }
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(value); 
        if (!matcher.matches()) {
            throw new ConversionException(String.format("'%s' konnte nicht als Strecke erkannt werden.", value));
        }
        
        int strecke = 0;
        strecke += Integer.valueOf(matcher.group(2));
        if(matcher.group(1)!=null) {
            strecke += Integer.valueOf(matcher.group(1)) * 1000;
        }
        
        if(strecke==0) {
            return null;
        }
        
        return Integer.valueOf(strecke);
    }

    @Override
    public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
        if(value==null) {
            return "";
        }
        int meter = value.intValue() % 1000;
        int kilometer = (value.intValue() / 1000);
        if(kilometer==0) {
            return String.format("%03d", meter);
        } else {
            return String.format("%d.%03d", kilometer, meter);
        }
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}
