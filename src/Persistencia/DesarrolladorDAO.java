package Persistencia;
import Modelo.Desarrollador;
import Modelo.Producto;
import Modelo.Juego;
import Modelo.DLC;
import Conexion.Conexion;

import Enum.GenerosJuegos;
import Enum.TipoContenido;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DesarrolladorDAO {

    // Registra un nuevo desarrollador
    public boolean insertar(Desarrollador desarrollador) {
        String sql = "INSERT INTO desarrolladores (username, contrasenia, email, estado, creacion_cuenta, descripcion, nacionalidad, nombre, apellido, fecha_nacimiento, ganancias, valoracion_general) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, desarrollador.getUsername());
            ps.setString(2, desarrollador.getContrasenia()); // Asegúrate de tener getContrasenia() en Usuario
            ps.setString(3, desarrollador.getEmail());
            ps.setBoolean(4, desarrollador.getEstado());
            ps.setObject(5, desarrollador.getCreacionCuenta());
            ps.setString(6, desarrollador.getDescripcion());
            ps.setString(7, desarrollador.getNacionalidad());
            ps.setString(8, desarrollador.getNombre());
            ps.setString(9, desarrollador.getApellido());
            ps.setObject(10, desarrollador.getFechaNacimiento());
            ps.setDouble(11, desarrollador.getGanancias()); // o getGanancias() según tu getter
            ps.setDouble(12, desarrollador.getValoracionGeneral());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        desarrollador.setId(rs.getInt(1)); // Asigna el ID autogenerado
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Buscar desarrollador por ID, cargando su catálogo propio
    public Desarrollador buscarPorId(int id) {
        String sql = "SELECT * FROM desarrolladores WHERE id = ?";
        Desarrollador desarrollador = null;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Primero lo mapeamos de forma básica para obtener su ID
                    desarrollador = mapearDesarrolladorSinCatalogo(rs);

                    // Luego cargamos su catálogo propio pasándole la conexión y el propio desarrollador
                    Set<Producto> catalogo = obtenerCatalogoPropio(con, desarrollador);

                    // Si prefieres re-instanciarlo con el catálogo completo usando su constructor de BD:
                    desarrollador = new Desarrollador(
                            desarrollador.getId(),
                            desarrollador.getUsername(),
                            desarrollador.getContrasenia(),
                            desarrollador.getEmail(),
                            desarrollador.getEstado(),
                            desarrollador.getCreacionCuenta(),
                            desarrollador.getDescripcion(),
                            desarrollador.getNacionalidad(),
                            desarrollador.getNombre(),
                            desarrollador.getApellido(),
                            desarrollador.getFechaNacimiento(),
                            catalogo,
                            desarrollador.getGanancias(),
                            desarrollador.getValoracionGeneral()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return desarrollador;
    }

    // Lista todos los desarrolladores
    public List<Desarrollador> listarTodos() {
        String sql = "SELECT * FROM desarrolladores";
        List<Desarrollador> lista = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Desarrollador devTemp = mapearDesarrolladorSinCatalogo(rs);
                Set<Producto> catalogo = obtenerCatalogoPropio(con, devTemp);

                Desarrollador desarrolladorCompleto = new Desarrollador(
                        devTemp.getId(),
                        devTemp.getUsername(),
                        devTemp.getContrasenia(),
                        devTemp.getEmail(),
                        devTemp.getEstado(),
                        devTemp.getCreacionCuenta(),
                        devTemp.getDescripcion(),
                        devTemp.getNacionalidad(),
                        devTemp.getNombre(),
                        devTemp.getApellido(),
                        devTemp.getFechaNacimiento(),
                        catalogo,
                        devTemp.getGanancias(),
                        devTemp.getValoracionGeneral()
                );

                lista.add(desarrolladorCompleto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Actualizar datos del desarrollador, ganancias, etc.
    public boolean actualizar(Desarrollador desarrollador) {
        String sql = "UPDATE desarrolladores SET username = ?, email = ?, estado = ?, descripcion = ?, nacionalidad = ?, nombre = ?, apellido = ?, fecha_nacimiento = ?, ganancias = ?, valoracion_general = ? WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, desarrollador.getUsername());
            ps.setString(2, desarrollador.getEmail());
            ps.setBoolean(3, desarrollador.getEstado());
            ps.setString(4, desarrollador.getDescripcion());
            ps.setString(5, desarrollador.getNacionalidad());
            ps.setString(6, desarrollador.getNombre());
            ps.setString(7, desarrollador.getApellido());
            ps.setObject(7, desarrollador.getFechaNacimiento()); // Ojo con los índices si se ajustan
            ps.setDouble(9, desarrollador.getGanancias());
            ps.setDouble(10, desarrollador.getValoracionGeneral());
            ps.setInt(11, desarrollador.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Elimina desarrollador
    public boolean eliminar(int id) {
        String sql = "DELETE FROM desarrolladores WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================
    // MÉTODOS AUXILIARES DE MAPEO
    // ==========================================

    private Desarrollador mapearDesarrolladorSinCatalogo(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String contrasenia = rs.getString("contrasenia");
        String email = rs.getString("email");
        boolean estado = rs.getBoolean("estado");
        LocalDateTime creacionCuenta = rs.getTimestamp("creacion_cuenta").toLocalDateTime();
        String descripcion = rs.getString("descripcion");
        String nacionalidad = rs.getString("nacionalidad");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");

        Date fechaNacSql = rs.getDate("fecha_nacimiento");
        LocalDate fechaNacimiento = (fechaNacSql != null) ? fechaNacSql.toLocalDate() : null;

        double ganancias = rs.getDouble("ganancias");
        double valoracionGeneral = rs.getDouble("valoracion_general");

        // Usamos el constructor completo pasando un catálogo vacío temporalmente
        return new Desarrollador(id, username, contrasenia, email, estado, creacionCuenta, descripcion, nacionalidad, nombre, apellido, fechaNacimiento, new HashSet<>(), ganancias, valoracionGeneral);
    }

    // Carga los productos que pertenecen a este desarrollador desde la tabla 'productos'
    private Set<Producto> obtenerCatalogoPropio(Connection con, Desarrollador desarrollador) throws SQLException {
        Set<Producto> catalogo = new HashSet<>();
        String sql = "SELECT * FROM productos WHERE desarrollador_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, desarrollador.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String tipo = rs.getString("tipo");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    String descripcion = rs.getString("descripcion");
                    LocalDate fechaLanzamiento = rs.getDate("fecha_lanzamiento").toLocalDate();
                    double pesoGB = rs.getDouble("peso_gb");
                    boolean estado = rs.getBoolean("estado");

                    if ("JUEGO".equalsIgnoreCase(tipo)) {
                        double duracion = rs.getDouble("duracion");
                        int edadMinima = rs.getInt("edad_minima");
                        GenerosJuegos genero = GenerosJuegos.valueOf(rs.getString("genero"));
                        String reqMin = rs.getString("requisitos_minimos");
                        String reqRec = rs.getString("requisitos_recomendados");

                        Juego juego = new Juego(id, desarrollador, descripcion, nombre, fechaLanzamiento,
                                pesoGB, precio, estado, duracion, edadMinima, genero,
                                reqMin, reqRec, new ArrayList<>());
                        catalogo.add(juego);

                    } else if ("DLC".equalsIgnoreCase(tipo)) {
                        TipoContenido contenido = TipoContenido.valueOf(rs.getString("contenido"));
                        int juegoBaseId = rs.getInt("juego_base_id");

                        // Buscamos el juego base para asociar el DLC correctamente
                        Juego juegoBase = buscarJuegoBase(con, juegoBaseId, desarrollador);

                        DLC dlc = new DLC(id, desarrollador, descripcion, nombre, fechaLanzamiento,
                                pesoGB, precio, estado, juegoBase, contenido);
                        catalogo.add(dlc);
                    }
                }
            }
        }
        return catalogo;
    }

    private Juego buscarJuegoBase(Connection con, int juegoId, Desarrollador desarrollador) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id = ? AND tipo = 'JUEGO'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, juegoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    String descripcion = rs.getString("descripcion");
                    LocalDate fechaLanzamiento = rs.getDate("fecha_lanzamiento").toLocalDate();
                    double pesoGB = rs.getDouble("peso_gb");
                    boolean estado = rs.getBoolean("estado");
                    double duracion = rs.getDouble("duracion");
                    int edadMinima = rs.getInt("edad_minima");
                    GenerosJuegos genero = GenerosJuegos.valueOf(rs.getString("genero"));
                    String reqMin = rs.getString("requisitos_minimos");
                    String reqRec = rs.getString("requisitos_recomendados");

                    return new Juego(id, desarrollador, descripcion, nombre, fechaLanzamiento,
                            pesoGB, precio, estado, duracion, edadMinima, genero,
                            reqMin, reqRec, new ArrayList<>());
                }
            }
        }
        return null;
    }
}