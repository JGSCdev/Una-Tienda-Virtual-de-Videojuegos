
package Service;

import Coleciones.ColeccionGestoresGenerica;
import Modelo.Administrador;
import Persistencia.AdministradorDAO;

import java.sql.SQLException;
import java.util.List;

public class AdministradorService {

    private final AdministradorDAO dao;
    private final ColeccionGestoresGenerica<Administrador> coleccion;

    // Constructor que recibe el DAO y carga los administradores desde MySQL
    public AdministradorService(AdministradorDAO dao) throws SQLException {

        if (dao == null) {
            throw new IllegalArgumentException("El DAO no puede ser nulo");
        }

        this.dao = dao;
        this.coleccion = new ColeccionGestoresGenerica<>();

        // Carga los administradores recuperados de MySQL en la colección
        coleccion.cargar(dao.listarTodos());
    }

    // -------------------- CUENTAS DE ADMINISTRADOR --------------------

    // Da de alta un administrador
    public boolean insertar(Administrador administrador) throws SQLException {

        if (administrador == null) {
            return false;
        }

        // Comprueba que el administrador todavía no tenga un ID asignado
        if (administrador.getId() != 0) {
            return false;
        }

        // Verifica que la contraseña sea válida
        if (administrador.getContrasenia() == null ||
                administrador.getContrasenia().isBlank()) {
            return false;
        }

        // Verifica que el PIN sea válido
        if (administrador.getPin() == null ||
                administrador.getPin().isBlank()) {
            return false;
        }

        // Inserta el administrador en MySQL y recupera el ID generado
        int idGenerado = dao.insertar(administrador);

        if (idGenerado <= 0) {
            return false;
        }

        // Asigna el ID generado por la base de datos
        administrador.asignarIdGenerado(idGenerado);

        // Agrega el administrador a la colección en memoria
        if (!coleccion.agregar(administrador)) {
            throw new IllegalStateException(
                    "No se pudo agregar el administrador a la colección"
            );
        }

        return true;
    }

    // Busca un administrador por su ID dentro de la colección
    public Administrador consultar(int id) {

        if (id <= 0) {
            return null;
        }

        return buscarEnLista(id);
    }

    // Devuelve una copia no modificable de la colección
    public List<Administrador> listarTodos() {
        return coleccion.listarTodos();
    }

    // Modifica la contraseña de un administrador
    public boolean actualizarContrasenia(int id, String contrasenia)
            throws SQLException {

        if (id <= 0 || contrasenia == null || contrasenia.isBlank()) {
            return false;
        }

        // Busca el administrador que se quiere modificar
        Administrador administrador = buscarEnLista(id);

        if (administrador == null) {
            return false;
        }

        // Actualiza primero la contraseña en MySQL
        boolean resultado = dao.actualizarContrasenia(id, contrasenia);

        // Si la base de datos confirma el cambio, actualiza el objeto en memoria
        if (resultado) {
            administrador.setContrasenia(contrasenia);
        }

        return resultado;
    }

    // Modifica el PIN de un administrador
    public boolean actualizarPin(int id, String pin) throws SQLException {

        if (id <= 0 || pin == null || pin.isBlank()) {
            return false;
        }

        // Busca el administrador que se quiere modificar
        Administrador administrador = buscarEnLista(id);

        if (administrador == null) {
            return false;
        }

        // Actualiza primero el PIN en MySQL
        boolean resultado = dao.actualizarPin(id, pin);

        // Si la base de datos confirma el cambio, actualiza el objeto en memoria
        if (resultado) {
            administrador.setPin(pin);
        }

        return resultado;
    }

    // Elimina un administrador
    public boolean eliminar(int id) throws SQLException {

        if (id <= 0) {
            return false;
        }

        // Busca el administrador que se quiere eliminar
        Administrador administrador = buscarEnLista(id);

        if (administrador == null) {
            return false;
        }

        // Elimina primero el registro de MySQL
        boolean resultado = dao.eliminar(id);

        // Si la eliminación fue exitosa, elimina el objeto de la colección
        if (resultado) {
            coleccion.eliminar(id);
        }

        return resultado;
    }

    // -------------------- MÉTODOS AUXILIARES --------------------

    // Busca un administrador por ID dentro de la colección
    private Administrador buscarEnLista(int id) {
        return coleccion.consultar(id);
    }
}
