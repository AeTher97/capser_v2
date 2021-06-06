package com.mwozniak.capser_v2.models.database.game.multiple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.models.database.Competitor;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "team")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team implements Competitor {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private UUID id;

    @Setter
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> playerList;
}
