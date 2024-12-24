package com.superwallet.repositories.interfaces;

import com.superwallet.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusJpaRepository extends JpaRepository<Status, Integer> {

    Optional<Status> getStatusByStatusId(int statusId);
}
