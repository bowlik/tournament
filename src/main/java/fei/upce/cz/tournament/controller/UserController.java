package fei.upce.cz.tournament.controller;

import fei.upce.cz.tournament.dto.ApiResponseDto;
import fei.upce.cz.tournament.dto.UserResponseDto;
import fei.upce.cz.tournament.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponseDto.ok("OK",
                userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.ok("OK",
                userService.getUserById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponseDto.ok("Uživatel smazán", null));
    }
}