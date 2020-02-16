package com.trichain.dolphin.model;

public class Player {
    private String name;
    private int interestedIn; /*0=boys, 1=girls, 2=both*/
    private int gender;

    public Player(String name, int interestedIn, int gender) {
        this.name = name;
        this.interestedIn = interestedIn;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(int interestedIn) {
        this.interestedIn = interestedIn;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
