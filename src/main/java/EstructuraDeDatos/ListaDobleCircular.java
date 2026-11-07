package EstructuraDeDatos;

public class ListaDobleCircular<T> {
    private NodoDoble<T> cabeza;
    private int tamanio;

    public ListaDobleCircular() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    // Inserta un dato al final del círculo
    public void insertar(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);

        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
            cabeza.anterior = cabeza;
        } else {
            NodoDoble<T> ultimo = cabeza.anterior; // El anterior a la cabeza es el último

            nuevo.siguiente = cabeza;
            nuevo.anterior = ultimo;

            cabeza.anterior = nuevo;
            ultimo.siguiente = nuevo;
        }
        tamanio++;
    }

    // Remueve y entrega el nodo que está en la cabeza (Para repartir cartas)
    public T removerFrente() {
        if (estaVacia()) {
            return null;
        }

        T dato = cabeza.dato;

        if (tamanio == 1) { // Si solo queda una carta
            cabeza = null;
        } else {
            NodoDoble<T> ultimo = cabeza.anterior;
            NodoDoble<T> segunda = cabeza.siguiente;

            segunda.anterior = ultimo;
            ultimo.siguiente = segunda;
            cabeza = segunda; // La segunda carta ahora es la cabeza
        }
        tamanio--;
        return dato;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int getTamanio() {
        return tamanio;
    }
}