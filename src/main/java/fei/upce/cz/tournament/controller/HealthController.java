package fei.upce.cz.tournament.controller;

import fei.upce.cz.tournament.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Slf4j
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponseDto<Map<String, String>>> health() {
        log.info("Health check volán v {}", LocalDateTime.now());
        return ResponseEntity.ok(ApiResponseDto.ok("Aplikace běží",
                Map.of(
                        "status", "UP",
                        "time", LocalDateTime.now().toString(),
                        "version", "1.0.0"
                )));
    }
}