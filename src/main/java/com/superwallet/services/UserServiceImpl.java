package com.superwallet.services;

import com.superwallet.exceptions.AuthorizationException;
import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.models.User;
import com.superwallet.repositories.interfaces.UserJpaRepository;
import com.superwallet.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.superwallet.helpers.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userJpaRepository;

    @Autowired
    public UserServiceImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User getUserByUsername(String username) {
        return userJpaRepository
                .getUsersByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("User", "username", username));
    }
}
