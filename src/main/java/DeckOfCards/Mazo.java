package DeckOfCards;

import EstructuraDeDatos.ListaDobleCircular;
import java.util.Random;

public class Mazo {
    private ListaDobleCircular<CartaInglesa> cartas;

    public Mazo() {
        cartas = new ListaDobleCircular<>();
        llenar();
        mezclar();
    }


    //Para repartir la carta de hasta arriba
    public CartaInglesa obtenerUnaCarta() {
        if (!cartas.estaVacia()) {
            return cartas.removerFrente();
        }
        return null;
    }

    private void llenar() {
        // Recorre del 2 al 14 (Donde 11=J, 12=Q, 13=K, 14=A)
        for (int i = 2; i <= 14; i++) {
            for (Palo palo : Palo.values()) {
                CartaInglesa c = new CartaInglesa(i, palo, palo.getColor());
                cartas.insertar(c);
            }
        }
    }

    private void mezclar() {
        int cantidad = cartas.getTamanio();
        if (cantidad == 0) return;

        // Vaciamos la lista circular en un arreglo temporal
        CartaInglesa[] arregloTemporal = new CartaInglesa[cantidad];
        for (int i = 0; i < cantidad; i++) {
            arregloTemporal[i] = cartas.removerFrente();
        }

        // Revolvemos el arreglo temporal al azar
        Random rnd = new Random();
        for (int i = cantidad - 1; i > 0; i--) {
            int indiceAlAzar = rnd.nextInt(i + 1);
            // Intercambiamos las posiciones
            CartaInglesa temp = arregloTemporal[indiceAlAzar];
            arregloTemporal[indiceAlAzar] = arregloTemporal[i];
            arregloTemporal[i] = temp;
        }

        // Volvemos a meter las cartas ya revueltas a nuestra lista circular
        for (int i = 0; i < cantidad; i++) {
            cartas.insertar(arregloTemporal[i]);
        }
    }

    // Retorna la estructura para que el Juego la pueda usar si es necesario
    public ListaDobleCircular<CartaInglesa> getCartas() {
        return cartas;
    }
}