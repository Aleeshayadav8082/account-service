package com.maveric.accountservice.controller;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.dto.UserDto;
import com.maveric.accountservice.entity.Account;
import com.maveric.accountservice.exception.AccountIDNotfoundException;
import com.maveric.accountservice.exception.AccountNotFoundException;
import com.maveric.accountservice.exception.CustomerIDNotFoundExistsException;
import com.maveric.accountservice.feignclient.FeignConsumer;
import com.maveric.accountservice.repository.AccountRepository;
import com.maveric.accountservice.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import com.maveric.accountservice.exception.CustomerIdMissmatchException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;



@RestController

@RequestMapping("/api/v1")
public class AccountController {
    @Autowired

    AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    FeignConsumer feignConsumer;


    @GetMapping("customers/{customerId}/accounts/{accountId}")
    public AccountDto getAccount(@PathVariable("customerId") String customerId, @Valid
    @PathVariable("accountId") String accountId) throws AccountNotFoundException ,CustomerIDNotFoundExistsException{
        AccountDto accounts=accountService.getAccountByAccId(customerId,accountId);
        return accounts;
    }
    @PutMapping("customers/{customerId}/accounts/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable(name = "customerId") String customerId, @Valid @PathVariable(name = "accountId") String accountId, @RequestBody Account account) {
        Account AccountDto = accountService.updateAccount(customerId, accountId, account);

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("message",

                "Account successfully updated");


        return new ResponseEntity<>(AccountDto, responseHeaders, HttpStatus.OK);

    }

    @GetMapping("customers/{customerId}/accounts")
    public ResponseEntity<List<AccountDto>> getAccountByCustomerId(@PathVariable String customerId, @Valid @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "10") @Valid Integer pageSize)throws CustomerIdMissmatchException {

        List<AccountDto> accountDtoResponse = accountService.getAccountByUserId(page, pageSize, customerId);
        return new ResponseEntity<>(accountDtoResponse, HttpStatus.OK);

    }

    @DeleteMapping("customers/{customerId}/accounts/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable String customerId,@Valid
    @PathVariable String accountId) throws AccountNotFoundException,CustomerIdMissmatchException{

        String result = accountService.deleteAccount(accountId,customerId);

        if(result!=null) {

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(accountId+"AccountIdNotFound", HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("customers/{customerId}/accounts")
    public ResponseEntity<AccountDto> createAccount (@PathVariable String customerId, @Valid @RequestBody AccountDto
            accountDto, HttpServletRequest request){

        UserDto userDto = feignConsumer.getUserById(customerId).getBody();
        if(userDto.getId().equals(accountDto.getCustomerId())) {
            AccountDto accountDtoResponse = accountService.createAccount(customerId, accountDto);
            return new ResponseEntity<>(accountDtoResponse, HttpStatus.CREATED);
        } else {
            throw new CustomerIDNotFoundExistsException("Customer does not exist");
        }


    }

}

