package com.zhang.firsapplication.controller;

import com.zhang.firsapplication.bean.Account;
import com.zhang.firsapplication.dao.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class AccountController {

    @Autowired
    private AccountMapper accountMapper;

    @PostMapping("/account")
    public Object addAccount(Account account) {
        int i = accountMapper.insertSelective(account);
        return i;
    }

    @GetMapping("/success")
    public String success(Map<String,Object> map){
        map.put("hello","你好");
        return "success";
    }
}
