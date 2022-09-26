package com.example.gameban.model;

public class SearchHistory {
    private String gameId;
    private String gameName;
    private String gamePrice;
    private double gamePraiseRate;

    public SearchHistory(String gameId, String gameName, String gamePrice, double gamePraiseRate) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gamePrice = gamePrice;
        this.gamePraiseRate = gamePraiseRate;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGamePrice() {
        return gamePrice;
    }

    public void setGamePrice(String gamePrice) {
        this.gamePrice = gamePrice;
    }

    public double getGamePraiseRate() {
        return gamePraiseRate;
    }

    public void setGamePraiseRate(double gamePraiseRate) {
        this.gamePraiseRate = gamePraiseRate;
    }
}
