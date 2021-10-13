package com.company.card_game.deck;

import com.company.card_game.uno.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnoDeck implements Deck {
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\033[0;93m";
    private static final String BLUE = "\u001B[34m";
    public List<Card> cardList = new ArrayList<>();
    final private int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    final private String[] colors = {RED, GREEN, YELLOW, BLUE};

    public UnoDeck(){
        for(String color : colors){
            for(int val : values){
                if(val == 0)
                    cardList.add(new Card(val, color));
                if(val > 0 && val < 13){
                    cardList.add(new Card(val, color));
                    cardList.add(new Card(val, color));
                }
                if(val > 12){
                    cardList.add(new Card(val, ""));
                }
            }
        }
    }

    public String[] getColors(){return colors;}

    @Override
    public void shuffle() {
        Collections.shuffle(cardList);
    }

    @Override
    public Card deal() {
        if(cardList.size() <= 1){
            reshuffle();
        }
        return (cardList.remove((cardList.size() - 1)));
    }

    public void reshuffle(){
        for(int i = 0; i < (Table.discardPile.size() - 1); i++){
            cardList.add(Table.discardPile.remove(i));
            Collections.shuffle(cardList);
        }
    }
}
