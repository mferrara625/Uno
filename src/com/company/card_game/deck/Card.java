package com.company.card_game.deck;

public class Card {
private int rank;
public String color;
public static final String ANSI_RESET = "\u001B[0m";

    public Card(int rank, String color){
        this.rank = rank;
        this.color = color;
    }

    public int getRank(){return rank;}

    public String getColor(){return color;}


    public String display(){
        String output = "";
        switch(rank){
            case 10 -> output = " Skp ";
            case 11 -> output = " Rev ";
            case 12 -> output = " +2  ";
            case 13 -> output = " Wld ";
            case 14 -> output = " W+4 ";
            default -> output = "  " + rank + "  ";
        }
        return color + output + ANSI_RESET;
    }
}
