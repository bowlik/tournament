package fei.upce.cz.tournament.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entita reprezentující herní turnaj.
 * Turnaj má maximální kapacitu hráčů a prochází různými stavy
 * od otevření po dokončení.
 */
@Entity
@Table(name = "tournaments")
@Data
@NoArgsConstructor
public class Tournament {

    /** Unikátní identifikátor turnaje */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Název turnaje */
    @Column(nullable = false)
    private String name;

    /** Název hry pro kterou je turnaj pořádán */
    private String game;

    /** Volitelný popis turnaje */
    private String description;

    /** Datum a čas začátku turnaje */
    @Column(nullable = false)
    private LocalDateTime startDate;

    /** Maximální počet hráčů kteří se mohou registrovat */
    @Column(nullable = false)
    private int maxPlayers;

    /** Aktuální stav turnaje */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.OPEN;

    /**
     * Výčet možných stavů turnaje.
     * OPEN — otevřen pro registrace,
     * IN_PROGRESS — probíhá,
     * FINISHED — ukončen,
     * CANCELLED — zrušen.
     */
    public enum Status {
        OPEN, IN_PROGRESS, FINISHED, CANCELLED
    }
}