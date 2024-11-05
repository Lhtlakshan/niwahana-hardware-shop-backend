package org.icet.crm.repository;

import org.icet.crm.entity.User;
import org.icet.crm.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String username);

    User findByUserRole(UserRole userRole);
}
