package com.zhang.firsapplication.bean;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "USER_PASSWORD")
public class AccountPassword {

    @Id
    @KeySql(sql = "select REPLACE(uuid(),'-','')", order = ORDER.BEFORE)
    private String id;
    private String password;
}
