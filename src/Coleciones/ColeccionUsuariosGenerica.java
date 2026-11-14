package Coleciones;
import Modelo.Usuario;

import java.util.*;

public class ColeccionUsuariosGenerica<T extends Usuario> {

    private Map<Integer, T> coleccion = new HashMap<>();

    public boolean agregar(T usuario) {
        if (usuario == null || coleccion.containsKey(usuario.getId())) {
            return false;
        }

        coleccion.put(usuario.getId(), usuario);
        return true;
    }

    public T buscar(Integer id) {
        return coleccion.get(id);
    }

    public boolean eliminar(Integer id) {
        return coleccion.remove(id) != null;
    }

    public boolean existe(Integer id) {
        return coleccion.containsKey(id);
    }

    public Collection<T> obtenerTodos() {
        return Collections.unmodifiableCollection(coleccion.values());
    }

    public int cantidad() {
        return coleccion.size();
    }

    public boolean estaVacia() {
        return coleccion.isEmpty();
    }
}
