package com.careersim.model;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private int currentStage;
    private boolean isActiveGame;

    public User(int userId, String username, String passwordHash, int currentStage, boolean isActiveGame) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.currentStage = currentStage;
        this.isActiveGame = isActiveGame;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getCurrentStage() { return currentStage; }
    public void setCurrentStage(int stage) { this.currentStage = stage; }
}