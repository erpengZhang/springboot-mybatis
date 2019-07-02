package com.zhang.firsapplication.config;


import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
public class MybatisConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // 配置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        // 第一次启动的时候自动建表（可以不用这句话，自己手动建表，源码中有语句的）
//         jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        /*return new ConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };*/

        return configuration -> configuration.setMapUnderscoreToCamelCase(true);
    }
}
