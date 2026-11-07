package solitaire;

import DeckOfCards.CartaInglesa;

public class Movimiento {
    public CartaInglesa carta;
    public String origen;
    public String destino;

    public Movimiento(CartaInglesa carta, String origen, String destino) {
        this.carta = carta;
        this.origen = origen;
        this.destino = destino;
    }
}