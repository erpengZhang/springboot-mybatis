package com.zhang.firsapplication.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.zhang.firsapplication.bean.User;
import com.zhang.firsapplication.dao.UserMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @ApiOperation(value = "添加用户", notes = "添加用户")
    @PostMapping("/user")
    public Object add(User user){
        return userMapper.insertSelective(user);
    }

    @GetMapping("/user")
    @Cacheable(cacheNames = {"user"})
    public Object get(User user){

        return userMapper.select(user);
    }

    @GetMapping("/druid/stat")
    public Object druidStat(){
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，
        // 除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }

}

