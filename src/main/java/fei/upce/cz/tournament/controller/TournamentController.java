package fei.upce.cz.tournament.controller;

import fei.upce.cz.tournament.dto.ApiResponseDto;
import fei.upce.cz.tournament.dto.TournamentRequestDto;
import fei.upce.cz.tournament.dto.TournamentResponseDto;
import fei.upce.cz.tournament.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<TournamentResponseDto>>> getAll(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponseDto.ok("OK",
                tournamentService.getAll(name, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TournamentResponseDto>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.ok("OK",
                tournamentService.getById(id)));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponseDto<List<TournamentResponseDto>>> getAvailable() {
        return ResponseEntity.ok(ApiResponseDto.ok("OK",
                tournamentService.getAvailable()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<TournamentResponseDto>> create(
            @Valid @RequestBody TournamentRequestDto dto) {
        return ResponseEntity.ok(ApiResponseDto.ok("Turnaj vytvořen",
                tournamentService.create(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<TournamentResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody TournamentRequestDto dto) {
        return ResponseEntity.ok(ApiResponseDto.ok("Turnaj aktualizován",
                tournamentService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        tournamentService.delete(id);
        return ResponseEntity.ok(ApiResponseDto.ok("Turnaj smazán", null));
    }
}