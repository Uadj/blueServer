package com.example.demo.Scheduler;

import com.example.demo.Ranking.RankingRepository;
import com.example.demo.Ranking.UserRankingDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RankingSyncScheduler {

    private final RankingRepository rankingRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public RankingSyncScheduler(RankingRepository rankingRepository, RedisTemplate<String, String> redisTemplate) {
        this.rankingRepository = rankingRepository;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedDelay = 3000) // 5분 (5분 = 300000 밀리초)
    public void syncRanking() {
        // MySQL에서 최신 랭킹 정보 가져오기
        List<UserRankingDto> topRankingFromMySQL = rankingRepository.getTopRankingFromMySQL(10); // 상위 10개 랭킹 정보 가져오기

        // Redis에 랭킹 정보 업데이트
        rankingRepository.updateRankingInRedis(topRankingFromMySQL);
    }
}
