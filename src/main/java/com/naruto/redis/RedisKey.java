package com.naruto.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import redis.clients.util.MurmurHash;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by yue.yuan on 2017/4/14.
 */
public class RedisKey implements Serializable{

    private static final Logger logger = LoggerFactory.getLogger(RedisKey.class);

    private static final long serialVersionUID = 1L;
    private static final int SEED = 65532;

    private String family;

    private String key;

    public RedisKey(String family, String key){
        this.family = family;
        this.key = key;
    }

    /*由于集群考虑了一致性hash,这不考虑hash问题*/
    private String makeRedisHashKey(){
        return String.valueOf(MurmurHash.hash64A(makeRedisKeyString().getBytes(), SEED));
    }

    private String makeRedisKeyString(){
        String newKey = family+key;
        if (!StringUtils.isEmpty(newKey)) {
            try {
                newKey = new String(newKey.getBytes(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("RedisKey.getRedisKey转码失败,错误原因:{}", e);
            }
        }
        return newKey;
    }

    public String getRedisKey(){
        return makeRedisKeyString();
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
