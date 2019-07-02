package com.zhang.firsapplication.server.impl;

import com.zhang.firsapplication.bean.User;
import com.zhang.firsapplication.bean.UserPassword;
import com.zhang.firsapplication.dao.UserMapper;
import com.zhang.firsapplication.dao.UserPasswordMapper;
import com.zhang.firsapplication.server.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create by administrator on 2019/6/27 0027
 **/
@Service
public class UserSeviceImpl implements UserSevice {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPasswordMapper userPasswordMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional()
    public User save(User user) {
        UserPassword userPassword = new UserPassword();
        userPassword.setUserPassword(passwordEncoder.encode(user.getPassword()));
        if (userPasswordMapper.insert(userPassword) > 0) {
            user.setPasswordId(userPassword.getId());
            userMapper.insert(user);
        }
        return user;
    }
}
