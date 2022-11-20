package com.mwozniak.capser_v2.models.database;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Table(name = "failed_emails")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedEmail {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")

    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String content;
    private String recipient;
    private String subject;
    private Date date;
}
