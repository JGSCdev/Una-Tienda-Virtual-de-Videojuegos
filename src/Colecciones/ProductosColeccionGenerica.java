package Colecciones;
import Modelo.Producto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collection;

public class ProductosColeccionGenerica<T extends Producto> {

    private Map<Integer, T> elementos;

    public ProductosColeccionGenerica() {
        elementos = new LinkedHashMap<>();
    }

    // Agregar un elemento
    public boolean agregar(T elemento) {

        if (elemento == null || elemento.getId() <= 0) {
            return false;
        }

        if (elementos.containsKey(elemento.getId())) {
            return false;
        }

        elementos.put(elemento.getId(), elemento);

        return true;
    }

    // Buscar un elemento por ID
    public T buscar(int id) {
        return elementos.get(id);
    }

    // Actualizar un elemento
    public boolean actualizar(T elemento) {

        if (elemento == null || elemento.getId() <= 0) {
            return false;
        }

        if (!elementos.containsKey(elemento.getId())) {
            return false;
        }

        elementos.put(elemento.getId(), elemento);

        return true;
    }

    // Eliminar un elemento por ID
    public boolean eliminar(int id) {
        return elementos.remove(id) != null;
    }

    // Verificar si existe un elemento
    public boolean contiene(int id) {
        return elementos.containsKey(id);
    }

    // Listar todos los elementos
    public Collection<T> listarTodos() {
        return elementos.values();
    }

    // Obtener la cantidad de elementos
    public int cantidad() {
        return elementos.size();
    }

    // Verificar si está vacía
    public boolean estaVacia() {
        return elementos.isEmpty();
    }

    // Vaciar la colección
    public void vaciar() {
        elementos.clear();
    }

}
