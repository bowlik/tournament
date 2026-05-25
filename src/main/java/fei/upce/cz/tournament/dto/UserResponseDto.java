package fei.upce.cz.tournament.dto;

import fei.upce.cz.tournament.entity.User.Role;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private Role role;
}