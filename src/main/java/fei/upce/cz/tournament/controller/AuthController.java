package fei.upce.cz.tournament.controller;

import fei.upce.cz.tournament.config.JwtService;
import fei.upce.cz.tournament.dto.ApiResponseDto;
import fei.upce.cz.tournament.dto.LoginRequestDto;
import fei.upce.cz.tournament.dto.RegisterRequestDto;
import fei.upce.cz.tournament.dto.UserResponseDto;
import fei.upce.cz.tournament.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto dto) {
        log.info("Požadavek na registraci: {}", dto.getUsername());
        UserResponseDto user = userService.register(dto);
        return ResponseEntity.ok(ApiResponseDto.ok("Registrace úspěšná", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<Map<String, String>>> login(
            @Valid @RequestBody LoginRequestDto dto) {
        log.info("Požadavek na přihlášení: {}", dto.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(), dto.getPassword()));

        UserDetails userDetails = userService.loadUserByUsername(dto.getUsername());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(ApiResponseDto.ok("Přihlášení úspěšné",
                Map.of("token", token)));
    }
}