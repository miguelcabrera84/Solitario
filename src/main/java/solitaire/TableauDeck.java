package solitaire;

import DeckOfCards.CartaInglesa;
import EstructuraDeDatos.ListaSimple;

public class TableauDeck {
    private ListaSimple<CartaInglesa> cartas;

    public TableauDeck() {
        this.cartas = new ListaSimple<>();
    }

    public void agregarCartaInicial(CartaInglesa carta) {
        carta.makeFaceUp();
        cartas.push(carta);
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (cartas.estaVacia()) {
            carta.makeFaceUp();
            cartas.push(carta);
            return true;
        } else {
            CartaInglesa tope = cartas.peek();
            // Regla de columnas si el mismo palo y valor descendente
            if (tope.tieneElMismoPalo(carta.getPalo()) && tope.getValorBajo() == carta.getValorBajo() + 1) {
                carta.makeFaceUp();
                cartas.push(carta);
                return true;
            }
        }
        return false;
    }

    public CartaInglesa removerUltimaCarta() {
        return cartas.pop();
    }

    public CartaInglesa verUltimaCarta() {
        return cartas.peek();
    }

    public boolean estaVacio() {
        return cartas.estaVacia();
    }

    // Permite a la interfaz gráfica consultar las cartas de la columna
    public ListaSimple<CartaInglesa> getCartas() {
        return cartas;
    }
}