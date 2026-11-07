package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import DeckOfCards.Palo;
import EstructuraDeDatos.ListaSimple;

public class JuegoEightOff {
    private TableauDeck[] tableaus;
    private FoundationDeck[] fundaciones;
    private Reserva reserva;
    private Mazo mazo;

    // Pila para guardar el historial de movimientos
    private ListaSimple<Movimiento> historial;

    public JuegoEightOff() {
        tableaus = new TableauDeck[8];
        fundaciones = new FoundationDeck[4];
        reserva = new Reserva();
        historial = new ListaSimple<>();
        mazo = new Mazo();

        // Inicializamos los 8 Tableaus
        for (int i = 0; i < 8; i++) {
            tableaus[i] = new TableauDeck();
        }

        // Inicializamos las 4 Fundaciones
        Palo[] palos = Palo.values();
        for (int i = 0; i < 4; i++) {
            fundaciones[i] = new FoundationDeck(palos[i]);
        }

        repartirCartas();
    }

    private void repartirCartas() {
        // 48 cartas van a las columnas
        for (int i = 0; i < 48; i++) {
            CartaInglesa carta = mazo.obtenerUnaCarta();
            int columnaDestino = i % 8;
            tableaus[columnaDestino].agregarCartaInicial(carta);
        }

        // Las 4 cartas restantes van a las reservas (boca arriba)
        for (int i = 0; i < 4; i++) {
            CartaInglesa carta = mazo.obtenerUnaCarta();
            carta.makeFaceUp();
            reserva.agregarCarta(i, carta);
        }
    }


    public TableauDeck[] getTableaus() {
        return tableaus;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public FoundationDeck[] getFundaciones() {
        return fundaciones;
    }

    // MOVIMIENTO: COLUMNA A COLUMNA (De uno en uno con apoyo de reservas)
    public boolean moverDeTableauATableau(int indiceOrigen, int indiceDestino) {
        TableauDeck tOrigen = tableaus[indiceOrigen];
        TableauDeck tDestino = tableaus[indiceDestino];

        if (tOrigen.estaVacio()) return false;

        CartaInglesa cartaTopeOrigen = tOrigen.verUltimaCarta();

        // Si la columna destino está vacía, podemos mover la carta directamente
        if (tDestino.estaVacio()) {
            tOrigen.removerUltimaCarta();
            tDestino.agregarCarta(cartaTopeOrigen);
            historial.push(new Movimiento(cartaTopeOrigen, "T" + indiceOrigen, "T" + indiceDestino));
            return true;
        }

        CartaInglesa cartaTopeDestino = tDestino.verUltimaCarta();

        // Validar si la carta del origen embona perfectamente con el destino (mismo palo y valor descendente)
        if (cartaTopeDestino.tieneElMismoPalo(cartaTopeOrigen.getPalo()) &&
                cartaTopeDestino.getValorBajo() == cartaTopeOrigen.getValorBajo() + 1) {

            tOrigen.removerUltimaCarta();
            tDestino.agregarCarta(cartaTopeOrigen);
            historial.push(new Movimiento(cartaTopeOrigen, "T" + indiceOrigen, "T" + indiceDestino));
            return true;
        }

        return false;
    }

    // MOVIMIENTO: COLUMNA A RESERVA
    public boolean moverDeTableauAReserva(int indiceTableau, int indiceReserva) {
        TableauDeck origen = tableaus[indiceTableau];

        if (origen.estaVacio()) return false;
        if (!reserva.estaVacia(indiceReserva)) return false;

        CartaInglesa cartaAMover = origen.removerUltimaCarta();
        cartaAMover.makeFaceUp();
        reserva.agregarCarta(indiceReserva, cartaAMover);
        historial.push(new Movimiento(cartaAMover, "T" + indiceTableau, "R" + indiceReserva));
        return true;
    }

    // MOVIMIENTO: RESERVA A COLUMNA
    public boolean moverDeReservaATableau(int indiceReserva, int indiceTableau) {
        if (reserva.estaVacia(indiceReserva)) return false;

        CartaInglesa cartaAMover = reserva.verCarta(indiceReserva);
        TableauDeck destino = tableaus[indiceTableau];

        if (destino.agregarCarta(cartaAMover)) {
            reserva.removerCarta(indiceReserva);
            historial.push(new Movimiento(cartaAMover, "R" + indiceReserva, "T" + indiceTableau));
            return true;
        }
        return false;
    }

    // MOVIMIENTO: COLUMNA A FUNDACIÓN
    public boolean moverDeTableauAFundacion(int indiceTableau) {
        TableauDeck origen = tableaus[indiceTableau];
        if (origen.estaVacio()) return false;

        CartaInglesa cartaAMover = origen.verUltimaCarta();
        int indiceFundacion = cartaAMover.getPalo().ordinal();
        FoundationDeck destino = fundaciones[indiceFundacion];

        if (destino.agregarCarta(cartaAMover)) {
            origen.removerUltimaCarta();
            historial.push(new Movimiento(cartaAMover, "T" + indiceTableau, "F" + indiceFundacion));
            return true;
        }
        return false;
    }

    // MOVIMIENTO: RESERVA A FUNDACIÓN
    public boolean moverDeReservaAFundacion(int indiceReserva) {
        if (reserva.estaVacia(indiceReserva)) return false;

        CartaInglesa cartaAMover = reserva.verCarta(indiceReserva);
        int indiceFundacion = cartaAMover.getPalo().ordinal();
        FoundationDeck destino = fundaciones[indiceFundacion];

        if (destino.agregarCarta(cartaAMover)) {
            reserva.removerCarta(indiceReserva);
            historial.push(new Movimiento(cartaAMover, "R" + indiceReserva, "F" + indiceFundacion));
            return true;
        }
        return false;
    }

    // BOTÓN DESHACER
    public void deshacerUltimoMovimiento() {
        if (historial.estaVacia()) return;

        Movimiento ultimoMov = historial.pop();
        String origen = ultimoMov.origen;
        String destino = ultimoMov.destino;
        CartaInglesa carta = ultimoMov.carta;

        char tipoDestino = destino.charAt(0);
        int indiceDestino = Character.getNumericValue(destino.charAt(1));

        if (tipoDestino == 'T') tableaus[indiceDestino].removerUltimaCarta();
        else if (tipoDestino == 'R') reserva.removerCarta(indiceDestino);
        else if (tipoDestino == 'F') fundaciones[indiceDestino].removerUltimaCarta();

        char tipoOrigen = origen.charAt(0);
        int indiceOrigen = Character.getNumericValue(origen.charAt(1));

        if (tipoOrigen == 'T') tableaus[indiceOrigen].agregarCartaInicial(carta);
        else if (tipoOrigen == 'R') {
            carta.makeFaceUp();
            reserva.agregarCarta(indiceOrigen, carta);
        }
    }

    // Metodo de Pista
    public String darPista() {
        for (int i = 0; i < 8; i++) {
            if (!tableaus[i].estaVacio()) {
                CartaInglesa c = tableaus[i].verUltimaCarta();
                int fIdx = c.getPalo().ordinal();
                FoundationDeck f = fundaciones[fIdx];

                if ((f.estaVacio() && c.getValorBajo() == 1) ||
                        (!f.estaVacio() && f.getUltimaCarta().getValorBajo() + 1 == c.getValorBajo())) {
                    return "Pista: Puedes mover la carta " + c + " de la columna " + (i + 1) + " a su fundación.";
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            if (!reserva.estaVacia(i)) {
                CartaInglesa c = reserva.verCarta(i);
                int fIdx = c.getPalo().ordinal();
                FoundationDeck f = fundaciones[fIdx];

                if ((f.estaVacio() && c.getValorBajo() == 1) ||
                        (!f.estaVacio() && f.getUltimaCarta().getValorBajo() + 1 == c.getValorBajo())) {
                    return "Pista: Puedes mover la carta " + c + " de la reserva " + (i + 1) + " a su fundación.";
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            if (!tableaus[i].estaVacio()) {
                CartaInglesa c = tableaus[i].verUltimaCarta();
                for (int j = 0; j < 8; j++) {
                    if (i != j) {
                        TableauDeck dest = tableaus[j];
                        if (dest.estaVacio() ||
                                (dest.verUltimaCarta().tieneElMismoPalo(c.getPalo()) &&
                                        dest.verUltimaCarta().getValorBajo() == c.getValorBajo() + 1)) {
                            return "Pista: Puedes mover la carta " + c + " de la columna " + (i + 1) + " a la columna " + (j + 1) + ".";
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            if (!reserva.estaVacia(i)) {
                CartaInglesa c = reserva.verCarta(i);
                for (int j = 0; j < 8; j++) {
                    TableauDeck dest = tableaus[j];
                    if (dest.estaVacio() ||
                            (dest.verUltimaCarta().tieneElMismoPalo(c.getPalo()) &&
                                    dest.verUltimaCarta().getValorBajo() == c.getValorBajo() + 1)) {
                        return "Pista: Puedes mover la carta " + c + " de la reserva " + (i + 1) + " a la columna " + (j + 1) + ".";
                    }
                }
            }
        }

        return "No hay movimientos sugeridos disponibles en este momento.";
    }
}