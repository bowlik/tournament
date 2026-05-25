package fei.upce.cz.tournament.repository;

import fei.upce.cz.tournament.entity.Tournament;
import fei.upce.cz.tournament.entity.Tournament.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository rozhraní pro přístup k datům turnajů.
 * Obsahuje standardní CRUD operace a vlastní dotazy
 * pro vyhledávání a filtrování turnajů.
 */
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    /** Vyhledá turnaje podle názvu (case-insensitive) se stránkováním */
    Page<Tournament> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /** Vrátí seznam turnajů podle jejich stavu */
    List<Tournament> findByStatus(Status status);

    /**
     * Složitější JPQL dotaz — vrátí turnaje které jsou otevřené
     * a mají alespoň jedno volné místo (počet registrací < maxPlayers).
     */
    @Query("SELECT t FROM Tournament t WHERE t.status = 'OPEN' AND " +
            "(SELECT COUNT(r) FROM Registration r WHERE r.tournament = t) < t.maxPlayers")
    List<Tournament> findAvailableTournaments();

    /** Vyhledá turnaje podle názvu hry (case-insensitive) */
    List<Tournament> findByGameContainingIgnoreCase(String game);
}