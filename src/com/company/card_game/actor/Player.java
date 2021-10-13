package com.company.card_game.actor;

import com.company.card_game.uno.Actor;

public class Player implements Actor {
    public String name;
    public int score;
    public Player(String name){
        this.name = name;
    }
}
