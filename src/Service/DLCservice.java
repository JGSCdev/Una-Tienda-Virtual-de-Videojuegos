package Service;

import Modelo.DLC;
import Persistencia.DLCdao;

import java.util.List;

public class DLCservice {

    private final DLCdao dlcDao = new DLCdao();

    public boolean agregar(DLC dlc) {
        if (dlc == null || dlc.getJuego() == null) {
            return false;
        }
        if (dlc.getJuego().getId() <= 0 || dlc.getDesarrollador() == null || dlc.getDesarrollador().getId() <= 0) {
            return false;
        }
        return dlcDao.agregar(dlc);
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return dlcDao.eliminar(id);
    }

    public DLC buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dlcDao.buscarPorId(id);
    }

    public List<DLC> listarTodos() {
        return dlcDao.listarTodos();
    }

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
