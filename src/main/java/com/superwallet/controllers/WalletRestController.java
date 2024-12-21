package com.superwallet.controllers;

import com.superwallet.exceptions.AuthorizationException;
import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.helpers.AuthenticationHelper;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.services.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/wallets")
public class WalletRestController {

    private final AuthenticationHelper authenticationHelper;
    private final WalletService walletService;

    @Autowired
    public WalletRestController(AuthenticationHelper authenticationHelper, WalletService walletService) {
        this.authenticationHelper = authenticationHelper;
        this.walletService = walletService;
    }

    @GetMapping("{walletId}")
    public Wallet getWalletById(@PathVariable int walletId, @RequestHeader HttpHeaders httpHeaders) {

        try {
            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(httpHeaders);

            return walletService.getWalletById(userAuthenticated ,walletId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

//    @GetMapping("/{walletId}/balance")
//    public double getWalletBalance(@PathVariable int walletId, @RequestHeader HttpHeaders httpHeaders) {
//
//        try {
//            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(httpHeaders);
//
//            return
//        }
//    }
}
