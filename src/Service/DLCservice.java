package Service;

import Colecciones.ProductosColeccionGenerica;
import Modelo.DLC;
import Persistencia.DLCdao;

import java.util.*;

public class DLCservice {

    private final DLCdao dlcDao;
    private final ProductosColeccionGenerica<DLC> repositorioMemoria;

    // Constructor: inicializa el DAO y la caché en memoria, precargando los DLCs de la BD.
    public DLCservice() {
        this.dlcDao = new DLCdao();
        this.repositorioMemoria = new ProductosColeccionGenerica<DLC>();
        cargarDLCsDesdeBD();
    }

    private void cargarDLCsDesdeBD() {
        List<DLC> dlcBD = dlcDao.listarTodos();
        for (DLC dlc : dlcBD) {
            repositorioMemoria.agregar(dlc);
        }
    }

    // Agrega un nuevo DLC validando los datos necesarios y actualiza la caché.
    public boolean agregar(DLC dlc) {
        if (dlc == null || dlc.getJuego() == null) {
            return false;
        }
        if (dlc.getJuego().getId() <= 0 || dlc.getDesarrollador() == null || dlc.getDesarrollador().getId() <= 0) {
            return false;
        }

        boolean exito = dlcDao.agregar(dlc);
        if (exito && dlc.getId() > 0) {
            repositorioMemoria.agregar(dlc);
        }
        return exito;
    }

    // Elimina un DLC por su ID y lo quita de la caché.
    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }

        boolean exito = dlcDao.eliminar(id);
        if (exito) {
            repositorioMemoria.eliminar(id);
        }
        return exito;
    }

    // Busca y devuelve un DLC por su ID (desde la caché).
    public DLC buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return repositorioMemoria.buscar(id);
    }

    // Lista todos los DLCs registrados (desde la caché).
    public List<DLC> listarTodos() {
        return new ArrayList<>(repositorioMemoria.listarTodos());
    }

    // Da de alta (activa) un DLC por su ID y sincroniza la caché.
    public boolean darDeAlta(int id) {
        return cambiarEstadoYSincronizar(id, true);
    }

    // Da de baja (desactiva) un DLC por su ID y sincroniza la caché.
    public boolean darDeBaja(int id) {
        return cambiarEstadoYSincronizar(id, false);
    }

    private boolean cambiarEstadoYSincronizar(int id, boolean activo) {
        if (id <= 0) {
            return false;
        }
        if (!repositorioMemoria.contiene(id)) {
            return false;
        }

        boolean exito = dlcDao.cambiarEstado(id, activo);
        if (exito) {
            // Re-sincronizamos el objeto desde la BD para asegurar consistencia
            DLC actualizado = dlcDao.buscarPorId(id);
            if (actualizado != null) {
                repositorioMemoria.agregar(actualizado);
            }
        }
        return exito;
    }

    // Modifica un DLC existente validando los datos y actualiza la caché.
    public boolean modificar(DLC dlc) {
        if (dlc == null || dlc.getId() <= 0) {
            return false;
        }
        if (!repositorioMemoria.contiene(dlc.getId())) {
            return false;
        }
        if (dlc.getJuego() == null || dlc.getJuego().getId() <= 0) {
            return false;
        }
        if (dlc.getDesarrollador() == null || dlc.getDesarrollador().getId() <= 0) {
            return false;
        }

        boolean exito = dlcDao.modificar(dlc);
        if (exito) {
            repositorioMemoria.agregar(dlc);
        }
        return exito;
    }

}
