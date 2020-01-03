package com.leyou.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
@Component
public class RedisClient {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据key取值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据key批量取值
     *
     * @param keys
     * @return
     */
    public List<String> mget(Collection<String> keys) {
        try {
            return redisTemplate.opsForValue().multiGet(keys);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 比较2个可以对应set的不同。
     * <p>
     * 在otherKeys中的value值，在key里面没有的
     */
    public Set<String> sDiff(String key, String otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * 将otherkey里面有，key里面没有的数据放到destkey里面。
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return 放到destkey里面的个数
     */
    public long sdiffAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    /**
     * 将key和otherkey中的数据取并集放入目标destkey里面。
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public long unionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * 计算2个key对应的交集
     *
     * @param key
     * @param otherKey
     * @return
     */
    public Set<String> intersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public boolean set(String key, String value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean setNX(final String key, final String value) {
        try {
            return redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                    byte[] byteKey = redisSerializer.serialize(key);
                    byte[] byteVal = redisSerializer.serialize(value);
                    return connection.setNX(byteKey, byteVal);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean setNEX(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 设置值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hashSet(String key, String field, String value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * 取值
     *
     * @param key
     * @param field
     * @return
     */
    public Object hashGet(String key, String field) {
        try {
            return redisTemplate.opsForHash().get(key, field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new HashMap();
        }
    }

    /**
     * 获取所有的数据
     *
     * @param key
     * @return
     */
    public Map<String, String> hashGetAll(String key) {
        try {
            HashOperations<String, String, String> ops = redisTemplate.opsForHash();
            return ops.entries(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new HashMap();
        }
    }
    /**
     * 设置值
     *
     * @param key
     * @param field
     * @return
     */
    public Object setAdd(String key, String field) {
        try {
            return redisTemplate.opsForSet().add(key, field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 批量添加
     */
    public Object setAdd(String key, String[] field) {
        try {
            return redisTemplate.opsForSet().add(key, field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public Boolean expireAt(String key, final long timeout) {
        try {
            return redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public Set<String> setGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public long setDel(String key, String[] field) {
        try {
            return redisTemplate.opsForSet().remove(key, field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }

    /**
     * 根据key来删除
     */
    public Boolean deleteOnlyByKey(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除一组keys数据
     *
     * @param keys
     * @return
     */
    public boolean delteByKeys(Collection<String> keys) {
        try {
            redisTemplate.delete(keys);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public void setScan() {
        try {
            Iterator<String> it = redisTemplate.opsForSet().scan("relation_device_token", ScanOptions.scanOptions().count(1).build());
            while (it.hasNext()) {
                String value = it.next();
                System.out.println(value);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 有序集合
     *
     * @param key
     * @param member
     * @param score
     * @return
     */
    public boolean zadd(String key, String member, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, member, score);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public void zrem(String key,String member){
        redisTemplate.opsForZSet().remove(key,member);
    }

    public Set<ZSetOperations.TypedTuple<String>> zrevrange(String key, long start, long stop){
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, start, stop);

    }
    /**
     * 从队列右边入队一个元素
     *
     * @param key
     * @param value
     * @return
     */
    public long rpush(String key, String value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }

    /**
     * 从列表中获取指定返回的元素
     *
     * @param key
     * @param begin
     * @param end
     * @return
     */
    public List<String> lRang(String key, int begin, int end) {
        try {
            return redisTemplate.opsForList().range(key, begin, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从列表中删除元素
     *
     * @param key
     * @param count
     * @param value
     */
    public long lRem(String key, long count, String value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }

    //判断当前key是否存在于redis里面
    public boolean isContainsKey(String specialMsgCheckKey) {
        try {
            return redisTemplate.hasKey(specialMsgCheckKey);
        } catch (Exception e) {
            return true;
        }

    }

    public long differenceAndUnion(String tempKey, String fromKey, String destKey) {
        try {
            return redisTemplate.opsForSet().unionAndStore(tempKey, fromKey, destKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }


    /**
     * 判断 member 元素是否集合 key 的成员。
     *
     * @param member
     * @param key
     * @return true存在 false不存在
     */
    public boolean isMemberOfSet(String member, String key) {

        try {
            return this.redisTemplate.opsForSet().isMember(key, member);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 键值自增
     *
     * @param key
     * @param growthLength
     */
    public String incr(String key, Long growthLength) {
        try {
            return redisTemplate.opsForValue().increment(key, growthLength).toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 获取所有list的数据
     *
     * @param key
     * @return
     */
    public List<String> listGetAll(String key) {
        return this.listGet(key, 0, -1);
    }

    /**
     * redis list类型的获取
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> listGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList();
        }
    }


}