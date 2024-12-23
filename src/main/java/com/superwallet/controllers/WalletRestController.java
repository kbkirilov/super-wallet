package com.superwallet.controllers;

import com.superwallet.exceptions.*;
import com.superwallet.helpers.AuthenticationHelper;
import com.superwallet.helpers.ModelMapper;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.*;
import com.superwallet.services.interfaces.WalletService;
import jakarta.validation.Valid;
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
    public WalletRestController(AuthenticationHelper authenticationHelper,
                                ModelMapper modelMapper,
                                WalletService walletService) {

        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.walletService = walletService;
    }

    @PostMapping
    public WalletDtoOutWhole createWallet(@RequestHeader HttpHeaders headers,
                                          @Valid @RequestBody WalletDtoInCreate dto) {
        try {
            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(headers);
            Wallet wallet = modelMapper.fromWalletDtoInCreateToWallet(dto, userAuthenticated);
            walletService.createWallet(wallet);

            return modelMapper.fromWalletTOWalletDtoOut(wallet);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("{id}")
    public WalletDtoOutWhole getWalletById(@PathVariable int id, @RequestHeader HttpHeaders headers) {

        try {
            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(headers);
            Wallet wallet = walletService.getWalletById(userAuthenticated, id);

            return modelMapper.fromWalletTOWalletDtoOut(wallet);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}/balance")
    public WalletDtoOutBalance getWalletBalance(@PathVariable int id, @RequestHeader HttpHeaders headers) {

        try {
            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(headers);
            Wallet wallet = walletService.getWalletById(userAuthenticated, id);

            return modelMapper.fromWalletToWalletDtoOutBalance(wallet);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("{id}")
    public WalletDtoOutWhole updateWallet(@PathVariable int id,
                                          @RequestHeader HttpHeaders headers,
                                          @RequestBody WalletDtoInUpdate dto) {
        try {
            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(headers);
            Wallet walletToUpdate = walletService.getWalletById(userAuthenticated, id);

            Wallet walletUpdated = walletService.updateWallet(userAuthenticated, walletToUpdate, dto);

            return modelMapper.fromWalletTOWalletDtoOut(walletUpdated);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }  catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityUpdateNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{id}/deposit")
    public WalletDtoOutWhole depositToWallet(@PathVariable int id,
                                             @RequestBody WalletDtoInDepositWithdrawal dto,
                                             @RequestHeader HttpHeaders headers) {
        try {
            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(headers);
            Wallet walletToDeposit = walletService.getWalletById(userAuthenticated, id);

            Wallet walletUpdated = walletService.depositToWallet(
                    userAuthenticated,
                    walletToDeposit,
                    dto);

            return modelMapper.fromWalletTOWalletDtoOut(walletUpdated);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }  catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InsufficientFundsException | EntityUpdateNotAllowedException | InvalidTransactionSumException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{id}/withdraw")
    public WalletDtoOutWhole withdrawFromWallet(@PathVariable int id,
                                                @RequestBody WalletDtoInDepositWithdrawal dto,
                                                @RequestHeader HttpHeaders headers) {
        try {
            User userAuthenticated = authenticationHelper.tryGeyAuthenticatedUser(headers);
            Wallet walletToWithdraw = walletService.getWalletById(userAuthenticated, id);

            Wallet walletUpdated = walletService.withdrawalFromWallet(
                    userAuthenticated,
                    walletToWithdraw,
                    dto);

            return modelMapper.fromWalletTOWalletDtoOut(walletUpdated);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }  catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InsufficientFundsException | EntityUpdateNotAllowedException | InvalidTransactionSumException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}






















