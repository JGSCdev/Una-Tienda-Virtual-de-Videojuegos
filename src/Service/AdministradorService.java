
package Service;

import Modelo.Administrador;
import Persistencia.AdministradorDAO;

import java.sql.SQLException;
import java.util.List;

public class AdministradorService {

    private final AdministradorDAO dao;

    public AdministradorService(AdministradorDAO dao) {

        if (dao == null) {
            throw new IllegalArgumentException("El DAO no puede ser nulo.");
        }

        this.dao = dao;
    }

    // Da de alta un administrador
    public boolean insertar(Administrador administrador) throws SQLException {

        if (administrador == null) {
            return false;
        }

        if (administrador.getId() != 0) {
            return false;
        }

        if (administrador.getContrasenia() == null ||
                administrador.getContrasenia().isBlank()) {
            return false;
        }

        if (administrador.getPin() == null ||
                administrador.getPin().isBlank()) {
            return false;
        }

        int idGenerado = dao.insertar(administrador);

        if (idGenerado <= 0) {
            return false;
        }

        administrador.asignarIdGenerado(idGenerado);

        return true;
    }

    // Busca un administrador por ID
    public Administrador consultar(int id) throws SQLException {

        if (id <= 0) {
            return null;
        }

        return dao.consultar(id);
    }

    // Lista todos los administradores.
    public List<Administrador> listarTodos() throws SQLException {

        return dao.listarTodos();
    }

    // Modifica la contraseña de un administrador
    public boolean actualizarContrasenia(int id, String contrasenia) throws SQLException {

        if (id <= 0 || contrasenia == null || contrasenia.isBlank()) {
            return false;
        }

        Administrador administrador = dao.consultar(id);

        if (administrador == null) {
            return false;
        }

        return dao.actualizarContrasenia(id, contrasenia);
    }

    // Modifica el PIN de un administrador
    public boolean actualizarPin(int id, String pin) throws SQLException {

        if (id <= 0 || pin == null || pin.isBlank()) {
            return false;
        }

        Administrador administrador = dao.consultar(id);

        if (administrador == null) {
            return false;
        }

        return dao.actualizarPin(id, pin);
    }

    // Elimina un administrador
    public boolean eliminar(int id) throws SQLException {

        if (id <= 0) {
            return false;
        }

        Administrador administrador = dao.consultar(id);

        if (administrador == null) {
            return false;
        }

        return dao.eliminar(id);
    }
}
