package com.zhang.firsapplication.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.zhang.firsapplication.bean.User;
import com.zhang.firsapplication.dao.UserMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * @author erpengZhang
 */
@RestController
public class UserController {

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @ApiOperation(value = "添加用户", notes = "添加用户")
    @PostMapping("/user")
    public User add(User user){
        userMapper.insertSelective(user);
        return user;
    }

    @GetMapping("/user")
    @Cacheable(cacheNames = {"user"})
    public User get(String id){
        return  userMapper.selectByPrimaryKey(id);
    }

    @PutMapping("/user")
    @CachePut(cacheNames = {"user"}, key = "#p0.id")
    public User update(User user){
        userMapper.updateByPrimaryKeySelective(user);
        return userMapper.selectByPrimaryKey(user.getId());
    }

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

