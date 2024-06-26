package com.word_training.api.config.converters;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

public class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {
    @Override
    public BigDecimal convert(Decimal128 source) {
        return source.bigDecimalValue();
    }
}
