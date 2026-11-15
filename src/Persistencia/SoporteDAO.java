package Persistencia;

import Conexion.Conexion;
import Modelo.Soporte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SoporteDAO {

    // -------------------- CUENTAS DE SOPORTE --------------------

    // Registrar un nuevo soporte y devolver el ID generado.
    public int insertar(Soporte s) throws SQLException {

        String sqlInsert = "INSERT INTO soporte (contrasenia, pin) VALUES (?, ?)";
        String sqlID = "SELECT LAST_INSERT_ID()";

        try (Connection con = Conexion.getConnection();
             PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {

            psInsert.setString(1, s.getContrasenia());
            psInsert.setString(2, s.getPin());

            int filas = psInsert.executeUpdate();

            if (filas == 0) {
                return 0;
            }

            // Recuperamos el ID generado por MySQL.
            try (PreparedStatement psID = con.prepareStatement(sqlID);
                 ResultSet rs = psID.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    // Consultar un soporte por su ID.
    public Soporte consultar(int id) throws SQLException {

        String sql = "SELECT * FROM soporte WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Soporte s = new Soporte(
                            rs.getInt("id"),
                            rs.getString("contrasenia"),
                            rs.getString("pin")
                    );

                    // Cargamos las quejas asociadas al soporte.
                    cargarQuejas(con, s);

                    return s;
                }
            }
        }

        return null;
    }

    // Listar todos los soportes.
    public List<Soporte> listarTodos() throws SQLException {

        List<Soporte> lista = new ArrayList<>();

        String sql = "SELECT * FROM soporte";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Soporte s = new Soporte(
                        rs.getInt("id"),
                        rs.getString("contrasenia"),
                        rs.getString("pin")
                );

                // Recuperamos las quejas de cada soporte.
                cargarQuejas(con, s);

                lista.add(s);
            }
        }

        return lista;
    }

    // Modificar la contraseña de un soporte.
    public int actualizarContrasenia(int id, String nuevaContrasenia)
            throws SQLException {

        String sql = "UPDATE soporte SET contrasenia = ? WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaContrasenia);
            ps.setInt(2, id);

            return ps.executeUpdate();
        }
    }

    // Modificar el PIN de un soporte.
    public int actualizarPin(int id, String nuevoPin) throws SQLException {

        String sql = "UPDATE soporte SET pin = ? WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoPin);
            ps.setInt(2, id);

            return ps.executeUpdate();
        }
    }

    // Eliminar un soporte y sus quejas asociadas.
    public int eliminar(int id) throws SQLException {

        String sqlQuejas = "DELETE FROM quejas WHERE id_soporte = ?";
        String sqlSoporte = "DELETE FROM soporte WHERE id = ?";

        try (Connection con = Conexion.getConnection()) {

            con.setAutoCommit(false);

            try {

                // Primero eliminamos las quejas asociadas.
                try (PreparedStatement psQuejas = con.prepareStatement(sqlQuejas)) {
                    psQuejas.setInt(1, id);
                    psQuejas.executeUpdate();
                }

                // Luego eliminamos el soporte.
                int filas;

                try (PreparedStatement psSoporte = con.prepareStatement(sqlSoporte)) {
                    psSoporte.setInt(1, id);
                    filas = psSoporte.executeUpdate();
                }

                con.commit();

                return filas;

            } catch (SQLException e) {

                con.rollback();
                throw e;

            } finally {
                con.setAutoCommit(true);
            }
        }
    }


    // -------------------- QUEJAS --------------------

    // Registrar una queja asociada a un soporte.
    public int insertarQueja(int idSoporte, String queja) throws SQLException {

        String sql = "INSERT INTO quejas (id_soporte, queja) VALUES (?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSoporte);
            ps.setString(2, queja);

            return ps.executeUpdate();
        }
    }

    // Consultar todas las quejas de un soporte.
    public List<String> consultarQuejas(int idSoporte) throws SQLException {

        List<String> lista = new ArrayList<>();

        String sql = "SELECT queja FROM quejas WHERE id_soporte = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSoporte);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(rs.getString("queja"));
                }
            }
        }

        return lista;
    }

    // Eliminar una queja de un soporte.
    public int eliminarQueja(int idSoporte, String queja) throws SQLException {

        String sql = "DELETE FROM quejas WHERE id_soporte = ? AND queja = ? LIMIT 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSoporte);
            ps.setString(2, queja);

            return ps.executeUpdate();
        }
    }


    // -------------------- MÉTODO AUXILIAR --------------------

    // Recupera todas las quejas de un soporte.
    // Método privado para reutilizar la conexión existente.
    private void cargarQuejas(Connection con, Soporte s) throws SQLException {

        String sql = "SELECT queja FROM quejas WHERE id_soporte = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getId());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    s.recibirQueja(rs.getString("queja"));
                }
            }
        }
    }
}