package com.mwozniak.capser_v2.models.database;

import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.database.game.multiple.Team;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "team_with_stats")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamWithStats {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @Setter
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> playerList;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_doubles_stats", referencedColumnName = "id", nullable = false)
    private UserStats doublesStats;
}
