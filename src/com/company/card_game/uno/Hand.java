package com.company.card_game.uno;

import com.company.card_game.actor.Player;
import com.company.card_game.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    public List<Card> cards = new ArrayList<>();
    public Player player;

    public Hand(Player player){
        this.player = player;
    }

    public String displayHand(){
        String top = "";
        String sides = "";
        String bottom = "";
        String output = "";
        String cardNumber = "";
        int cardNum = 1;
        for(Card card : cards){
            top += "_______ ";
            output += "|" + card.display() + "| ";
            sides += "|     | ";
            bottom += "|_____| ";
            if(cardNum < 10)
                cardNumber += "  ("+ cardNum +")   ";
            else
                cardNumber += "  ("+ cardNum +")  ";

            cardNum++;
        }
        return top + "\n" + sides + "\n" + output + "\n" + bottom + "\n" + cardNumber;
    }

    public int getHandSize(){
        return cards.size();
    }

    public void addCardToHand(Card card){
        cards.add(card);
    }
}
