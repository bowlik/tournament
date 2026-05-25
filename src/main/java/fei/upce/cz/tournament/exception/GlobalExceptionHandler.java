package fei.upce.cz.tournament.exception;

import fei.upce.cz.tournament.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

/**
 * Globální handler pro výjimky v celé aplikaci.
 * Zachytává výjimky z controllerů a vrací jednotný formát chybových odpovědí.
 * Díky @RestControllerAdvice se aplikuje na všechny REST controllery.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Zpracuje výjimku když požadovaný zdroj nebyl nalezen.
     * Vrací HTTP 404 Not Found.
     *
     * @param e výjimka s popisem chyby
     * @return chybová odpověď s HTTP 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotFound(
            ResourceNotFoundException e) {
        log.warn("Nenalezeno: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDto.error(e.getMessage()));
    }

    /**
     * Zpracuje výjimku když se uživatel pokusí vytvořit již existující zdroj.
     * Vrací HTTP 409 Conflict.
     *
     * @param e výjimka s popisem chyby
     * @return chybová odpověď s HTTP 409
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleAlreadyExists(
            ResourceAlreadyExistsException e) {
        log.warn("Již existuje: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponseDto.error(e.getMessage()));
    }

    /**
     * Zpracuje výjimku když je turnaj plný a nelze se registrovat.
     * Vrací HTTP 409 Conflict.
     *
     * @param e výjimka s popisem chyby
     * @return chybová odpověď s HTTP 409
     */
    @ExceptionHandler(TournamentFullException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleTournamentFull(
            TournamentFullException e) {
        log.warn("Turnaj plný: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponseDto.error(e.getMessage()));
    }

    /**
     * Zpracuje výjimku při selhání validace vstupních dat.
     * Sbírá všechny chybové zprávy z polí a vrací je jako jeden řetězec.
     * Vrací HTTP 400 Bad Request.
     *
     * @param e výjimka obsahující seznam chyb validace
     * @return chybová odpověď s HTTP 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleValidation(
            MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Chyba validace: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(errors));
    }

    /**
     * Zpracuje výjimku při špatných přihlašovacích údajích.
     * Vrací HTTP 401 Unauthorized.
     *
     * @param e výjimka
     * @return chybová odpověď s HTTP 401
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBadCredentials(
            BadCredentialsException e) {
        log.warn("Špatné přihlašovací údaje");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDto.error("Špatné uživatelské jméno nebo heslo"));
    }

    /**
     * Zpracuje výjimku při pokusu o přístup k chráněnému endpointu bez oprávnění.
     * Vrací HTTP 403 Forbidden.
     *
     * @param e výjimka
     * @return chybová odpověď s HTTP 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleAccessDenied(
            AccessDeniedException e) {
        log.warn("Přístup odepřen");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponseDto.error("Nemáte oprávnění k této akci"));
    }

    /**
     * Zachytí všechny ostatní neočekávané výjimky.
     * Vrací HTTP 500 Internal Server Error.
     *
     * @param e výjimka
     * @return chybová odpověď s HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGeneral(Exception e) {
        log.error("Neočekávaná chyba: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDto.error("Interní chyba serveru"));
    }
}