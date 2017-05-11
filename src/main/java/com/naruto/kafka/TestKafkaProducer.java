package com.naruto.kafka;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Created by yue.yuan on 2017/4/17.
 */
@Component
public class TestKafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage() {
        LogBean log = new LogBean();
        log.setName("naruto");
        log.setOperator(2);
        Gson gson = new Gson();
        kafkaTemplate.send(KafkaConstants.TEST_TOPIC, gson.toJson(log));

    }
}
