package com.zhang.firsapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Create by administrator on 2019/6/28 0028
 **/
@Controller
public class LoginController {

    /**
     * 页面路由，当使用GET请求访问/login接口，会自动跳转到`/templates/login.html`页面
     *
     * @return login登录页面路由地址
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 页面路由，当使用GET请求访问/login接口，会自动跳转到`/templates/index.html`页面
     *
     * @return index首页面路由地址
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
