package com.zhang.firsapplication.bean;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "USER_PASSWORD")
public class UserPassword {

    @Id
    @KeySql(sql = "SELECT REPLACE(uuid(),'-','')", order = ORDER.BEFORE)
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;

    @Column(name = "USER_PASSWORD")
    private String userPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }
}