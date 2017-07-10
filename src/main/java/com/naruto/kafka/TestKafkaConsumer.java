package com.naruto.kafka;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by yue.yuan on 2017/4/17.
 */
@Component
public class TestKafkaConsumer {

    @KafkaListener(topics = KafkaConstants.TEST_TOPIC)
    public void processMessage(String content) {
        Gson gson = new Gson();
        LogBean log = gson.fromJson(content,LogBean.class);
        System.out.println("name:" + log.getName() +
                ",operator:" + log.getOperator());
    }
}
