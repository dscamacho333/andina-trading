package co.edu.unbosque.microservice_investor.repository;


import co.edu.unbosque.microservice_investor.model.entity.User;
import co.edu.unbosque.microservice_investor.model.enums.AccountStatus;
import co.edu.unbosque.microservice_investor.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);

    // Buscar por nombre (opcional, útil para búsqueda en frontend)
    Optional<User> findByName(String name);

    // Buscar todos los usuarios por rol (Comisionista, Inversionista, etc.)
    List<User> findAllByRole(Role role);

    // Buscar usuarios con estado específico
    List<User> findAllByAccountStatus(AccountStatus status);

    public List<User> findAllByHasSubscriptionTrue();

    // Buscar por nombre que contenga un texto (para búsqueda parcial)
    List<User> findByNameContainingIgnoreCase(String partialName);

    // Verificar existencia por email
    boolean existsByEmail(String email);


}