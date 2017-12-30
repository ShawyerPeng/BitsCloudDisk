package controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import util.redis.JedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 排行榜
 */
public class RankController {
    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        Jedis jedis = JedisUtil.getResource();

        try {

        } finally {
            JedisUtil.returnResource(jedis);
        }
        // 排行榜
        String key = "奔跑吧～";
        jedis.del(key);

        List<String> playerList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            playerList.add(UUID.randomUUID().toString());
        }

        System.out.println(" 输入所有玩家 ");

        for (int i = 0; i < playerList.size(); i++) {
            // 随机生成数字，模拟玩家的游戏得分
            int score = (int) (Math.random() * 5000);
            String member = playerList.get(i);
            System.out.println(" 玩家 ID：" + member + "， 玩家得分: " + score);
            // 将玩家的 ID 和得分，都加到对应 key 的 SortedSet 中去
            jedis.zadd(key, score, member);
        }


        // 输出打印全部玩家排行榜
        System.out.println();
        System.out.println(" " + key);
        System.out.println(" 全部玩家排行榜 ");

        Set<Tuple> scoreList = jedis.zrevrangeWithScores(key, 0, -1);
        for (Tuple item : scoreList) {
            System.out.println(" 玩家 ID：" + item.getElement() + "， 玩家得分:" + Double.valueOf(item.getScore()).intValue());
        }
        // 输出打印 Top5 玩家排行榜
        System.out.println();
        System.out.println(" " + key);
        System.out.println(" Top 玩家 ");
        scoreList = jedis.zrevrangeWithScores(key, 0, 4);
        for (Tuple item : scoreList) {
            System.out.println(" 玩家 ID：" + item.getElement() + "， 玩家得分:" + Double.valueOf(item.getScore()).intValue());
        }
        // 输出打印特定玩家列表
        System.out.println();
        System.out.println(" " + key);
        System.out.println(" 积分在 1000 至 2000 的玩家 ");
        // 从对应 key 的 SortedSet 中获取已经积分在 1000 至 2000 的玩家列表
        scoreList = jedis.zrangeByScoreWithScores(key, 1000, 2000);
        for (Tuple item : scoreList) {
            System.out.println(" 玩家 ID：" + item.getElement() + "， 玩家得分:" + Double.valueOf(item.getScore()).intValue());
        }

        model.addAttribute("message", "Hello world!");
        return "hello";
    }
}