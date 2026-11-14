package Service;

import DAO.SoporteDAO;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SoporteService {

    private final SoporteDAO dao;

    public SoporteService(SoporteDAO dao) {

        if (dao == null) {
            throw new IllegalArgumentException("El DAO no puede ser null");
        }

        this.dao = dao;
    }

    // Valida que la queja tenga contenido
    private void validarQueja(String queja) {

        if (queja == null || queja.isBlank()) {
            throw new IllegalArgumentException(
                    "La queja no puede estar vacía"
            );
        }
    }

    // Registra una nueva queja
    public boolean recibirQueja(String queja) throws SQLException {

        validarQueja(queja);

        return dao.insertar(queja) > 0;
    }

    // Devuelve todas las quejas almacenadas
    public List<String> consultarQuejas() throws SQLException {

        // Impide que quien recibe la lista pueda modificarla
        return Collections.unmodifiableList(dao.listarTodas());
    }

    // Atiende una queja eliminándola de la base de datos
    public boolean atenderQueja(String queja) throws SQLException {

        validarQueja(queja);

        return dao.eliminar(queja);
    }
}
