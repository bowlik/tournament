package fei.upce.cz.tournament.service;

import fei.upce.cz.tournament.dto.TournamentRequestDto;
import fei.upce.cz.tournament.dto.TournamentResponseDto;
import fei.upce.cz.tournament.entity.Tournament;
import fei.upce.cz.tournament.exception.ResourceNotFoundException;
import fei.upce.cz.tournament.repository.RegistrationRepository;
import fei.upce.cz.tournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service třída pro správu turnajů.
 * Obsahuje veškerou business logiku pro vytváření, úpravu,
 * mazání a vyhledávání turnajů.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final RegistrationRepository registrationRepository;

    /**
     * Vytvoří nový turnaj podle zadaných dat.
     * Nový turnaj má automaticky status OPEN.
     *
     * @param dto data pro vytvoření turnaje
     * @return vytvořený turnaj jako DTO
     */
    public TournamentResponseDto create(TournamentRequestDto dto) {
        log.info("Vytváření turnaje: {}", dto.getName());

        Tournament tournament = new Tournament();
        tournament.setName(dto.getName());
        tournament.setGame(dto.getGame());
        tournament.setDescription(dto.getDescription());
        tournament.setStartDate(dto.getStartDate());
        tournament.setMaxPlayers(dto.getMaxPlayers());

        Tournament saved = tournamentRepository.save(tournament);
        log.info("Turnaj vytvořen s id: {}", saved.getId());
        return mapToDto(saved);
    }

    /**
     * Vrátí stránkovaný seznam turnajů.
     * Pokud je zadán parametr name, filtruje podle názvu.
     *
     * @param name volitelný filtr podle názvu
     * @param pageable parametry stránkování
     * @return stránka turnajů jako DTO
     */
    public Page<TournamentResponseDto> getAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return tournamentRepository
                    .findByNameContainingIgnoreCase(name, pageable)
                    .map(this::mapToDto);
        }
        return tournamentRepository.findAll(pageable).map(this::mapToDto);
    }

    /**
     * Vrátí turnaj podle jeho ID.
     *
     * @param id ID turnaje
     * @return turnaj jako DTO
     * @throws ResourceNotFoundException pokud turnaj neexistuje
     */
    public TournamentResponseDto getById(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turnaj nenalezen"));
        return mapToDto(tournament);
    }

    /**
     * Vrátí seznam turnajů, které jsou otevřené a mají volná místa.
     * Využívá složitější JPQL dotaz z repository.
     *
     * @return seznam dostupných turnajů
     */
    public List<TournamentResponseDto> getAvailable() {
        return tournamentRepository.findAvailableTournaments()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Aktualizuje existující turnaj.
     *
     * @param id ID turnaje k aktualizaci
     * @param dto nová data turnaje
     * @return aktualizovaný turnaj jako DTO
     * @throws ResourceNotFoundException pokud turnaj neexistuje
     */
    public TournamentResponseDto update(Long id, TournamentRequestDto dto) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turnaj nenalezen"));

        tournament.setName(dto.getName());
        tournament.setGame(dto.getGame());
        tournament.setDescription(dto.getDescription());
        tournament.setStartDate(dto.getStartDate());
        tournament.setMaxPlayers(dto.getMaxPlayers());

        log.info("Aktualizace turnaje s id: {}", id);
        return mapToDto(tournamentRepository.save(tournament));
    }

    /**
     * Smaže turnaj podle jeho ID.
     *
     * @param id ID turnaje ke smazání
     * @throws ResourceNotFoundException pokud turnaj neexistuje
     */
    public void delete(Long id) {
        if (!tournamentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Turnaj nenalezen");
        }
        log.info("Mazání turnaje s id: {}", id);
        tournamentRepository.deleteById(id);
    }

    /**
     * Převede entitu Tournament na DTO objekt.
     * Doplňuje aktuální počet registrovaných hráčů.
     *
     * @param t entita turnaje
     * @return DTO objekt turnaje
     */
    private TournamentResponseDto mapToDto(Tournament t) {
        TournamentResponseDto dto = new TournamentResponseDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setGame(t.getGame());
        dto.setDescription(t.getDescription());
        dto.setStartDate(t.getStartDate());
        dto.setMaxPlayers(t.getMaxPlayers());
        dto.setStatus(t.getStatus());
        dto.setCurrentPlayers(
                registrationRepository.countByTournamentId(t.getId())
        );
        return dto;
    }
}