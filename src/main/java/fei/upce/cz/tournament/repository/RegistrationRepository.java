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

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByUser(User user);
    List<Registration> findByTournament(Tournament tournament);
    Optional<Registration> findByUserAndTournament(User user, Tournament tournament);
    boolean existsByUserAndTournament(User user, Tournament tournament);

    // Složitější dotaz — počet registrací v turnaji
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.tournament.id = :tournamentId")
    int countByTournamentId(@Param("tournamentId") Long tournamentId);
}