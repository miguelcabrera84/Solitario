package solitaire;

import DeckOfCards.CartaInglesa;

public class Reserva {
    // Arreglo de 8 espacios. Si la posición es null, significa que la celda está vacía.
    private CartaInglesa[] celdas;

    public Reserva() {
        celdas = new CartaInglesa[8];
    }

    public boolean estaVacia(int indice) {
        return celdas[indice] == null;
    }

    public boolean agregarCarta(int indice, CartaInglesa carta) {
        if (estaVacia(indice)) {
            celdas[indice] = carta;
            return true;
        }
        return false;
    }

    public CartaInglesa removerCarta(int indice) {
        CartaInglesa carta = celdas[indice];
        celdas[indice] = null;
        return carta;
    }

    public CartaInglesa verCarta(int indice) {
        return celdas[indice];
    }
}