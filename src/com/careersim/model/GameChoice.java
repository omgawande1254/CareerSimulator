package com.careersim.model;

public class GameChoice {
    public int choiceId;
    public int stage;
    public String description;
    public String optionA;
    public String optionB;
    public String optionC;

    public GameChoice(int choiceId, int stage, String description, String optionA, String optionB, String optionC) {
        this.choiceId = choiceId;
        this.stage = stage;
        this.description = description;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
    }
}