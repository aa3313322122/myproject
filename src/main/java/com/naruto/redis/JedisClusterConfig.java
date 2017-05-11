package com.naruto.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yue.yuan on 2017/4/14.
 */
public class JedisClusterConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterConfig.class);

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 注意：
     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用
     * 建议使用jedis-2.8
     * @return
     */
    @Bean
    public BinaryJedisCluster getJedisCluster() {
        String[] serverArray = new String[]{};
        try {
            serverArray = redisProperties.getClusterNodes().split(",");

        }catch (Exception e) {
            LOGGER.error("redisSentinel.cache.clusterNodes无法解析");
        }
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(redisProperties.getMinIdle());
        config.setMaxIdle(redisProperties.getMaxIdle());
        config.setMaxTotal(redisProperties.getMaxActive());
        config.setMaxIdle(redisProperties.getMaxIdle());
        return new BinaryJedisCluster(nodes, redisProperties.getCommandTimeout(), redisProperties.getMaxRedirections(), config);
    }
}
