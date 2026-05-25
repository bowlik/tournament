package fei.upce.cz.tournament;

import fei.upce.cz.tournament.dto.RegisterRequestDto;
import fei.upce.cz.tournament.dto.TournamentRequestDto;
import fei.upce.cz.tournament.entity.Tournament;
import fei.upce.cz.tournament.entity.User;
import fei.upce.cz.tournament.exception.ResourceAlreadyExistsException;
import fei.upce.cz.tournament.exception.ResourceNotFoundException;
import fei.upce.cz.tournament.repository.TournamentRepository;
import fei.upce.cz.tournament.repository.UserRepository;
import fei.upce.cz.tournament.service.TournamentService;
import fei.upce.cz.tournament.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TournamentApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @BeforeEach
    void setUp() {
        tournamentRepository.deleteAll();
        userRepository.deleteAll();
    }

    // ===== USER TESTY =====

    @Test
    void registraceNovehoUzivatele_uspech() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("testuser");
        dto.setPassword("heslo123");
        dto.setEmail("test@test.cz");

        var result = userService.register(dto);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(User.Role.PLAYER, result.getRole());
    }

    @Test
    void registraceDuplikatnihoUzivatele_vyhodiVyjimku() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("testuser");
        dto.setPassword("heslo123");
        dto.setEmail("test@test.cz");
        userService.register(dto);

        RegisterRequestDto dto2 = new RegisterRequestDto();
        dto2.setUsername("testuser");
        dto2.setPassword("heslo456");
        dto2.setEmail("test2@test.cz");

        assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.register(dto2));
    }

    @Test
    void registraceDuplikatnihoEmailu_vyhodiVyjimku() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("user1");
        dto.setPassword("heslo123");
        dto.setEmail("stejny@test.cz");
        userService.register(dto);

        RegisterRequestDto dto2 = new RegisterRequestDto();
        dto2.setUsername("user2");
        dto2.setPassword("heslo456");
        dto2.setEmail("stejny@test.cz");

        assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.register(dto2));
    }

    @Test
    void getUserById_uspech() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("testuser");
        dto.setPassword("heslo123");
        dto.setEmail("test@test.cz");
        var registered = userService.register(dto);

        var result = userService.getUserById(registered.getId());

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getUserById_neexistujiciId_vyhodiVyjimku() {
        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(999L));
    }

    // ===== TOURNAMENT TESTY =====

    @Test
    void vytvoreniTurnaje_uspech() {
        TournamentRequestDto dto = new TournamentRequestDto();
        dto.setName("Jarní turnaj");
        dto.setGame("CS2");
        dto.setDescription("Popis turnaje");
        dto.setStartDate(LocalDateTime.now().plusDays(7));
        dto.setMaxPlayers(16);

        var result = tournamentService.create(dto);

        assertNotNull(result);
        assertEquals("Jarní turnaj", result.getName());
        assertEquals(Tournament.Status.OPEN, result.getStatus());
        assertEquals(16, result.getMaxPlayers());
    }

    @Test
    void getTournamentById_neexistujiciId_vyhodiVyjimku() {
        assertThrows(ResourceNotFoundException.class,
                () -> tournamentService.getById(999L));
    }

    @Test
    void smazaniTurnaje_uspech() {
        TournamentRequestDto dto = new TournamentRequestDto();
        dto.setName("Turnaj ke smazání");
        dto.setGame("Valorant");
        dto.setStartDate(LocalDateTime.now().plusDays(3));
        dto.setMaxPlayers(8);
        var created = tournamentService.create(dto);

        tournamentService.delete(created.getId());

        assertThrows(ResourceNotFoundException.class,
                () -> tournamentService.getById(created.getId()));
    }

    @Test
    void smazaniNeexistujicihoTurnaje_vyhodiVyjimku() {
        assertThrows(ResourceNotFoundException.class,
                () -> tournamentService.delete(999L));
    }

    @Test
    void aktualizaceTurnaje_uspech() {
        TournamentRequestDto dto = new TournamentRequestDto();
        dto.setName("Původní název");
        dto.setGame("LoL");
        dto.setStartDate(LocalDateTime.now().plusDays(5));
        dto.setMaxPlayers(8);
        var created = tournamentService.create(dto);

        TournamentRequestDto updateDto = new TournamentRequestDto();
        updateDto.setName("Nový název");
        updateDto.setGame("LoL");
        updateDto.setStartDate(LocalDateTime.now().plusDays(5));
        updateDto.setMaxPlayers(16);

        var updated = tournamentService.update(created.getId(), updateDto);

        assertEquals("Nový název", updated.getName());
        assertEquals(16, updated.getMaxPlayers());
    }
}