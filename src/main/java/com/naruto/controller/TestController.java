package com.naruto.controller;

import com.naruto.kafka.TestKafkaProducer;
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

    @RequestMapping("/testKafka")
    public void testKafka() {
        kafkaProducer.sendMessage();
    }

}
