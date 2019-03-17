package com.zhang.firsapplication;

import com.zhang.firsapplication.bean.User;
import com.zhang.firsapplication.dao.UserMapper;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCaching
@EnableSwagger2
@SpringBootApplication
@MapperScan("com.zhang.firsapplication.dao")
@RestController
public class FirsapplicationApplication {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String hello(){
        User user = userMapper.selectByPrimaryKey("402882ef6892703c01689272cbcd0000");

        if (user==null){
            return "hello world";
        }else {
            return user.getName();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(FirsapplicationApplication.class, args);
    }

}

