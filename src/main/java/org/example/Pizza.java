package org.example;

public class Pizza {
    private String name;
    private final Integer preparationTimeInSeconds;
    private final Integer bakeTimeInSeconds;
    private boolean isBaked = false;

    public Pizza(String input) {
        input = input.trim();
        String[] split = input.split(" ");
        name = split[0];
        preparationTimeInSeconds = Integer.parseInt(split[1]);
        bakeTimeInSeconds = Integer.parseInt(split[2]);
    }

    public Integer getPreparationTimeInSeconds() {
        return preparationTimeInSeconds;
    }


    public Integer getBakeTimeInSeconds() {
        return bakeTimeInSeconds;
    }

    public void setIsBaked() {
        this.isBaked = true;
    }

    public boolean getIsBaked() {
        return isBaked;
    }
}
