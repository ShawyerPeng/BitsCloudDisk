package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import service.AttendanceService;
import util.redis.BitSetUtils;
import util.redis.JedisUtil;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户某天活跃
     * 1 统计系统中某天用户登录的情况：
     * 以当天日期做为 key，比如 ‘20150410’ ，对应的 bitMap 的 index 用 userId 来标示，如果 id 不是以 0 开头，加上相应的偏移量就 OK 了
     * 如果该天用户登录，调用 activeUser 方法，来更改 bitMap 相应 index 上的标示
     */
    @Override
    public void activeUser(long userId, String dateKey) {
        Jedis jedis = JedisUtil.getResource();
        try {
            jedis.setbit(dateKey, userId, true);
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * 判断某天用户是否活跃
     */
    @Override
    public boolean isActiveUser(long userId, String dateKey) {
        Jedis jedis = JedisUtil.getResource();
        try {
            return jedis.getbit(dateKey, userId);
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * 该天用户登录的数量及登录的用户id
     */
    @Override
    public long totalCountUsers(String dateKey) {
        Jedis jedis = JedisUtil.getResource();
        try {
            return jedis.bitcount(dateKey);
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * 该天登录所有的用户 id
     */
    @Override
    public List<Long> activeUserIds(String dateKey) {
        Jedis jedis = JedisUtil.getResource();
        try {
            if (jedis.get(dateKey) == null) {
                return null;
            }
            BitSet set = BitSetUtils.byteArray2BitSet(jedis.get(dateKey).getBytes());

            List<Long> list = new ArrayList<>();
            for (long i = 0; i < set.size(); i++) {
                if (set.get((int) i)) { // (int)
                    list.add(i);
                }
            }
            return list;
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * 如果我们想统计 n 天内连续登录的用户数及 UserId
     */
    @Override
    public List<Long> continueActiveUserCount(String... dateKeys) {
        Jedis jedis = JedisUtil.getResource();
        try {
            BitSet all = null;
            for (String key : dateKeys) {
                if (jedis.get(key) == null) {
                    continue;
                }
                BitSet set = BitSetUtils.byteArray2BitSet(jedis.get(key).getBytes());
                if (all == null) {
                    all = set;
                }
                System.out.println(set.size());
                all.and(set);
            }
            List<Long> list = new ArrayList<>();
            for (long i = 0; i < all.size(); i++) {
                if (all.get((int) i)) {
                    list.add(i);
                }
            }
            return list;
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }


}
