package EstructuraDeDatos;

public class NodoSimple<T> {
    public T dato;
    public NodoSimple<T> siguiente;

    public NodoSimple(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}