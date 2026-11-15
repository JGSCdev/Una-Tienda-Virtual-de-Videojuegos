
package Colecciones;

import Modelo.Gestores;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ColeccionGestoresGenerica<T extends Gestores> {

    private final Map<Integer, T> elementos;

    // Constructor que inicializa la colección en memoria
    public ColeccionGestoresGenerica() {
        this.elementos = new LinkedHashMap<>();
    }

    // Agrega un elemento a la colección
    public boolean agregar(T elemento) {

        if (elemento == null) {
            return false;
        }

        int id = elemento.getId();

        // Verifica que el ID sea válido
        if (id <= 0) {
            return false;
        }

        // Comprueba que no exista otro elemento con el mismo ID
        if (elementos.containsKey(id)) {
            return false;
        }

        elementos.put(id, elemento);

        return true;
    }

    // Busca un elemento por su ID dentro de la colección
    public T consultar(int id) {

        if (id <= 0) {
            return null;
        }

        return elementos.get(id);
    }

    // Elimina un elemento de la colección mediante su ID
    public boolean eliminar(int id) {

        if (id <= 0) {
            return false;
        }

        return elementos.remove(id) != null;
    }

    // Devuelve una copia no modificable de los elementos almacenados
    public List<T> listarTodos() {
        return List.copyOf(elementos.values());
    }

    // Carga los elementos recuperados de la base de datos
    public void cargar(List<? extends T> lista) {

        if (lista == null) {
            throw new IllegalArgumentException(
                    "La lista no puede ser nula"
            );
        }

        // Se utiliza un mapa temporal para comprobar los datos antes de cargarlos
        Map<Integer, T> nuevosElementos = new LinkedHashMap<>();

        for (T elemento : lista) {

            if (elemento == null) {
                throw new IllegalArgumentException(
                        "No se pueden cargar elementos nulos"
                );
            }

            int id = elemento.getId();

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "Todos los elementos deben tener un ID válido"
                );
            }

            if (nuevosElementos.containsKey(id)) {
                throw new IllegalArgumentException(
                        "Existen elementos con IDs repetidos"
                );
            }

            nuevosElementos.put(id, elemento);
        }

        // Reemplaza el contenido de la colección por los elementos recuperados
        elementos.clear();
        elementos.putAll(nuevosElementos);
    }
}
