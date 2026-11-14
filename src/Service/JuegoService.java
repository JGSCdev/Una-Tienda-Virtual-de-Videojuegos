package Service;

import Modelo.Productos.Juego;
import Persistencia.JuegoDAO;

import java.util.List;

public class JuegoService {

    private final JuegoDAO juegoDAO = new JuegoDAO();

    public boolean agregar(Juego juego) {
        if (juego == null || juego.getDesarrollador() == null) {
            return false;
        }
        if (juego.getDesarrollador().getId() <= 0) {
            return false;
        }
        return juegoDAO.agregar(juego);
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return juegoDAO.eliminar(id);
    }

    public Juego buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return juegoDAO.buscarPorId(id);
    }

    public List<Juego> listarTodos() {
        return juegoDAO.listarTodos();
    }

    public boolean darDeAlta(int id) {
        if (id <= 0) {
            return false;
        }
        Juego juego = buscarPorId(id);
        if (juego == null) {
            return false;
        }
        return juegoDAO.cambiarEstado(id, true);
    }

    public boolean darDeBaja(int id) {
        if (id <= 0) {
            return false;
        }
        Juego juego = buscarPorId(id);
        if (juego == null) {
            return false;
        }
        return juegoDAO.cambiarEstado(id, false);
    }

    public boolean modificar(Juego juego) {
        if (juego == null || juego.getId() <= 0) {
            return false;
        }
        Juego juegoExistente = buscarPorId(juego.getId());
        if (juegoExistente == null) {
            return false;
        }
        if (juego.getDesarrollador() == null || juego.getDesarrollador().getId() <= 0) {
            return false;
        }
        return juegoDAO.modificar(juego);
    }

}
