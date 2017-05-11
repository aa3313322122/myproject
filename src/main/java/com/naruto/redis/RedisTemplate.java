package com.naruto.redis;

import com.naruto.util.JsonUtil;
import com.naruto.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.BinaryJedisCluster;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by yue.yuan on 2017/4/14.
 */
public class RedisTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTemplate.class);

    @Autowired
    protected BinaryJedisCluster jedisCluster;

    @Autowired
    protected RedisProperties redisProperties;


    /**
     * 获取C++端存放的信息
     * 说明:C++的redis构建的RedisKey比较特殊,使用实例:
     * RedisKey redisKey = new RedisKey(RedisConstants.C_FAMILY_KEY, "4227ceb737ce779f7a8eb236d2815dc06dd9b551");
     * "4227ceb737ce779f7a8eb236d2815dc06dd9b551"就是C++端的redis的key值

     *
     * @param redisKey 缓存的key对象
     * @return Map
     */
    public Map<String, Object> getValueFromC(final RedisKey redisKey) throws UnsupportedEncodingException {
        Map<String, Object> value = JsonUtil.parseJSON2Map(jedisCluster.get(redisKey.getRedisKey().getBytes()));
        LOGGER.debug("RedisUtil:get cache key={},value={}", redisKey.getRedisKey(), value);
        return value;
    }

    /**
     * 获取C++端存放的信息
     * 说明:C++的redis构建的RedisKey比较特殊,使用实例:
     * RedisKey redisKey = new RedisKey(RedisConstants.C_FAMILY_KEY, "4227ceb737ce779f7a8eb236d2815dc06dd9b551");
     * "4227ceb737ce779f7a8eb236d2815dc06dd9b551"就是C++端的redis的key值
     *
     *
     * @param redisKey 缓存的key对象
     * @return Object
     */
    public Object getObjectFromC(final RedisKey redisKey, Class pojoClass) throws UnsupportedEncodingException {
        Object value = null;
        byte[] bytes;
        if (null!=redisKey && null!=redisKey.getRedisKey() && redisKey.getRedisKey().length()>0) {
            if (redisKey.getRedisKey().startsWith("【prefix】")) {
                bytes = jedisCluster.get(redisKey.getRedisKey().getBytes());
                if (null == bytes) {
                    return null;
                }
                try {
                    LOGGER.debug("从缓存中取出的对象：{}",SerializeUtil.unserialize(bytes));
                    value = JsonUtil.parseJsonStr2Object(SerializeUtil.unserialize(bytes).toString(),pojoClass);
                } catch (Exception e) {
                    LOGGER.info("JAVA获取缓存对象失败, redisKey的key:{}, redisKey的value:{} 错误原因:{}", redisKey.getRedisKey(), value, e);
                }
            } else {
                try {
                    value = JsonUtil.parseJsonStr2Object(jedisCluster.get(redisKey.getRedisKey().getBytes()), pojoClass);
                } catch (Exception e) {
                    LOGGER.info("C++获取缓存对象失败, redisKey的key:{}, redisKey的value:{} 错误原因:{}", redisKey.getRedisKey(), value, e);
                }
            }
        }
        LOGGER.debug("RedisUtil:getObjectFromC cache key={},value={}", redisKey.getRedisKey(), value);
        return value;
    }

    /**
     * 设置永不过期
     * @param redisKey 缓存的key对象
     * @param value  缓存value
     */
    public void set(RedisKey redisKey, Object value) {
        jedisCluster.set(redisKey.getRedisKey().getBytes(), SerializeUtil.serialize(value));
        LOGGER.debug("RedisUtil:set cache key={},value={}", redisKey.getRedisKey(), value);
    }

    public void setnx(RedisKey redisKey, Object value){
        jedisCluster.setnx(redisKey.getRedisKey().getBytes(), SerializeUtil.serialize(value));
    }

    /**
     * 设置缓存，并且自己指定过期时间
     * @param redisKey 缓存的key对象
     * @param value
     * @param expireTime 过期时间
     */
    public void setWithExpireTime(final RedisKey redisKey, Object value, int expireTime) {
        jedisCluster.setex(redisKey.getRedisKey().getBytes(), expireTime, SerializeUtil.serialize(value));
        LOGGER.debug("RedisUtil:setWithExpireTime cache key={},value={},expireTime={}", redisKey.getRedisKey(), value,
                expireTime);
    }

    /**
     * 设置缓存，并且由配置文件指定过期时间
     * @param redisKey 缓存的key对象
     * @param value
     * @param value
     */
    public void setWithExpireTime(final RedisKey redisKey, Object value) {
        int EXPIRE_SECONDS = redisProperties.getExpireSeconds();
        jedisCluster.setex(redisKey.getRedisKey().getBytes(), EXPIRE_SECONDS, SerializeUtil.serialize(value));
        LOGGER.debug("RedisUtil:setWithExpireTime cache key={},value={},expireTime={}", redisKey.getRedisKey(), value,
                EXPIRE_SECONDS);
    }

    /**
     * 获取指定key的缓存
     * @param redisKey 缓存的key对象
     *
     * @return Object
     */
    public Object get(final RedisKey redisKey) {
        byte[] bytes = jedisCluster.get(redisKey.getRedisKey().getBytes());
        if (StringUtils.isEmpty(bytes)){
            return null;
        }
        Object value = SerializeUtil.unserialize(bytes);
        LOGGER.debug("RedisUtil:get cache key={},value={}", redisKey.getRedisKey(), value);
        return value;
    }

    public String getRaw(final RedisKey redisKey) {
        byte[] bytes = jedisCluster.get(redisKey.getRedisKey().getBytes());
        if (StringUtils.isEmpty(bytes)) {
            return null;
        }


        String value = new String(bytes);

        LOGGER.debug("getRaw from cache key:{}, value:{}", redisKey.getRedisKey(), value);

        return value;
    }

    public void expire(String key, int timeout){
        if (null != jedisCluster) {
            jedisCluster.expire(key.getBytes(), timeout);
        }
    }

    /**
     * 删除指定key的缓存
     * @param redisKey 缓存的key对象
     */
    public void delteKey(final RedisKey redisKey) {
        jedisCluster.del(redisKey.getRedisKey().getBytes());
        LOGGER.debug("RedisUtil:delete cache key={}", redisKey.getRedisKey());
    }

    /**
     * 缓存map对象
     * @param redisKey
     */
    public void hmset(final RedisKey redisKey, Map<byte[], byte[]> map) {
        jedisCluster.hmset(redisKey.getRedisKey().getBytes(), map);
        LOGGER.debug("RedisUtil:hmset cache key={}", redisKey.getRedisKey());
    }


    /**
     * 获取map对象
     * @param redisKey
     */
    public Map<byte[], byte[]> hgetAll(final RedisKey redisKey) {
        Map<byte[], byte[]> map = jedisCluster.hgetAll(redisKey.getRedisKey().getBytes());
        LOGGER.debug("RedisUtil:hmset cache key={}", redisKey.getRedisKey());
        return map;
    }


    /**
     * 校验key是否存在
     *
     * @param redisKey
     */
    public Boolean exists(final RedisKey redisKey) {
        Boolean flag = jedisCluster.exists(redisKey.getRedisKey().getBytes());
        LOGGER.debug("RedisUtil:exists cache key={}", redisKey.getRedisKey());
        return flag;
    }


    /**
     * 获取key的过期时长
     *
     * @param redisKey
     */
    public Long ttl(final RedisKey redisKey) {
        Long ttl = jedisCluster.ttl(redisKey.getRedisKey().getBytes());
        LOGGER.debug("RedisUtil:ttl cache key={}", redisKey.getRedisKey());
        return ttl;
    }


    /**
     * 给geogle中添加一个成员
     *
     * @param redisKey
     */
    public Long geoadd(final RedisKey redisKey, double lon, double lat, byte[] member) {
        Long ttl = jedisCluster.geoadd(redisKey.getRedisKey().getBytes(), lon, lat, member);
        LOGGER.debug("RedisUtil:ttl cache key={}", redisKey.getRedisKey());
        return ttl;
    }
    public void hset(String key, String field, Object value) {
        if (null == jedisCluster){
            return;
        }

        jedisCluster.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
    }
    public Map<String, Object> hget(String key) {
        if (null == jedisCluster) {
            return null;
        }

        Map<byte[], byte[]> map = jedisCluster.hgetAll(key.getBytes());
        Map<String, Object> result = new HashMap<String, Object>();
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            result.put(new String(entry.getKey()), SerializeUtil.unserialize(entry.getValue()));
        }
        return result;
    }

    public List<Object> hvals(String key) {
        if (null == jedisCluster) {
            return null;
        }
        Collection<byte[]> collection = this.jedisCluster.hvals(key.getBytes());
        if (CollectionUtils.isEmpty(collection)){
            return null;
        }
        List<Object> list = new ArrayList<Object>();
        for (byte[] obj : collection) {
            list.add(SerializeUtil.unserialize(obj));
        }
        return list;
    }

    public void hdel(String key, byte[]... bytes) {
        if (null == jedisCluster) {
            return ;
        }
        jedisCluster.hdel(key.getBytes(), bytes);
        LOGGER.debug("redis del{}",key);
    }
    /*以上是一些简单的命令,不同的开发组使用不同命令时,定义自己的的方法*/

    /**
     * 调用lua脚本
     * @param script    lua脚本
     * @param keys      参数列表
     * @param args      值列表
     * @return          返回对象
     */
    public Object eval(String script, List<byte[]> keys, List<byte[]> args){
        if (null == jedisCluster) {
            return null;
        }
        return jedisCluster.eval(script.getBytes(), keys, args);
    }

    public long lpush(String key, String jsonObject){
        if (null == jedisCluster) {
            return -1;
        }
        return jedisCluster.lpush(key.getBytes(), jsonObject.getBytes());
    }

    public long llen(String key){
        if (null == jedisCluster) {
            return -1;
        }
        return jedisCluster.llen(key.getBytes());
    }

    public List<byte[]> lrange(String key, int start, int end){
        if (null == jedisCluster) {
            return null;
        }
        return jedisCluster.lrange(key.getBytes(), start, end);
    }
}
