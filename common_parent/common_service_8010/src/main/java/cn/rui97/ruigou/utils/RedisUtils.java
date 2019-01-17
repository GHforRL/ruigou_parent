package cn.rui97.ruigou.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 获取连接池对象
 */
public enum RedisUtils {
    INSTANCE;
    static JedisPool jedisPool = null;

    static {
        //1 创建连接池配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        //2 进行配置-四个配置
        config.setMaxIdle(1);//最小连接数
        config.setMaxTotal(11);//最大连接数
        config.setMaxWaitMillis(5 * 1000L);//最长等待时间
        config.setTestOnBorrow(true);//测试连接时是否畅通
        //3 通过配置对象创建连接池对象
        jedisPool = new JedisPool(config, "127.0.0.1", 6379, 5 * 1000, "root");
    }

    //获取连接
    public Jedis getSource() {
        return jedisPool.getResource();
    }

    //关闭资源
    public void closeSource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }

    }

    /**
     * 设置字符值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = getSource();
        jedis.set(key, value);
        closeSource(jedis);
    }


    /**
     * 设置字符值
     *
     * @param key
     */
    public String get(String key) {
        Jedis jedis = getSource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSource(jedis);
        }

        return null;

    }
}
