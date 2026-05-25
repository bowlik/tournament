package fei.upce.cz.tournament.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 * Entita reprezentující uživatele systému.
 * Implementuje UserDetails pro integraci se Spring Security.
 * Uživatel může mít roli PLAYER nebo ADMIN.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    /** Unikátní identifikátor uživatele */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unikátní uživatelské jméno použité pro přihlášení */
    @Column(unique = true, nullable = false)
    private String username;

    /** Hashované heslo uživatele (BCrypt) */
    @Column(nullable = false)
    private String password;

    /** Unikátní emailová adresa uživatele */
    @Column(unique = true, nullable = false)
    private String email;

    /** Role uživatele — určuje jeho oprávnění v systému */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.PLAYER;

    /**
     * Výčet dostupných rolí uživatele.
     * PLAYER — běžný hráč, ADMIN — správce systému.
     */
    public enum Role {
        PLAYER, ADMIN
    }

    /**
     * Vrátí seznam oprávnění uživatele na základě jeho role.
     * Formát oprávnění je "ROLE_" + název role (např. ROLE_ADMIN).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}