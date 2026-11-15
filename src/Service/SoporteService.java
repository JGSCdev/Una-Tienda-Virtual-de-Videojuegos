package Service;

import Persistencia.SoporteDAO;
import Modelo.Soporte;

import java.sql.SQLException;
import java.util.List;

public class SoporteService {

    private SoporteDAO dao;

    public SoporteService(SoporteDAO dao) {
        if (dao == null) {
            throw new IllegalArgumentException("El DAO no puede ser nulo");
        }
        this.dao = dao;
    }

    // -------------------- CUENTAS DE SOPORTE --------------------

    // Registrar soporte
    // Registra un nuevo soporte.
    // Valida sus datos antes de enviarlo al DAO.
    // Luego asigna al objeto el ID generado por la base de datos.
    public boolean insertar(Soporte s) throws SQLException {

        // Verificamos que el soporte no sea nulo.
        if (s == null) {
            throw new IllegalArgumentException("El soporte no puede ser nulo");
        }

        // Verificamos que el soporte todavía no esté registrado.
        if (s.getId() != 0) {
            throw new IllegalArgumentException("El soporte ya tiene un ID asignado");
        }

        // Verificamos que la contraseña no esté vacía.
        if (s.getContrasenia() == null || s.getContrasenia().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Verificamos que el PIN no esté vacío.
        if (s.getPin() == null || s.getPin().isBlank()) {
            throw new IllegalArgumentException("El PIN es obligatorio");
        }

        // Insertamos el soporte y recuperamos el ID generado.
        int idGenerado = dao.insertar(s);

        // Asignamos el ID al objeto para poder utilizarlo posteriormente.
        s.asignarIdGenerado(idGenerado);

        return true;
    }

    // Consultar soporte por ID
    public Soporte consultar(int id) throws SQLException {
        validarId(id);

        return dao.consultar(id);
    }

    // Listar todos los soportes
    public List<Soporte> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    // Modificar contraseña
    public boolean actualizarContrasenia(int id, String nuevaContrasenia)
            throws SQLException {

        validarId(id);

        if (nuevaContrasenia == null || nuevaContrasenia.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña no puede estar vacía"
            );
        }

        return dao.actualizarContrasenia(id, nuevaContrasenia) > 0;
    }

    // Modificar PIN
    public boolean actualizarPin(int id, String nuevoPin) throws SQLException {

        validarId(id);

        if (nuevoPin == null || nuevoPin.isBlank()) {
            throw new IllegalArgumentException(
                    "El PIN no puede estar vacío"
            );
        }

        return dao.actualizarPin(id, nuevoPin) > 0;
    }

    // Eliminar soporte
    public boolean eliminar(int id) throws SQLException {

        validarId(id);

        return dao.eliminar(id) > 0;
    }


    // -------------------- QUEJAS --------------------

    // Registrar una queja
    public boolean insertarQueja(int idSoporte, String queja) throws SQLException {

        validarId(idSoporte);
        validarQueja(queja);

        return dao.insertarQueja(idSoporte, queja) > 0;
    }

    // Consultar quejas
    public List<String> consultarQuejas(int idSoporte) throws SQLException {

        validarId(idSoporte);

        return dao.consultarQuejas(idSoporte);
    }

    // Atender una queja
    public boolean atenderQueja(int idSoporte, String queja) throws SQLException {

        validarId(idSoporte);
        validarQueja(queja);

        return dao.eliminarQueja(idSoporte, queja) > 0;
    }


    // Recibir queja y actualizar el objeto Soporte
    public boolean recibirQueja(Soporte s, String queja) throws SQLException {

        if (s == null) {
            throw new IllegalArgumentException(
                    "El soporte no puede ser nulo"
            );
        }

        validarId(s.getId());
        validarQueja(queja);

        boolean resultado = dao.insertarQueja(s.getId(), queja) > 0;

        if (resultado) {
            s.recibirQueja(queja);
        }

        return resultado;
    }

    // Atender queja y actualizar el objeto Soporte
    public boolean atenderQueja(Soporte s, String queja) throws SQLException {

        if (s == null) {
            throw new IllegalArgumentException(
                    "El soporte no puede ser nulo"
            );
        }

        validarId(s.getId());
        validarQueja(queja);

        boolean resultado = dao.eliminarQueja(s.getId(), queja) > 0;

        if (resultado) {
            s.atenderQueja(queja);
        }

        return resultado;
    }

    // -------------------- VALIDACIONES --------------------

    // Verifica que un ID sea válido.
    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID debe ser válido"
            );
        }
    }

    // Verifica que una queja no sea nula ni esté vacía.
    private void validarQueja(String queja) {
        if (queja == null || queja.isBlank()) {
            throw new IllegalArgumentException(
                    "La queja no puede estar vacía"
            );
        }
    }
}