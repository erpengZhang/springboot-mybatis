package com.zhang.firsapplication.config.security;

import com.zhang.firsapplication.dao.UserMapper;
import com.zhang.firsapplication.dao.UserPasswordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPasswordMapper userPasswordMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("用户的用户名: {}", username);
        com.zhang.firsapplication.bean.User user = new com.zhang.firsapplication.bean.User();
        user.setName(username);
        com.zhang.firsapplication.bean.User one = userMapper.selectOne(user);
        if (one != null) {
            // 封装用户信息，并返回。参数分别是：用户名，密码，用户权限
            return new User(username, userPasswordMapper.selectByPrimaryKey(one.getPasswordId()).getUserPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        } else {
            throw new UsernameNotFoundException("用户未找到！");
        }

    }
}