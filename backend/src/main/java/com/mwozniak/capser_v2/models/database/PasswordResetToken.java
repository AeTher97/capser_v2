package com.mwozniak.capser_v2.models.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
public class PasswordResetToken {

    private static final int EXPIRATION = 24 * 60 * 60 * 1000;
    @Getter
    private final String token;
    @Getter
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private final User user;
    @Getter
    private final Date expiryDate;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    public PasswordResetToken(String token, User user) {
        this.user = user;
        this.token = token;
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION);
    }

    public PasswordResetToken() {
        token = null;
        user = null;
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION);
    }

    public boolean isExpired() {
        return expiryDate.before(new Date());
    }

}
