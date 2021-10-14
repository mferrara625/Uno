package com.company.card_game.uno;

import com.company.card_game.actor.Player;
import com.company.card_game.deck.Card;
import com.company.card_game.deck.UnoDeck;
import com.company.utils.Console;

import java.util.ArrayList;
import java.util.List;

public class Table {
//     TODO: only allow wild draw 4 if no other options available  || ****implement challenge draw4 feature****
     private List<Hand> hands = new ArrayList<>();
     public static List<Card> discardPile = new ArrayList<>();
     private UnoDeck deck = new UnoDeck();
     private int playerCount;
     private int botCount;
     private boolean canEndTurn = false;
     private boolean isReversed = false;
     private boolean wasSkipped = false;
     private boolean isDraw2 = false;
     private boolean isDraw4 = false;
     private boolean didWin = false;
     private boolean triedAuto = false;
     private boolean hasBot = false;



     public Table() {
          playerCount = Console.getInt("How many players? (1 - 6)", 1, 6, "Invalid player selection");
          botCount = Console.getInt("How many bots? (0 - "+(6 - playerCount)+")", 0, (6 - playerCount), "Invalid bot selection");
          for (int count = 0; count < playerCount; count++) {
               Player newPlayer = new Player("Player " + (count + 1));
               hands.add(new Hand(newPlayer));
          }
          if(botCount > 0){
               for (int count = 0; count < botCount; count++) {
                    Player newPlayer = new Player("Bot " + (count + 1));
                    hands.add(new Hand(newPlayer));
               }
               hasBot = true;
          }
     }

     public void deal(){
          deck.shuffle();
          discardPile.add(deck.deal());
          if(discardPile.get(discardPile.size() - 1).getRank() > 9){
               deck.cardList.add(discardPile.remove(0));
               deal();
          }
          for(Hand hand : hands){
               while(hand.getHandSize() < 7){
                    hand.addCardToHand(deck.deal());
               }
          }

     }


     public List getHands(){
          return hands;
     }

     public String showDiscardPile(){
          String top = "_______ ";
          String output = "|" + discardPile.get(discardPile.size() - 1).display() + "| ";
          String sides = "|     | ";
          String bottom = "|_____| ";

        return "Current Card\n" + top + "\n" + sides + "\n" + output + "\n" + bottom;
     }

     public void turn(Hand hand){
          System.out.println(showDiscardPile());
          System.out.println("\n" + hand.player.name + "'s Hand");
          System.out.println(hand.displayHand());
          int choice = 0;
          if(wasSkipped){
               while(isDraw2){
                    hand.addCardToHand(deck.deal());
                    hand.addCardToHand(deck.deal());
                    isDraw2 = false;
                    if(isDraw4){
                         isDraw2 = true;
                         isDraw4 = false;
                    }
               }
               canEndTurn = true;
               choice = 3;
               wasSkipped = false;
          }
          else if (hand.player.name.contains("Bot ")){
               if(!triedAuto){
                    int selectedCard = autoPlay(hand);
                    if(selectedCard > 0){
                         playCard(hand);
                    } else{
                         hand.addCardToHand(deck.deal());
                    }
               }
               else{
                    hand.addCardToHand(deck.deal());
               }
               canEndTurn = true;
               choice = 3;
          }
          else
          choice = Console.getInt("What would you like to do? \n1. Play a card\n2. Draw a card\n3. End Turn", 1, 3, "Invalid selection");
          if(choice == 1){
               playCard(hand);
          } else if(choice == 2){
               canEndTurn = true;
               hand.addCardToHand(deck.deal());
               turn(hand);
          } else if(choice == 3){
               if(canEndTurn)
                    canEndTurn = false;
               else turn(hand);
          }
     }

     public void playCard(Hand hand){
          int cardSelection;
          if(hand.player.name.contains("Bot ")){
               cardSelection = autoPlay(hand);
          }
          else
          cardSelection = Console.getInt("Pick a card to play or press \"0\" to go back", 0, hand.cards.size(), "Invalid Selection");
          if(cardSelection == 0)
               turn(hand);
               else if((hand.cards.get(cardSelection - 1).getRank() == discardPile.get(discardPile.size() - 1).getRank()) || ((hand.cards.get(cardSelection - 1).getColor().equals(discardPile.get(discardPile.size() - 1).getColor()))) || hand.cards.get(cardSelection - 1).getRank() > 12){
                    discardPile.add(hand.cards.remove(cardSelection - 1));
                    if(discardPile.get(discardPile.size() - 1).getRank() == 10){
                         wasSkipped = true;
                    }
                    if(discardPile.get(discardPile.size() - 1).getRank() == 11){
                         isReversed = !isReversed;
                         if(playerCount < 3)
                              wasSkipped = true;
                    }
                    if(discardPile.get(discardPile.size() - 1).getRank() == 12){
                         wasSkipped = true;
                         isDraw2 = true;
                    }
                    if(discardPile.get(discardPile.size() - 1).getRank() > 12){
                         int colorSelection;
                         if(hand.player.name.contains("Bot ")){
                            colorSelection = (int)(Math.random() * 4) + 1;
                         }
                         else
                         colorSelection = Console.getInt("Select a Color: \n1. Red\n2. Green\n3. Yellow\n4. Blue", 1, 4, "Invalid Selection");
                         discardPile.get(discardPile.size() - 1).color = deck.getColors()[colorSelection - 1];
                         if(discardPile.get(discardPile.size() - 1).getRank() == 14){
                              wasSkipped = true;
                              isDraw2 = true;
                              isDraw4 = true;
                         }
                    }
               }
               else playCard(hand);

          if(hand.cards.size() == 0)
               didWin = true;
     }

     public void playRound(){
          deal();
          for(int i = 0; i < hands.size();){
               turn(hands.get(i));
               triedAuto = false;
               if(didWin){
                    hands.get(i).player.score += calculateScore();
                    System.out.println("\n\t           " + hands.get(i).player.name + " Wins This Round!");
                    System.out.println("\n\t                  + " + calculateScore() + " PTS!");

                    if(hands.get(i).player.score > 500)
                         System.out.println("\n\t           " + hands.get(i).player.name + " Wins The GAME!!!\n");
                    System.out.println("#################################################");
                    for(Hand hand : hands){
                         System.out.println(hand.player.name + " * Score: " + hand.player.score + "\n" + hand.displayHand() + "\n");
                    }
                    break;
               }
               if(isReversed && (playerCount + botCount) > 2){
                    if(i <= 0){
                         i = hands.size();
                    }
                    i--;
               }
               else{
                    if(i >= hands.size() - 1){
                         i = -1;
                    }
                    i++;
               }
          }

     }

     public void playGame(){
          boolean isActive = true;
          while(isActive){
               playRound();
               didWin = false;
               isReversed = false;
               wasSkipped = false;
               isDraw2 = false;
               isDraw4 = false;
               for(Hand hand : hands){
                    deck.cardList.addAll(hand.cards);
                    hand.cards.clear();
                    if(hand.player.score >= 500){
                         isActive = false;
                    }
               }
          }
     }

     public int calculateScore(){
          int playerScore = 0;
          for(Hand hand : hands){
               for (Card card : hand.cards){
                    if(card.getRank() < 10){
                         playerScore += card.getRank();
                    } else if (card.getRank() >= 10 && card.getRank() <= 12){
                         playerScore += 20;
                    } else {
                         playerScore += 50;
                    }
               }
          }
          return playerScore;
     }

     public int autoPlay(Hand hand){
          int result = 0;
          for(int i = 0; i < hand.cards.size(); i++){
               if((hand.cards.get(i).getRank() == discardPile.get(discardPile.size() - 1).getRank()) || ((hand.cards.get(i).getColor().equals(discardPile.get(discardPile.size() - 1).getColor()))) || hand.cards.get(i).getRank() > 12){
                    result = (i + 1);
                    triedAuto = true;
               }
          }
          return result;
     }

}
