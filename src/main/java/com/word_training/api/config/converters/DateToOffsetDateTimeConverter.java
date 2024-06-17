package com.word_training.api.config.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {
    @Override
    public OffsetDateTime convert(Date date) {
       // return OffsetDateTime.ofInstant(source.toInstant(), ZoneId.of("UTC"));
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }
}
