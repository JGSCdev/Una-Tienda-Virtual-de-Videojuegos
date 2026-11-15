package Service;

import Modelo.DLC;
import Persistencia.DLCdao;

import java.util.List;

public class DLCservice {

    private final DLCdao dlcDao = new DLCdao();

    //Agrega un nuevo DLC validando los datos necesarios.
    public boolean agregar(DLC dlc) {
        if (dlc == null || dlc.getJuego() == null) {
            return false;
        }
        if (dlc.getJuego().getId() <= 0 || dlc.getDesarrollador() == null || dlc.getDesarrollador().getId() <= 0) {
            return false;
        }
        return dlcDao.agregar(dlc);
    }

    //Elimina un DLC por su ID.
    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return dlcDao.eliminar(id);
    }

    //Busca y devuelve un DLC por su ID.
    public DLC buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dlcDao.buscarPorId(id);
    }

    //Lista todos los DLCs registrados.
    public List<DLC> listarTodos() {
        return dlcDao.listarTodos();
    }

    //Da de alta (activa) un DLC por su ID.
    public boolean darDeAlta(int id) {
        if (id <= 0) {
            return false;
        }
        DLC dlc = buscarPorId(id);
        if (dlc == null) {
            return false;
        }
        return dlcDao.cambiarEstado(id, true);
    }

    //Da de baja (desactiva) un DLC por su ID.
    public boolean darDeBaja(int id) {
        if (id <= 0) {
            return false;
        }
        DLC dlc = buscarPorId(id);
        if (dlc == null) {
            return false;
        }
        return dlcDao.cambiarEstado(id, false);
    }

    //Modifica un DLC existente validando los datos.
    public boolean modificar(DLC dlc) {
        if (dlc == null || dlc.getId() <= 0) {
            return false;
        }
        DLC dlcExistente = buscarPorId(dlc.getId());
        if (dlcExistente == null) {
            return false;
        }
        if (dlc.getJuego() == null || dlc.getJuego().getId() <= 0) {
            return false;
        }
        if (dlc.getDesarrollador() == null || dlc.getDesarrollador().getId() <= 0) {
            return false;
        }
        return dlcDao.modificar(dlc);
    }


}
