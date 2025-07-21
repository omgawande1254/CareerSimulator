package com.careersim.model;

public class PlayerStats {
    public int userId;
    public int money;
    public int knowledge;
    public int energy;
    public int stress;
    public int reputation;
    public String careerTitle;

    public PlayerStats(int userId, int money, int knowledge, int energy, int stress, int reputation, String careerTitle) {
        this.userId = userId;
        this.money = money;
        this.knowledge = knowledge;
        this.energy = energy;
        this.stress = stress;
        this.reputation = reputation;
        this.careerTitle = careerTitle;
    }

    public void applyChanges(int moneyChange, int knowledgeChange) {
        this.money += moneyChange;
        this.knowledge += knowledgeChange;
    }

    public void displayStats() {
        System.out.println("\n💼 Stats:");
        System.out.println("Money: " + money);
        System.out.println("Knowledge: " + knowledge);
        System.out.println("Energy: " + energy);
        System.out.println("Stress: " + stress);
        System.out.println("Reputation: " + reputation);
        System.out.println("Career Title: " + careerTitle);
    }
}