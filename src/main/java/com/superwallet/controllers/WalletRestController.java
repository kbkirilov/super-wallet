package com.superwallet.controllers;

import com.superwallet.exceptions.AuthorizationException;
import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.helpers.AuthenticationHelper;
import com.superwallet.helpers.ModelMapper;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoOut;
import com.superwallet.services.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/wallets")
public class WalletRestController {

    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;

    @Autowired
    public WalletRestController(AuthenticationHelper authenticationHelper, ModelMapper modelMapper, WalletService walletService) {
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.walletService = walletService;
    }

    @GetMapping("{walletId}")
    public WalletDtoOut getWalletById(@PathVariable int walletId, @RequestHeader HttpHeaders httpHeaders) {

        try {
            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(httpHeaders);
            Wallet wallet = walletService.getWalletById(userAuthenticated, walletId);

            return modelMapper.fromWalletTOWalletDtoOut(wallet);
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