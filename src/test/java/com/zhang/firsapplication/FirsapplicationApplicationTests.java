package com.zhang.firsapplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FirsapplicationApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    public void contextLoads() {
        SetOperations<String, String> stringStringSetOperations = stringRedisTemplate.opsForSet();
//        stringStringSetOperations.add("test","a","b","c","d","e","f","g","h","i","j","k");
        System.out.println(stringStringSetOperations.members("test"));
        System.out.println(stringStringSetOperations.randomMember("test"));
    }

}

