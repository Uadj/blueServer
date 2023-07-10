package com.example.demo.Ranking;

public class UserRankingDto {
    private int rank;
    private String name;
    private int score;

    public UserRankingDto() {
    }

    public UserRankingDto(int rank, String name, int score) {
        this.rank = rank;
        this.name = name;
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

