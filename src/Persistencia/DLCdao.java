package Persistencia;

import Conexion.Conexion;
import Modelo.Cuentas.Desarrollador;
import Modelo.Productos.DLC;
import Modelo.Productos.Juego;
import Enum.TipoContenido;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DLCdao {

    public boolean agregar(DLC dlc) {
        if (dlc == null || dlc.getJuego() == null) {
            return false;
        }
        String sql = "INSERT INTO dlc(nombre, precio, descripcion, fecha_lanzamiento, peso_gb, desarrollador_id, estado, juego_id, contenido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dlc.getNombre());
            ps.setDouble(2, dlc.getPrecio());
            ps.setString(3, dlc.getDescripcion());
            ps.setDate(4, Date.valueOf(dlc.getFechaLanzamiento()));
            ps.setDouble(5, dlc.getPesoGB());
            ps.setInt(6, dlc.getDesarrollador().getId());
            ps.setBoolean(7, dlc.isEstado());
            ps.setInt(8, dlc.getJuego().getId());
            ps.setString(9, dlc.getContenido().name());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                return false;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    dlc.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar DLC: " + e.getMessage());
        }
        return false;
    }

    private boolean cambiarEstado(int id, boolean estado) {
        String sql = "UPDATE dlc SET estado = ? WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del DLC: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM dlc WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar DLC: " + e.getMessage());
            return false;
        }
    }

    private DLC mapearDlc(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio");
        String descripcion = rs.getString("descripcion");
        LocalDate fechaLanzamiento = rs.getDate("fecha_lanzamiento").toLocalDate();
        double pesoGB = rs.getDouble("peso_gb");
        boolean estado = rs.getBoolean("estado");
        int desarrolladorId = rs.getInt("desarrollador_id");

        Desarrollador desarrollador = new Desarrollador();
        desarrollador.setId(desarrolladorId);

        int juegoId = rs.getInt("juego_id");
        Juego juego = new JuegoDAO().buscarPorId(juegoId);

        TipoContenido contenido = TipoContenido.valueOf(rs.getString("contenido"));

        return new DLC(id, desarrollador, descripcion, nombre, fechaLanzamiento,
                pesoGB, precio, estado, juego, contenido);
    }

    public List<DLC> listarTodos() {
        List<DLC> lista = new ArrayList<>();
        String sql = "SELECT * FROM dlc";
        try (Connection conn = Conexion.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearDlc(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar DLCs: " + e.getMessage());
        }
        return lista;
    }

    public DLC buscarPorId(int id) {
        String sql = "SELECT * FROM dlc WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearDlc(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar DLC por ID: " + e.getMessage());
        }
        return null;
    }

}
