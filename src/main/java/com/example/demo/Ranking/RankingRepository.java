package com.example.demo.Ranking;

import com.example.demo.User.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RankingRepository {

    private final RedisTemplate<String, String> redisTemplate;
    @PersistenceContext
    private final EntityManager entityManager;
    private final static String RANKING_KEY = "ranking";

    public RankingRepository(RedisTemplate<String, String> redisTemplate, EntityManager entityManager) {
        this.redisTemplate = redisTemplate;
        this.entityManager = entityManager;
    }

    public void updateRanking(String username, int score) {
        // 저장된 score 값 가져오기
        User user = entityManager.find(User.class, username);
        int oldScore = (user != null) ? user.getScore() : 0;

        // Redis에 랭킹 정보 업데이트
        redisTemplate.opsForZSet().add(RANKING_KEY, username, score);

        // MySQL에 랭킹 정보 업데이트
        if (user != null) {
            user.setScore(score);
            entityManager.merge(user);
        } else {
            user = new User(username, score);
            entityManager.persist(user);
        }

        // Redis와 MySQL의 score 값을 동기화
        syncScore(username, oldScore, score);
    }

    private void syncScore(String username, int oldScore, int newScore) {
        if (oldScore != newScore) {
            redisTemplate.opsForZSet().add(RANKING_KEY, username, newScore);
        }
    }

    public List<UserRankingDto> getTopRankingFromMySQL(int count) {
        String query = "SELECT u FROM User u ORDER BY u.score DESC";
        List<User> users = entityManager.createQuery(query, User.class)
                .setMaxResults(count)
                .getResultList();

        List<UserRankingDto> topRanking = new ArrayList<>();
        int rank = 1;
        for (User user : users) {
            String username = user.getName();
            int score = user.getScore();
            UserRankingDto userRankingDto = new UserRankingDto(rank++, username, score);
            topRanking.add(userRankingDto);
        }

        return topRanking;
    }

    public void updateRankingInRedis(List<UserRankingDto> topRanking) {
        redisTemplate.opsForZSet().removeRange(RANKING_KEY, 0, -1); // Redis 랭킹 정보 초기화
        for (UserRankingDto userRankingDto : topRanking) {
            redisTemplate.opsForZSet().add(RANKING_KEY, userRankingDto.getName(), userRankingDto.getScore());
        }
    }

    public List<UserRankingDto> getTopRankingFromRedis(int count) {
        Set<ZSetOperations.TypedTuple<String>> rankingSet = redisTemplate.opsForZSet().reverseRangeWithScores(RANKING_KEY, 0, count - 1);

        if (rankingSet == null || rankingSet.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserRankingDto> topRanking = new ArrayList<>();
        int rank = 1;

        for (ZSetOperations.TypedTuple<String> entry : rankingSet) {
            String username = entry.getValue();
            double score = entry.getScore();
            UserRankingDto userRankingDto = new UserRankingDto(rank++, username, (int) score);
            topRanking.add(userRankingDto);
        }

        return topRanking;
    }

    public int getUserRank(String username) {
        Long rank = redisTemplate.opsForZSet().reverseRank(RANKING_KEY, username);
        return rank != null ? rank.intValue() + 1 : -1;
    }
}
