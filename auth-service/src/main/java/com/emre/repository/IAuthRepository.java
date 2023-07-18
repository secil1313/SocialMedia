package com.emre.repository;

import com.emre.repository.entity.Auth;
import com.emre.repository.entity.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findByUsername(String username);
    Optional<Auth> findByUsernameAndPassword(String username,String password);

    List<Auth> findByRole(ERole role);

    Optional<Auth> findOptionalByEmail(String email);
}
