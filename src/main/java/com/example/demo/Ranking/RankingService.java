package com.example.demo.Ranking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {
    private final RankingRepository rankingRepository;

    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public void updateRanking(String username, int score) {
        rankingRepository.updateRanking(username, score);
    }

    public List<UserRankingDto> getTopRanking(int count) {
        return rankingRepository.getTopRankingFromRedis(count);
    }

    public int getUserRank(String username) {
        return rankingRepository.getUserRank(username);
    }
}
