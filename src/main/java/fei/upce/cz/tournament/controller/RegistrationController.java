package fei.upce.cz.tournament.controller;

import fei.upce.cz.tournament.dto.ApiResponseDto;
import fei.upce.cz.tournament.dto.TournamentResponseDto;
import fei.upce.cz.tournament.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/{tournamentId}")
    public ResponseEntity<ApiResponseDto<Void>> register(
            @PathVariable Long tournamentId,
            Principal principal) {
        registrationService.register(tournamentId, principal.getName());
        return ResponseEntity.ok(ApiResponseDto.ok("Registrace úspěšná", null));
    }

    @DeleteMapping("/{tournamentId}")
    public ResponseEntity<ApiResponseDto<Void>> unregister(
            @PathVariable Long tournamentId,
            Principal principal) {
        registrationService.unregister(tournamentId, principal.getName());
        return ResponseEntity.ok(ApiResponseDto.ok("Odregistrování úspěšné", null));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponseDto<List<TournamentResponseDto>>> myTournaments(
            Principal principal) {
        return ResponseEntity.ok(ApiResponseDto.ok("OK",
                registrationService.getMyTournaments(principal.getName())));
    }
}