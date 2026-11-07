package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import EstructuraDeDatos.ListaSimple;

public class FoundationDeck {
    private Palo palo;

    private ListaSimple<CartaInglesa> cartas;

    public FoundationDeck(Palo palo) {
        this.palo = palo;
        this.cartas = new ListaSimple<>();
    }

    public boolean agregarCarta(CartaInglesa carta) {
        // Debe ser del mismo palo que esta fundación
        if (!carta.tieneElMismoPalo(palo)) {
            return false;
        }

        if (cartas.estaVacia()) {
            // Si está vacía, la carta obligatoriamente debe ser un As (valorBajo = 1)
            if (carta.getValorBajo() == 1) {
                cartas.push(carta);
                return true;
            }
        } else {
            //Si ya hay cartas, debe ser el número que sigue (si hay un 2, debe ser un 3)
            CartaInglesa tope = cartas.peek();
            if (tope.getValorBajo() + 1 == carta.getValorBajo()) {
                cartas.push(carta);
                return true;
            }
        }
        return false;
    }

    // Este metodo es para el boton de desacer
    public CartaInglesa removerUltimaCarta() {
        return cartas.pop();
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.peek();
    }

    public boolean estaVacio() {
        return cartas.estaVacia();
    }
}