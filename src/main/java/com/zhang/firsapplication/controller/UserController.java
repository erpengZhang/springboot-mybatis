package com.zhang.firsapplication.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.zhang.firsapplication.bean.User;
import com.zhang.firsapplication.dao.UserMapper;
import com.zhang.firsapplication.server.UserSevice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 */
@RestController
@Api(tags = "用户信息接口")
public class UserController {

    private final UserMapper userMapper;

    @Autowired
    private UserSevice userSevice;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @ApiOperation(value = "添加用户", notes = "添加用户")
    @PostMapping("/user")
    public User add(User user){
        return userSevice.save(user);
    }

    @ApiOperation(value = "查询用户", notes = "查询用户")
    @GetMapping("/user")
    @Cacheable(cacheNames = {"user"})
    public User get(String id){
        return  userMapper.selectByPrimaryKey(id);
    }

    @ApiOperation(value = "更新用户", notes = "更新用户")
    @PutMapping("/user")
    @CachePut(cacheNames = {"user"}, key = "#p0.id")
    public User update(User user){
        userMapper.updateByPrimaryKeySelective(user);
        return userMapper.selectByPrimaryKey(user.getId());
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @CacheEvict(cacheNames = "user", key = "#p0.id")
    @DeleteMapping("/user")
    public User delete(User user){
        userMapper.delete(user);
        return user;
    }

    @GetMapping("/druid/stat")
    public Object druidStat(){
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，
        // 除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }

}

