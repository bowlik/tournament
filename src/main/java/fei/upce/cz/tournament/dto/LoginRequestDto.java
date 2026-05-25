package fei.upce.cz.tournament.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message = "Uživatelské jméno je povinné")
    private String username;

    @NotBlank(message = "Heslo je povinné")
    private String password;
}