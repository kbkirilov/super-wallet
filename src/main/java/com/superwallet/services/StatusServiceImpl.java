package com.superwallet.services;

import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.models.Status;
import com.superwallet.repositories.interfaces.StatusJpaRepository;
import com.superwallet.services.interfaces.StatusService;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusJpaRepository statusJpaRepository;

    public StatusServiceImpl(StatusJpaRepository statusJpaRepository) {
        this.statusJpaRepository = statusJpaRepository;
    }

    @Override
    public Status getStatusById(int id) {
        return statusJpaRepository
                .getStatusByStatusId(id)
                .orElseThrow(() -> new EntityNotFoundException("Status", id));
    }
}
