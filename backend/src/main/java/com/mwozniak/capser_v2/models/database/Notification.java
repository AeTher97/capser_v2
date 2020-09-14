package com.mwozniak.capser_v2.models.database;

import com.mwozniak.capser_v2.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification implements Comparable<Notification> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String text;

    private UUID userId;

    private boolean seen;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Override
    public int compareTo(Notification o) {
        return 0;
    }


    public static class Comparators {

        public static final Comparator<Notification> DATE = Comparator.comparing(Notification::getDate);
    }
}
