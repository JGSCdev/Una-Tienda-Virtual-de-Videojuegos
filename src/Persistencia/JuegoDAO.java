package Persistencia;

import Conexion.Conexion;
import Modelo.Cuentas.Desarrollador;
import Modelo.Productos.DLC;
import Modelo.Productos.Juego;
import Enum.GenerosJuegos;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JuegoDAO {

    public boolean agregar(Juego juego) {
        if (juego == null) {
            return false;
        }

        String sql = "INSERT INTO juego (nombre, precio, descripcion, fecha_lanzamiento, peso_gb, desarrollador_id, estado, genero, edad_minima, requisitos_minimos, requisitos_recomendados, duracion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, juego.getNombre());
            ps.setDouble(2, juego.getPrecio());
            ps.setString(3, juego.getDescripcion());
            ps.setDate(4, Date.valueOf(juego.getFechaLanzamiento()));
            ps.setDouble(5, juego.getPesoGB());
            ps.setInt(6, juego.getDesarrollador().getId());
            ps.setBoolean(7, juego.isEstado());
            ps.setString(8, juego.getGenero().name());
            ps.setInt(9, juego.getEdadMinima());
            ps.setString(10, juego.getRequisitosMinimos());
            ps.setString(11, juego.getRequisitosRecomendados());
            ps.setDouble(12, juego.getDuracion());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                return false;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    juego.setId(rs.getInt(1));
                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al agregar juego: " + e.getMessage());
        }
        return false;
    }

    public boolean cambiarEstado(int id, boolean estado) {
        String sql = "UPDATE juego SET estado = ? WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del juego: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM juego WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar juego: " + e.getMessage());
            return false;
        }
    }

    private Juego mapearJuego(ResultSet rs) throws SQLException {
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

        double duracion = rs.getDouble("duracion");
        int edadMinima = rs.getInt("edad_minima");
        GenerosJuegos genero = GenerosJuegos.valueOf(rs.getString("genero"));
        String requisitosMinimos = rs.getString("requisitos_minimos");
        String requisitosRecomendados = rs.getString("requisitos_recomendados");

        List<DLC> dlcs = new ArrayList<>(); // Se pueden cargar o dejar vacíos al inicializar

        return new Juego (id, desarrollador, descripcion, nombre, fechaLanzamiento,
                pesoGB, precio, estado, duracion, edadMinima, genero,
                requisitosMinimos, requisitosRecomendados, dlcs);
    }

    public Juego buscarPorId(int id) {

        String sql = "SELECT * FROM juego WHERE id = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearJuego(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar juego por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Juego> listarTodos() {
        List<Juego> lista = new ArrayList<>();
        String sql = "SELECT * FROM juego";
        try (Connection conn = Conexion.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearJuego(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar juegos: " + e.getMessage());
        }
        return lista;
    }

    public boolean modificar(Juego juego) {
        if (juego == null || juego.getId() <= 0) {
            return false;
        }
        String sql = "UPDATE juego SET nombre = ?, precio = ?, descripcion = ?, fecha_lanzamiento = ?, peso_gb = ?, desarrollador_id = ?, estado = ?, genero = ?, edad_minima = ?, requisitos_minimos = ?, requisitos_recomendados = ?, duracion = ? WHERE id = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, juego.getNombre());
            ps.setDouble(2, juego.getPrecio());
            ps.setString(3, juego.getDescripcion());
            ps.setDate(4, Date.valueOf(juego.getFechaLanzamiento()));
            ps.setDouble(5, juego.getPesoGB());
            ps.setInt(6, juego.getDesarrollador().getId());
            ps.setBoolean(7, juego.isEstado());
            ps.setString(8, juego.getGenero().name());
            ps.setInt(9, juego.getEdadMinima());
            ps.setString(10, juego.getRequisitosMinimos());
            ps.setString(11, juego.getRequisitosRecomendados());
            ps.setDouble(12, juego.getDuracion());
            ps.setInt(13, juego.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar juego: " + e.getMessage());
            return false;
        }
    }

}
