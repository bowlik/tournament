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

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    // Vyhledávání podle názvu (pro stránkování)
    Page<Tournament> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Filtrování podle statusu
    List<Tournament> findByStatus(Status status);

    // Složitější dotaz — turnaje s volnými místy
    @Query("SELECT t FROM Tournament t WHERE t.status = 'OPEN' AND " +
            "(SELECT COUNT(r) FROM Registration r WHERE r.tournament = t) < t.maxPlayers")
    List<Tournament> findAvailableTournaments();

    // Vyhledávání podle hry
    List<Tournament> findByGameContainingIgnoreCase(String game);
}