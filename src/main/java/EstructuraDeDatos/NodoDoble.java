package EstructuraDeDatos;

public class NodoDoble<T> {
    public T dato;
    public NodoDoble<T> siguiente;
    public NodoDoble<T> anterior;

    public NodoDoble(T dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }
}