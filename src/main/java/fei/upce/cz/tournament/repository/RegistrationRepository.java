package fei.upce.cz.tournament.repository;

import fei.upce.cz.tournament.entity.Registration;
import fei.upce.cz.tournament.entity.Tournament;
import fei.upce.cz.tournament.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository rozhraní pro přístup k datům registrací.
 * Umožňuje vyhledávání registrací podle hráče nebo turnaje
 * a kontrolu duplicitních registrací.
 */
@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    /** Vrátí všechny registrace daného hráče */
    List<Registration> findByUser(User user);

    /** Vrátí všechny registrace v daném turnaji */
    List<Registration> findByTournament(Tournament tournament);

    /** Najde konkrétní registraci hráče v turnaji */
    Optional<Registration> findByUserAndTournament(User user, Tournament tournament);

    /** Zkontroluje zda je hráč již registrován v daném turnaji */
    boolean existsByUserAndTournament(User user, Tournament tournament);

    /**
     * Spočítá počet registrovaných hráčů v daném turnaji.
     * Používá se pro kontrolu kapacity turnaje.
     */
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.tournament.id = :tournamentId")
    int countByTournamentId(@Param("tournamentId") Long tournamentId);
}