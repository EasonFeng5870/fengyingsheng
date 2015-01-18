package com.iassistent.server.user.controller;

import com.google.gson.Gson;
import com.iassistent.server.cache.sessionCache.SessionService;
import com.iassistent.server.user.bean.Account;
import com.iassistent.server.user.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by cwr.yingsheng.feng on 2015.1.2 0002.
 */
@Controller
@RequestMapping(value = "/account/v0")
public class AccountController {

    private static final Logger logger = Logger.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private SessionService sessionService;

    private static HttpHeaders JsonResponseHeaders;

    @RequestMapping(value = "/register", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> create(@RequestBody String json) {
        System.out.println("the request login form json is : " + json);
        Gson gson = new Gson();
        Account account = gson.fromJson(json, Account.class);
        int res = -1;
        try {
            res = accountService.createAccount(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonResponseHeaders = new HttpHeaders();
        JsonResponseHeaders.add("Content-Type", "application/json; charset=utf-8");
        if(res > 0){
            return new ResponseEntity<String>("{'token','"+ account.getUsername() + "|" + account.getPassword() +"'}",JsonResponseHeaders, HttpStatus.valueOf(200));
        }else{
            return new ResponseEntity<String>("{'token',''}",JsonResponseHeaders, HttpStatus.valueOf(200));
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> validateAccount(@RequestBody String json){
        System.out.println("the validate account from json is : " + json);
        Gson gson = new Gson();
        Account account = gson.fromJson(json, Account.class);
        System.out.println("the sessionService is : " + sessionService);
        try {
            account = accountService.selectAccount(account);
            if(account != null && account.getPassword() != null){
                //set session
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonResponseHeaders = new HttpHeaders();
        JsonResponseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(gson.toJson(account),JsonResponseHeaders, HttpStatus.valueOf(200));
    }

    @RequestMapping(value = "/register", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> logout(@RequestParam(value = "token", required = true) String token, HttpServletRequest request){
        System.out.println("the sessionService is : " + sessionService);
        try {
           //boolean logout = sessionService.invalidate(token);
            HttpSession session = request.getSession();
            if(session != null){
                session.invalidate();;
            }
        } catch (Exception e) {
            logger.error("log out failed!");
        }
        return new ResponseEntity<String>("", HttpStatus.OK);
    }


}
