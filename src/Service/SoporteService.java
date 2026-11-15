
package Service;

import Coleciones.ColeccionGestoresGenerica;
import Persistencia.SoporteDAO;
import Modelo.Soporte;

import java.sql.SQLException;
import java.util.List;

public class SoporteService {

    private final SoporteDAO dao;
    private final ColeccionGestoresGenerica<Soporte> coleccion;

    // Constructor que recibe el DAO y carga los soportes desde MySQL
    public SoporteService(SoporteDAO dao) throws SQLException {

        if (dao == null) {
            throw new IllegalArgumentException("El DAO no puede ser nulo");
        }

        this.dao = dao;
        this.coleccion = new ColeccionGestoresGenerica<>();

        // Carga los soportes recuperados de MySQL en la colección
        coleccion.cargar(dao.listarTodos());
    }

    // -------------------- CUENTAS DE SOPORTE --------------------

    // Registrar un nuevo soporte
    public boolean insertar(Soporte s) throws SQLException {

        if (s == null) {
            throw new IllegalArgumentException("El soporte no puede ser nulo");
        }

        // Verifica que el soporte todavía no esté registrado
        if (s.getId() != 0) {
            throw new IllegalArgumentException("El soporte ya tiene un ID asignado");
        }

        // Verifica que la contraseña sea válida
        if (s.getContrasenia() == null || s.getContrasenia().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Verifica que el PIN sea válido
        if (s.getPin() == null || s.getPin().isBlank()) {
            throw new IllegalArgumentException("El PIN es obligatorio");
        }

        // Inserta el soporte en MySQL y recupera el ID generado
        int idGenerado = dao.insertar(s);

        if (idGenerado <= 0) {
            return false;
        }

        // Asigna el ID generado por la base de datos
        s.asignarIdGenerado(idGenerado);

        // Agrega el soporte a la colección en memoria
        if (!coleccion.agregar(s)) {
            throw new IllegalStateException(
                    "No se pudo agregar el soporte a la colección"
            );
        }

        return true;
    }

    // Consultar un soporte por ID dentro de la colección
    public Soporte consultar(int id) {
        validarId(id);
        return buscarEnLista(id);
    }

    // Devuelve una copia no modificable de la colección
    public List<Soporte> listarTodos() {
        return coleccion.listarTodos();
    }

    // Modificar la contraseña de un soporte
    public boolean actualizarContrasenia(int id, String nuevaContrasenia)
            throws SQLException {

        validarId(id);

        if (nuevaContrasenia == null || nuevaContrasenia.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña no puede estar vacía"
            );
        }

        // Busca el soporte que se quiere modificar
        Soporte soporte = buscarEnLista(id);

        if (soporte == null) {
            return false;
        }

        // Actualiza primero la contraseña en MySQL
        boolean resultado = dao.actualizarContrasenia(id, nuevaContrasenia) > 0;

        // Si MySQL confirma el cambio, actualiza el objeto en memoria
        if (resultado) {
            soporte.setContrasenia(nuevaContrasenia);
        }

        return resultado;
    }

    // Modificar el PIN de un soporte
    public boolean actualizarPin(int id, String nuevoPin) throws SQLException {

        validarId(id);

        if (nuevoPin == null || nuevoPin.isBlank()) {
            throw new IllegalArgumentException(
                    "El PIN no puede estar vacío"
            );
        }

        // Busca el soporte que se quiere modificar
        Soporte soporte = buscarEnLista(id);

        if (soporte == null) {
            return false;
        }

        // Actualiza primero el PIN en MySQL
        boolean resultado = dao.actualizarPin(id, nuevoPin) > 0;

        // Si MySQL confirma el cambio, actualiza el objeto en memoria
        if (resultado) {
            soporte.setPin(nuevoPin);
        }

        return resultado;
    }

    // Eliminar un soporte
    public boolean eliminar(int id) throws SQLException {

        validarId(id);

        // Busca el soporte que se quiere eliminar
        Soporte soporte = buscarEnLista(id);

        if (soporte == null) {
            return false;
        }

        // Elimina primero el soporte y sus quejas de MySQL
        boolean resultado = dao.eliminar(id) > 0;

        // Si la eliminación fue exitosa, retira el soporte de la colección
        if (resultado) {
            coleccion.eliminar(id);
        }

        return resultado;
    }

    // -------------------- GESTIÓN DE QUEJAS --------------------

    // Registrar una queja utilizando el ID del soporte
    public boolean insertarQueja(int idSoporte, String queja)
            throws SQLException {

        validarId(idSoporte);
        validarQueja(queja);

        // Busca el soporte cuya lista de quejas debe actualizarse
        Soporte soporte = buscarEnLista(idSoporte);

        if (soporte == null) {
            return false;
        }

        // Guarda primero la queja en MySQL
        boolean resultado = dao.insertarQueja(idSoporte, queja) > 0;

        // Si la inserción fue exitosa, agrega la queja a la lista del soporte
        if (resultado) {
            soporte.recibirQueja(queja);
        }

        return resultado;
    }

    // Consultar las quejas de un soporte desde la colección en memoria
    public List<String> consultarQuejas(int idSoporte) {

        validarId(idSoporte);

        // Busca el soporte que contiene las quejas
        Soporte soporte = buscarEnLista(idSoporte);

        if (soporte == null) {
            return List.of();
        }

        return soporte.consultarQuejas();
    }

    // Atender una queja utilizando el ID del soporte
    public boolean atenderQueja(int idSoporte, String queja)
            throws SQLException {

        validarId(idSoporte);
        validarQueja(queja);

        // Busca el soporte cuya lista de quejas debe actualizarse
        Soporte soporte = buscarEnLista(idSoporte);

        if (soporte == null) {
            return false;
        }

        // Elimina primero la queja de MySQL
        boolean resultado = dao.eliminarQueja(idSoporte, queja) > 0;

        // Si MySQL confirma la eliminación, quita la queja de la lista del soporte
        if (resultado) {
            soporte.atenderQueja(queja);
        }

        return resultado;
    }

    // Recibir una queja y actualizar el objeto Soporte
    public boolean recibirQueja(Soporte s, String queja)
            throws SQLException {

        if (s == null) {
            throw new IllegalArgumentException("El soporte no puede ser nulo");
        }

        validarId(s.getId());
        validarQueja(queja);

        // Busca el objeto que realmente está almacenado en la colección
        Soporte soporteEnLista = buscarEnLista(s.getId());

        if (soporteEnLista == null) {
            return false;
        }

        // Guarda primero la queja en MySQL
        boolean resultado = dao.insertarQueja(s.getId(), queja) > 0;

        // Si se guardó correctamente, actualiza la colección y el objeto recibido
        if (resultado) {
            soporteEnLista.recibirQueja(queja);

            if (s != soporteEnLista) {
                s.recibirQueja(queja);
            }
        }

        return resultado;
    }

    // Atender una queja y actualizar el objeto Soporte
    public boolean atenderQueja(Soporte s, String queja)
            throws SQLException {

        if (s == null) {
            throw new IllegalArgumentException("El soporte no puede ser nulo");
        }

        validarId(s.getId());
        validarQueja(queja);

        // Busca el objeto que realmente está almacenado en la colección
        Soporte soporteEnLista = buscarEnLista(s.getId());

        if (soporteEnLista == null) {
            return false;
        }

        // Elimina primero la queja de MySQL
        boolean resultado = dao.eliminarQueja(s.getId(), queja) > 0;

        // Si se eliminó correctamente, actualiza la colección y el objeto recibido
        if (resultado) {
            soporteEnLista.atenderQueja(queja);

            if (s != soporteEnLista) {
                s.atenderQueja(queja);
            }
        }

        return resultado;
    }

    // -------------------- VALIDACIONES --------------------

    // Verifica que un ID sea válido
    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
    }

    // Verifica que una queja no sea nula ni esté vacía
    private void validarQueja(String queja) {
        if (queja == null || queja.isBlank()) {
            throw new IllegalArgumentException(
                    "La queja no puede estar vacía"
            );
        }
    }

    // -------------------- MÉTODOS AUXILIARES --------------------

    // Busca un soporte por ID dentro de la colección
    private Soporte buscarEnLista(int id) {
        return coleccion.consultar(id);
    }
}
