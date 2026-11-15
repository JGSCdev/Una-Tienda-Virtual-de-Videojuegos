package Service;

import Modelo.Juego;
import Persistencia.JuegoDAO;

import java.util.*;

public class JuegoService {

    private final JuegoDAO juegoDAO;
    private final Map<Integer, Juego> repositorioMemoria;

    // Constructor: inicializa el DAO y la caché en memoria, precargando los juegos de la BD.
    public JuegoService() {
        this.juegoDAO = new JuegoDAO();
        this.repositorioMemoria = new HashMap<>();
        cargarJuegosDesdeBD();
    }

    private void cargarJuegosDesdeBD() {
        List<Juego> juegosBD = juegoDAO.listarTodos();
        for (Juego juego : juegosBD) {
            repositorioMemoria.put(juego.getId(), juego);
        }
    }

    // Agrega un nuevo juego validando los datos necesarios y actualiza la caché.
    public boolean agregar(Juego juego) {
        if (juego == null || juego.getDesarrollador() == null) {
            return false;
        }
        if (juego.getDesarrollador().getId() <= 0) {
            return false;
        }

        boolean exito = juegoDAO.agregar(juego);
        if (exito && juego.getId() > 0) {
            repositorioMemoria.put(juego.getId(), juego);
        }
        return exito;
    }

    // Elimina un juego por su ID y lo quita de la caché.
    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }

        boolean exito = juegoDAO.eliminar(id);
        if (exito) {
            repositorioMemoria.remove(id);
        }
        return exito;
    }

    // Busca y devuelve un juego por su ID (desde la caché).
    public Juego buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return repositorioMemoria.get(id);
    }

    // Lista todos los juegos registrados (desde la caché).
    public List<Juego> listarTodos() {
        return new ArrayList<>(repositorioMemoria.values());
    }

    // Da de alta (activa) un juego por su ID y sincroniza la caché.
    public boolean darDeAlta(int id) {
        return cambiarEstadoYSincronizar(id, true);
    }

    // Da de baja (desactiva) un juego por su ID y sincroniza la caché.
    public boolean darDeBaja(int id) {
        return cambiarEstadoYSincronizar(id, false);
    }

    private boolean cambiarEstadoYSincronizar(int id, boolean activo) {
        if (id <= 0) {
            return false;
        }
        if (!repositorioMemoria.containsKey(id)) {
            return false;
        }

        boolean exito = juegoDAO.cambiarEstado(id, activo);
        if (exito) {
            // Re-sincronizamos el objeto desde la BD para asegurar consistencia
            Juego actualizado = juegoDAO.buscarPorId(id);
            if (actualizado != null) {
                repositorioMemoria.put(id, actualizado);
            }
        }
        return exito;
    }

    // Modifica un juego existente validando los datos y actualiza la caché.
    public boolean modificar(Juego juego) {
        if (juego == null || juego.getId() <= 0) {
            return false;
        }
        if (!repositorioMemoria.containsKey(juego.getId())) {
            return false;
        }
        if (juego.getDesarrollador() == null || juego.getDesarrollador().getId() <= 0) {
            return false;
        }

        boolean exito = juegoDAO.modificar(juego);
        if (exito) {
            repositorioMemoria.put(juego.getId(), juego);
        }
        return exito;
    }

}
