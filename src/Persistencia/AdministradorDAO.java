package Persistencia;

import Conexion.Conexion;
import Modelo.Administrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO {

    // -------------------- CUENTAS DE ADMINISTRADOR --------------------

    // Inserta un administrador y devuelve el ID generado
    public int insertar(Administrador administrador) throws SQLException {

        String sqlInsert = "INSERT INTO administrador (contrasenia, pin) VALUES (?, ?)";

        String sqlID = "SELECT LAST_INSERT_ID()";

        try (Connection con = Conexion.getConnection();
             PreparedStatement psInsert =
                     con.prepareStatement(sqlInsert)) {

            psInsert.setString(1, administrador.getContrasenia());
            psInsert.setString(2, administrador.getPin());

            int filas = psInsert.executeUpdate();

            if (filas == 0) {
                return 0;
            }

            // Recuperamos el ID generado por MySQL
            // Utilizamos la misma conexión del INSERT
            try (PreparedStatement psID = con.prepareStatement(sqlID);
                 ResultSet rs = psID.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    // Busca un administrador por su ID
    public Administrador consultar(int id) throws SQLException {

        String sql = "SELECT * FROM administrador WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearAdministrador(rs);
                }
            }
        }

        return null;
    }

    // Devuelve todos los administradores registrados
    public List<Administrador> listarTodos() throws SQLException {

        List<Administrador> administradores = new ArrayList<>();

        String sql = "SELECT * FROM administrador";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                administradores.add(mapearAdministrador(rs));
            }
        }

        return administradores;
    }

    // Actualiza la contraseña de un administrador
    public boolean actualizarContrasenia(int id, String contrasenia) throws SQLException {

        String sql = "UPDATE administrador SET contrasenia = ? WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, contrasenia);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    // Actualiza el PIN de un administrador
    public boolean actualizarPin(int id, String pin) throws SQLException {

        String sql = "UPDATE administrador SET pin = ? WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pin);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    // Elimina un administrador de la base de datos
    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM administrador WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    // -------------------- MÉTODO AUXILIAR --------------------

    // Convierte un registro de la base de dato
    // en un objeto Administrador
    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {

        return new Administrador(
                rs.getInt("id"),
                rs.getString("contrasenia"),
                rs.getString("pin")
        );
    }
}