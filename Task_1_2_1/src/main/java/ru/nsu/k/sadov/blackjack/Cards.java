package ru.nsu.k.sadov.blackjack;

public class Card {

    private Suit suit;
    private Value val;

    public Cards(Suit suit, Value val){
        this.suit = suit;
        this.val = val;
    }

    public String toStr(){
        return this.suit.toString() + this.val.toString();
    }

    public Value getVal(){
        return this.val;
    }
}
