package fei.upce.cz.tournament.service;

import fei.upce.cz.tournament.dto.TournamentResponseDto;
import fei.upce.cz.tournament.entity.Registration;
import fei.upce.cz.tournament.entity.Tournament;
import fei.upce.cz.tournament.entity.User;
import fei.upce.cz.tournament.exception.ResourceAlreadyExistsException;
import fei.upce.cz.tournament.exception.ResourceNotFoundException;
import fei.upce.cz.tournament.exception.TournamentFullException;
import fei.upce.cz.tournament.repository.RegistrationRepository;
import fei.upce.cz.tournament.repository.TournamentRepository;
import fei.upce.cz.tournament.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service třída pro správu registrací hráčů do turnajů.
 * Obsahuje business logiku pro přihlašování a odhlašování hráčů
 * včetně validace kapacity turnaje a duplicitních registrací.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    /**
     * Zaregistruje hráče do turnaje.
     * Kontroluje zda je turnaj otevřený, zda hráč není již registrován
     * a zda turnaj není plný.
     *
     * @param tournamentId ID turnaje
     * @param username uživatelské jméno hráče
     * @throws ResourceNotFoundException pokud uživatel nebo turnaj neexistuje
     * @throws ResourceAlreadyExistsException pokud je hráč již registrován
     * @throws TournamentFullException pokud je turnaj plný
     * @throws IllegalStateException pokud turnaj není otevřen
     */
    public void register(Long tournamentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel nenalezen"));

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Turnaj nenalezen"));

        if (tournament.getStatus() != Tournament.Status.OPEN) {
            throw new IllegalStateException("Turnaj není otevřen pro registrace");
        }

        if (registrationRepository.existsByUserAndTournament(user, tournament)) {
            throw new ResourceAlreadyExistsException("Hráč je již registrován v tomto turnaji");
        }

        int current = registrationRepository.countByTournamentId(tournamentId);
        if (current >= tournament.getMaxPlayers()) {
            throw new TournamentFullException("Turnaj je plný");
        }

        Registration registration = new Registration();
        registration.setUser(user);
        registration.setTournament(tournament);
        registrationRepository.save(registration);
        log.info("Hráč {} se registroval do turnaje {}", username, tournament.getName());
    }

    /**
     * Odhlásí hráče z turnaje.
     *
     * @param tournamentId ID turnaje
     * @param username uživatelské jméno hráče
     * @throws ResourceNotFoundException pokud registrace neexistuje
     */
    public void unregister(Long tournamentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel nenalezen"));

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Turnaj nenalezen"));

        Registration registration = registrationRepository
                .findByUserAndTournament(user, tournament)
                .orElseThrow(() -> new ResourceNotFoundException("Registrace nenalezena"));

        registrationRepository.delete(registration);
        log.info("Hráč {} se odregistroval z turnaje {}", username, tournament.getName());
    }

    /**
     * Vrátí seznam turnajů ve kterých je přihlášen daný hráč.
     *
     * @param username uživatelské jméno hráče
     * @return seznam turnajů jako DTO
     * @throws ResourceNotFoundException pokud uživatel neexistuje
     */
    public List<TournamentResponseDto> getMyTournaments(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel nenalezen"));

        return registrationRepository.findByUser(user)
                .stream()
                .map(r -> {
                    Tournament t = r.getTournament();
                    TournamentResponseDto dto = new TournamentResponseDto();
                    dto.setId(t.getId());
                    dto.setName(t.getName());
                    dto.setGame(t.getGame());
                    dto.setStatus(t.getStatus());
                    dto.setStartDate(t.getStartDate());
                    dto.setMaxPlayers(t.getMaxPlayers());
                    return dto;
                })
                .toList();
    }
}