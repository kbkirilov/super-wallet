package com.superwallet.services.interfaces;

import com.superwallet.models.User;

public interface UserService {

    User getUserById(User userAuthenticated, int userId);

    User getUserByUsername(String username);
}
