package com.mwozniak.capser_v2.models.database;

import com.vladmihalcea.hibernate.type.array.DoubleArrayType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;


@Data
@Entity(name = "TimeSeries")
@TypeDef(
        name = "list-array",
        typeClass = DoubleArrayType.class
)
public class TimeSeries {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Type(type = "list-array")
    @Column(name = "data_series",
            columnDefinition = "real[]")
    private float[] data;

    private LocalDate lastLogged;

    private int lastElement;

    public void logToday(float points) {
        float NULL_VALUE = -100000;

        int start = getLastElement();
        int end;
        if (getLastLogged() == null) {
            end = start;
        } else {
            end = Math.toIntExact(start + DAYS.between(getLastLogged(), LocalDate.now())) % 365;
        }

        if (end - start != 1) {
            if (end > start) {
                for (int i = start; i < end; i++) {

                    getData()[i] = NULL_VALUE;
                }
            } else if (start > end) {
                for (int i = start; i < 365; i++) {
                    getData()[i] = NULL_VALUE;
                }
                for (int i = 0; i < end; i++) {
                    getData()[i] = NULL_VALUE;
                }
            }
        }

        setLastElement(end);
        setLastLogged(LocalDate.now());
        getData()[end] = points;
    }

}
