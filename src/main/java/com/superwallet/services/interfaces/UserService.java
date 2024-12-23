package com.superwallet.services.interfaces;

import com.superwallet.models.User;

public interface UserService {

    User getUserByUsername(String username);
}
