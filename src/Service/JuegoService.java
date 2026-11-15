package Service;

import Modelo.Juego;
import Persistencia.JuegoDAO;

import java.util.List;

public class JuegoService {

    private final JuegoDAO juegoDAO = new JuegoDAO();

    //Agrega un nuevo juego validando los datos necesarios.
    public boolean agregar(Juego juego) {
        if (juego == null || juego.getDesarrollador() == null) {
            return false;
        }
        if (juego.getDesarrollador().getId() <= 0) {
            return false;
        }
        return juegoDAO.agregar(juego);
    }

    //Elimina un juego por su ID.
    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return juegoDAO.eliminar(id);
    }

    //Busca y devuelve un juego por su ID.
    public Juego buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return juegoDAO.buscarPorId(id);
    }

    //Lista todos los juegos registrados.
    public List<Juego> listarTodos() {
        return juegoDAO.listarTodos();
    }

    //Da de alta (activa) un juego por su ID.
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

    //Da de baja (desactiva) un juego por su ID.
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

    //Modifica un juego existente validando los datos.
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
