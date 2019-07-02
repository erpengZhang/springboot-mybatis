package com.zhang.firsapplication.bean;

import io.swagger.annotations.ApiModel;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("用户信息")
@Table(name = "USER")
public class User implements Serializable {

    @Id
    @KeySql(sql = "SELECT REPLACE(uuid(),'-','')", order = ORDER.BEFORE)
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "AGE")
    private BigDecimal age;

    @Column(name = "PASSWORD_ID")
    private String passwordId;

    @Transient
    private String password;

    public User() {

    }

    public User(String id, String email, String name, String sex, String passwordId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.sex = sex;
        this.passwordId = passwordId;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }


    public String getSex() {
        return sex;
    }


    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(String passwordId) {
        this.passwordId = passwordId;
    }

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", passwordId='" + passwordId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}