package DAO;
import Conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SoporteDAO {

    public SoporteDAO() {
    }

    // Guarda una queja en la base de datos
    public int insertar(String queja) throws SQLException {

        String sql = "INSERT INTO quejas (descripcion) VALUES (?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, queja);

            // Devuelve la cantidad de registros insertados
            return ps.executeUpdate();
        }
    }

    // Recupera todas las quejas almacenadas
    public List<String> listarTodas() throws SQLException {

        String sql = "SELECT descripcion FROM quejas ORDER BY fecha_creacion";

        List<String> lista = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("descripcion"));
            }
        }

        return lista;
    }

    // Elimina una sola queja que coincida con la descripción recibida
    public boolean eliminar(String queja) throws SQLException {

        String sql = "DELETE FROM quejas WHERE descripcion = ? LIMIT 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, queja);

            // Devuelve true si se eliminó una queja
            return ps.executeUpdate() > 0;
        }
    }
}
