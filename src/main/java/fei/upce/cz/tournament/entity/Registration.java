package fei.upce.cz.tournament.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entita reprezentující registraci hráče do turnaje.
 * Spojuje entitu User a Tournament — jeden hráč může být
 * registrován ve více turnajích, ale v každém pouze jednou.
 */
@Entity
@Table(name = "registrations")
@Data
@NoArgsConstructor
public class Registration {

    /** Unikátní identifikátor registrace */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Hráč který se registroval */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Turnaj do kterého se hráč registroval */
    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    /** Datum a čas kdy byla registrace provedena */
    @Column(nullable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();
}