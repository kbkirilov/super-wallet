package com.superwallet.services.interfaces;

import com.superwallet.models.User;

public interface UserService {

    void checkIfUserIsOwnerOfWallet(User userAuthenticated, int walletId);

    User getUserById(User userAuthenticated, int userId);

    User getUserByUsername(String username);
}
