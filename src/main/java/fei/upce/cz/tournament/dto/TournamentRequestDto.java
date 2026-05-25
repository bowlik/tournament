package fei.upce.cz.tournament.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TournamentRequestDto {

    @NotBlank(message = "Název turnaje je povinný")
    @Size(min = 3, max = 100, message = "Název musí mít 3–100 znaků")
    private String name;

    @NotBlank(message = "Název hry je povinný")
    private String game;

    private String description;

    @NotNull(message = "Datum začátku je povinné")
    @Future(message = "Datum začátku musí být v budoucnosti")
    private LocalDateTime startDate;

    @Min(value = 2, message = "Turnaj musí mít alespoň 2 hráče")
    @Max(value = 128, message = "Turnaj může mít maximálně 128 hráčů")
    private int maxPlayers;
}