package com.naruto.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

/**
 * Created by yue.yuan on 2017/4/17.
 */
@Configuration
public class CustomKafkaBeans {

    /**
     * Customized ProducerFactory bean.
     * @param properties the kafka properties.
     * @return the bean.
     */
    /*@Bean
    public ProducerFactory<?, ?> kafkaProducerFactory(KafkaProperties properties) {
        Map<String, Object> producerProperties = properties.buildProducerProperties();
        producerProperties.put(CommonClientConfigs.METRIC_REPORTER_CLASSES_CONFIG,
                MyProducerMetricsReporter.class);
        return new DefaultKafkaProducerFactory<Object, Object>(producerProperties);
    }

    *//**
     * Customized ConsumerFactory bean.
     * @param properties the kafka properties.
     * @return the bean.
     *//*
    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties properties) {
        Map<String, Object> consumerProperties = properties.buildConsumerProperties();
        consumerProperties.put(CommonClientConfigs.METRIC_REPORTER_CLASSES_CONFIG,
                MyConsumerMetricsReporter.class);
        return new DefaultKafkaConsumerFactory<Object, Object>(consumerProperties);
    }*/
}
