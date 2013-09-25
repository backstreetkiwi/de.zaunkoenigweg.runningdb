package de.zaunkoenigweg.runningdb.ui;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.util.converter.Converter;

public class ZeitConverter implements Converter<String, Integer> {

    private static final String REGEX = "^((\\d):)?(\\d{1,2}):(\\d{1,2})$";
    private static final long serialVersionUID = 4370150854933461623L;

    @Override
    public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws Converter.ConversionException {
        if(StringUtils.isBlank(value)) {
            return null;
        }
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(value); 
        if (!matcher.matches()) {
            throw new ConversionException(String.format("'%s' konnte nicht als Zeitwert erkannt werden.", value));
        }
        
        int zeit = 0;
        zeit += Integer.valueOf(matcher.group(4));
        zeit += Integer.valueOf(matcher.group(3)) * 60;
        if(matcher.group(2)!=null) {
            zeit += Integer.valueOf(matcher.group(2)) * 3600;
        }
        
        if(zeit==0) {
            return null;
        }
        
        return Integer.valueOf(zeit);
    }

    @Override
    public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
        if(value==null) {
            return "";
        }
        int zeit = value.intValue();
        int sekunden = zeit % 60;
        int minuten = (zeit / 60) % 60;
        int stunden = (zeit / 3600);
        if(stunden==0) {
            return String.format("%d:%02d", minuten, sekunden);
        } else {
            return String.format("%d:%02d:%02d", stunden, minuten, sekunden);
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
