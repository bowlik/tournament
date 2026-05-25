package fei.upce.cz.tournament.dto;

import fei.upce.cz.tournament.entity.Tournament.Status;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TournamentResponseDto {

    private Long id;
    private String name;
    private String game;
    private String description;
    private LocalDateTime startDate;
    private int maxPlayers;
    private int currentPlayers;
    private Status status;
}