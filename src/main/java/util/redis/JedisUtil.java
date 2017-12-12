package util.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

public class JedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    static {
        initPool();
    }

    private static volatile JedisPool jedisPool;
    private static ResourceBundle resourceBundle;

    public static Jedis getResource() {
        return jedisPool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void initPool() {
        if (jedisPool != null) {
            return;
        }
        loadProperties();
        String host = resourceBundle.getString("redis.host");
        int port = Integer.parseInt(resourceBundle.getString("redis.port"));
        JedisPoolConfig config = config();
        jedisPool = new JedisPool(config, host, port, 60, null);
    }

    private static void loadProperties() {
        resourceBundle = ResourceBundle.getBundle("redis");
    }

    private static JedisPoolConfig config() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    public static void main(String[] args) {
        Jedis jedis = JedisUtil.getResource();
        JedisUtil.returnResource(jedis);
    }
}