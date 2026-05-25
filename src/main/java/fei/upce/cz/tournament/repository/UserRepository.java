package fei.upce.cz.tournament.repository;

import fei.upce.cz.tournament.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository rozhraní pro přístup k datům uživatelů.
 * Rozšiřuje JpaRepository o vlastní dotazy pro vyhledávání
 * podle uživatelského jména a emailu.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** Najde uživatele podle uživatelského jména */
    Optional<User> findByUsername(String username);

    /** Najde uživatele podle emailové adresy */
    Optional<User> findByEmail(String email);

    /** Zkontroluje zda existuje uživatel s daným uživatelským jménem */
    boolean existsByUsername(String username);

    /** Zkontroluje zda existuje uživatel s daným emailem */
    boolean existsByEmail(String email);
}