package de.vorb.npmstat.persistence.converters;

import org.jooq.Converter;

import java.sql.Date;
import java.time.LocalDate;

public class LocalDateConverter implements Converter<Date, LocalDate> {

    @Override
    public LocalDate from(Date databaseObject) {
        return databaseObject == null ? null : databaseObject.toLocalDate();
    }

    @Override
    public Date to(LocalDate userObject) {
        return userObject == null ? null : Date.valueOf(userObject);
    }

    @Override
    public Class<Date> fromType() {
        return Date.class;
    }

    @Override
    public Class<LocalDate> toType() {
        return LocalDate.class;
    }

}
