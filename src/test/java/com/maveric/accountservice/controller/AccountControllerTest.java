package com.maveric.accountservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.dto.BalanceDto;
import com.maveric.accountservice.entity.Account;
import com.maveric.accountservice.enums.Type;
import com.maveric.accountservice.repository.AccountRepository;
import com.maveric.accountservice.services.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.maveric.accountservice.enums.Constants.ACCOUNT_DELETED_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes=AccountController.class)
//@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(AccountController.class)


public class AccountControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Mock
    private List<Account> account;
    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

//    @MockBean
//    private AccountService accountService;

    @Autowired
    private MockMvc mock;


    @Mock
    ResponseEntity<BalanceDto> balanceDto;

    @Test
    void shouldGetBalanceWhenRequestMadeToGetBalance() throws Exception{
        when(accountService.getAccountByAccId("1","1")).thenReturn(getAccountDto());
        mock.perform(get("/api/v1/customers/1/accounts/1").header("userId",1))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void getAccounts() throws Exception
    {

        mock.perform(get("/api/v1/customers/1234/accounts")
                        .contentType(MediaType.APPLICATION_JSON).header("userId", "1234"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void notgetAccounts() throws Exception {
//        String invalidApiV1 = new String();
        mock.perform(get("/api/v1/customers/12346/accounts")
                .contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    void deleteAccounts() throws Exception {
        ResponseEntity<AccountDto> responseEntity = new ResponseEntity<>(getAccountDto(), HttpStatus.OK);
        mock.perform(delete("/api/v1/customers/1/accounts/1234")
                .contentType(MediaType.APPLICATION_JSON));

    }
    @Test

    void updateAccount() throws Exception{
        ResponseEntity<AccountDto> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        Object AccountDto = new Object();
        when(accountService.updateAccount(any())).thenReturn(getAccountDto());
        mock.perform(MockMvcRequestBuilders.put("/api/v1/customers/1234/accounts/1234")
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(getAccountDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    void createAccounts() throws Exception{
        ResponseEntity<AccountDto> responseEntity = new ResponseEntity<>(getAccountDto(), HttpStatus.OK);
        when(accountService.createAccount(any(), any(AccountDto.class))).thenReturn(getAccountDto());
        mock.perform(MockMvcRequestBuilders.post("/api/v1/customers/1/accounts")
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(getAccountDto())))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    public AccountDto getAccountDto(){
        AccountDto accountDto=new AccountDto();
        accountDto.setCustomerId("1");
        accountDto.setType(Type.SAVINGS);
        accountDto.set_id("1");
        return accountDto;
    }


    @Test
    void NotupdateAccount() throws Exception{
        ResponseEntity<AccountDto> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        Object AccountDto = new Object();
        when(accountService.updateAccount(any())).thenReturn(getAccountDto());
        mock.perform(MockMvcRequestBuilders.put("/api/v1/customers/2/accounts/3456")
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(getAccountDto())))

                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void createAccounts_failure() throws Exception{
        ResponseEntity<AccountDto> responseEntity = new ResponseEntity<>(getAccountDto1(), HttpStatus.OK);
       when(accountService.createAccount(any(), any(AccountDto.class))).thenReturn(getAccountDto1());
        mock.perform(MockMvcRequestBuilders.post("/api/v1/customers/1/accounts")
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(getAccountDto1())))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    public AccountDto getAccountDto1(){
        AccountDto accountDto=new AccountDto();
        accountDto.setCustomerId("1");
        return accountDto;
    }

    public ResponseEntity<List<Account>> getSampleAccount(){

        List<Account> accountList = new ArrayList<>();
        Account account = new Account();
        account.setCustomerId("1");

        Account account1 = new Account();
        account1.setCustomerId("2");


        accountList.add(account1);
        accountList.add(account);
        return ResponseEntity.status(HttpStatus.OK).body(accountList);
    }
}

