package com.jiuzhang.seckill.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.text.MessageFormat;

@Configuration
public class JedisConfig extends CachingConfigurerSupport {

    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    public JedisPool redisPoolFactory(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(this.maxIdle);
        config.setMaxWaitMillis(this.maxWaitMillis);
        config.setMaxTotal(this.maxActive);
        config.setMinIdle(this.minIdle);
        JedisPool jedisPool = new JedisPool(config, host, port, timeout, null);
        logger.info("JedisPool 注入成功");
        logger.info(MessageFormat.format("Redis address: {0}:{1}", host, port));
        return jedisPool;
    }
}
