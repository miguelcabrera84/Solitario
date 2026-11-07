package EstructuraDeDatos;

public class ListaSimple<T> {
    private NodoSimple<T> tope;
    private int tamanio;

    public ListaSimple() {
        this.tope = null;
        this.tamanio = 0;
    }

    public void push(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamanio++;
    }

    public T pop() {
        if (estaVacia()) return null;
        T dato = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return dato;
    }

    public T peek() {
        if (estaVacia()) return null;
        return tope.dato;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public NodoSimple<T> getTope() {
        return tope;
    }
}