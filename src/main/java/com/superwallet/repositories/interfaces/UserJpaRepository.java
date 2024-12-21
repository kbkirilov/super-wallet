package com.superwallet.repositories.interfaces;

import com.superwallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {

    Optional<User> getUsersByUserId(int userId);

    Optional<User> getUsersByUsername(String username);

}
