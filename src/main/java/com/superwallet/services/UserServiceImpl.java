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
    public void checkIfUserIsOwnerOfWallet(User userAuthenticated, int walletId) {
        boolean isOwner = userAuthenticated.getWallets()
                .stream()
                .anyMatch(wallet -> wallet.getWalletId() == walletId);

        if (!isOwner) {
            throw new AuthorizationException(CANT_MAKE_CHANGES_IF_NOT_OWNER);
        }
    }

    @Override
    public User getUserById(User userAuthenticated, int userId) {
        if (userAuthenticated.getUserId() != userId) {
            throw new AuthorizationException(PROFILES_OF_OTHER_USERS_ERROR_MESSAGE);
        }

        return userJpaRepository
                .getUsersByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
    }

    @Override
    public User getUserByUsername(String username) {
        return userJpaRepository
                .getUsersByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("User", "username", username));
    }
}
