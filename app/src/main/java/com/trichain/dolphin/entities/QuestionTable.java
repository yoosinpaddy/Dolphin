package com.trichain.dolphin.entities;


import android.content.Intent;

import java.io.Serializable;


public class QuestionTable implements Serializable {
    String questions,players,level;
    int players1,level1;

    public QuestionTable(String questions, String players, String level) {
        this.questions = questions;
        this.players = players;
        this.level = level;
        this.players1= Integer.valueOf(players);
        this.level1=Integer.valueOf(level);
    }
    //getters and setters

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPlayers1() {
        return Integer.valueOf(players);
    }

    public int getLevel1() {
        return Integer.valueOf(level);
    }
}
