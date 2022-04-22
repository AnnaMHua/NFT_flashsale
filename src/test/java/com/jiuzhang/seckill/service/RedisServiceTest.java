package com.jiuzhang.seckill.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisServiceTest {

    @Resource
    private RedisService redisService;


    @Test
    void getValue() {
        String value = redisService.getValue("stock:19");
        assertEquals(new Long(value), 10L);
    }

    @Test
    void stockDeductValidator() {
        boolean result =  redisService.stockDeductValidator("stock:19");
        assertEquals(result, true);
        System.out.println("result:"+result);
        String stock =  redisService.getValue("stock:19");
        System.out.println("stock:"+stock);
    }
}