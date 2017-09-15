package com.naruto.controller;

import com.naruto.kafka.TestKafkaProducer;
import com.naruto.redis.TestRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yue.yuan on 2017/4/17.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestKafkaProducer kafkaProducer;

    @Autowired
    private TestRedis testRedis;

    @RequestMapping("/testKafka")
    public void testKafka() {
        kafkaProducer.sendMessage();
    }

    @RequestMapping("/testRedis")
    public String testRedis(String key) {
        return testRedis.testRedis(key);
    }

}
