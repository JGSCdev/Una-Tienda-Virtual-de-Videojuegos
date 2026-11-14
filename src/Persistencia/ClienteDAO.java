package Persistencia;
import Modelo.Cliente;
import Modelo.Producto;
import Conexion.Conexion;
import Modelo.Desarrollador;
import Modelo.Juego;
import Modelo.DLC;
import Enum.GenerosJuegos;
import Enum.TipoContenido;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ClienteDAO {

    // ==========================================
    //         CRUD BÁSICO DE CLIENTES
    // ==========================================

    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (username, contrasenia, email, estado, creacion_cuenta, descripcion, nacionalidad, nombre, apellido, fecha_nacimiento, saldo, tiempo_conectado, fecha_ultima_conexion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getUsername());
            ps.setString(2, cliente.getContrasenia());
            ps.setString(3, cliente.getEmail());
            ps.setBoolean(4, cliente.getEstado());
            ps.setObject(5, cliente.getCreacionCuenta());
            ps.setString(6, cliente.getDescripcion());
            ps.setString(7, cliente.getNacionalidad());
            ps.setString(8, cliente.getNombre());
            ps.setString(9, cliente.getApellido());
            ps.setObject(10, cliente.getFechaNacimiento());
            ps.setDouble(11, cliente.getSaldo());
            ps.setDouble(12, cliente.getTiempoConectado());
            ps.setObject(13, cliente.getFechaUltimaConexion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        Cliente cliente = null;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = mapearCliente(rs);
                    cliente.setBiblioteca(obtenerBiblioteca(id));
                    cliente.setCarrito(obtenerCarrito(id));
                    cliente.setAmigos(obtenerAmigos(id));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    // ==========================================
    // GESTIÓN DE BIBLIOTECA Y CARRITO (PRODUCTOS)
    // ==========================================

    public Set<Producto> obtenerBiblioteca(int clienteId) {
        Set<Producto> biblioteca = new HashSet<>();
        String sql = "SELECT p.*, d.id AS dev_id, d.username AS dev_user, d.contrasenia AS dev_pass, " +
                "d.email AS dev_email, d.estado AS dev_estado, d.creacion_cuenta AS dev_creacion, " +
                "d.descripcion AS dev_desc, d.nacionalidad AS dev_nac, d.nombre AS dev_nom, " +
                "d.apellido AS dev_ape, d.fecha_nacimiento AS dev_fnac, d.ganancias AS dev_gan, " +
                "d.valoracion_general AS dev_val FROM productos p " +
                "JOIN cliente_biblioteca cb ON p.id = cb.producto_id " +
                "JOIN desarrolladores d ON p.desarrollador_id = d.id WHERE cb.cliente_id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    biblioteca.add(mapearProductoConDesarrollador(con, rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return biblioteca;
    }

    public List<Producto> obtenerCarrito(int clienteId) {
        List<Producto> carrito = new ArrayList<>();
        String sql = "SELECT p.*, d.id AS dev_id, d.username AS dev_user, d.contrasenia AS dev_pass, " +
                "d.email AS dev_email, d.estado AS dev_estado, d.creacion_cuenta AS dev_creacion, " +
                "d.descripcion AS dev_desc, d.nacionalidad AS dev_nac, d.nombre AS dev_nom, " +
                "d.apellido AS dev_ape, d.fecha_nacimiento AS dev_fnac, d.ganancias AS dev_gan, " +
                "d.valoracion_general AS dev_val FROM productos p " +
                "JOIN cliente_carrito cc ON p.id = cc.producto_id " +
                "JOIN desarrolladores d ON p.desarrollador_id = d.id WHERE cc.cliente_id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    carrito.add(mapearProductoConDesarrollador(con, rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carrito;
    }

    // ==========================================
    //              GESTIÓN DE AMIGOS
    // ==========================================

    public TreeSet<Cliente> obtenerAmigos(int clienteId) {
        TreeSet<Cliente> amigos = new TreeSet<>();
        String sql = "SELECT c.* FROM clientes c JOIN cliente_amigos ca ON c.id = ca.amigo_id WHERE ca.cliente_id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente amigo = new Cliente();
                    amigo.setId(rs.getInt("id"));
                    amigo.setUsername(rs.getString("username"));
                    amigo.setEmail(rs.getString("email"));
                    amigo.setEstado(rs.getBoolean("estado"));
                    amigos.add(amigo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amigos;
    }

    // ==========================================
    //       MÉTODOS AUXILIARES DE MAPEO
    // ==========================================

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
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

        double saldo = rs.getDouble("saldo");
        double tiempoConectado = rs.getDouble("tiempo_conectado");

        Timestamp ultimaConSql = rs.getTimestamp("fecha_ultima_conexion");
        LocalDateTime fechaUltimaConexion = (ultimaConSql != null) ? ultimaConSql.toLocalDateTime() : LocalDateTime.now();

        return new Cliente(id, username, contrasenia, email, estado, creacionCuenta, descripcion, nacionalidad, nombre, apellido, fechaNacimiento, null, saldo, null, tiempoConectado, null, fechaUltimaConexion);
    }

    private Producto mapearProductoConDesarrollador(Connection con, ResultSet rs) throws SQLException {
        // 1. Reconstruir el Desarrollador asociado
        int devId = rs.getInt("dev_id");
        String devUser = rs.getString("dev_user");
        String devPass = rs.getString("dev_pass");
        String devEmail = rs.getString("dev_email");
        boolean devEstado = rs.getBoolean("dev_estado");
        LocalDateTime devCreacion = rs.getTimestamp("dev_creacion").toLocalDateTime();
        String devDesc = rs.getString("dev_desc");
        String devNac = rs.getString("dev_nac");
        String devNom = rs.getString("dev_nom");
        String devApe = rs.getString("dev_ape");
        LocalDate devFnac = rs.getDate("dev_fnac").toLocalDate();
        double devGan = rs.getDouble("dev_gan");
        double devVal = rs.getDouble("dev_val");

        Desarrollador desarrollador = new Desarrollador(devId, devUser, devPass, devEmail, devEstado, devCreacion, devDesc, devNac, devNom, devApe, devFnac, null, devGan, devVal);

        // 2. Extraer datos comunes del producto
        int id = rs.getInt("id");
        String tipo = rs.getString("tipo"); // 'JUEGO' o 'DLC'
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio");
        String descripcion = rs.getString("descripcion");
        LocalDate fechaLanzamiento = rs.getDate("fecha_lanzamiento").toLocalDate();
        double pesoGB = rs.getDouble("peso_gb");
        boolean estado = rs.getBoolean("estado");

        // 3. Diferenciar e instanciar usando los constructores protegidos
        if ("JUEGO".equalsIgnoreCase(tipo)) {
            double duracion = rs.getDouble("duracion");
            int edadMinima = rs.getInt("edad_minima");
            GenerosJuegos genero = GenerosJuegos.valueOf(rs.getString("genero"));
            String reqMin = rs.getString("requisitos_minimos");
            String reqRec = rs.getString("requisitos_recomendados");

            // Nota: Los DLCs de este juego se pueden cargar de forma independiente si es necesario
            List<DLC> dlcs = new ArrayList<>();

            return new Juego(id, desarrollador, descripcion, nombre, fechaLanzamiento, pesoGB, precio, estado, duracion, edadMinima, genero, reqMin, reqRec, dlcs);

        } else {
            // Es un DLC
            TipoContenido contenido = TipoContenido.valueOf(rs.getString("contenido"));
            int juegoBaseId = rs.getInt("juego_base_id");

            // Buscamos de manera auxiliar el Juego base al que pertenece este DLC
            Juego juegoBase = buscarJuegoBasePorId(con, juegoBaseId, desarrollador);

            return new DLC(id, desarrollador, descripcion, nombre, fechaLanzamiento, pesoGB, precio, estado, juegoBase, contenido);
        }
    }

    // Metodo auxiliar para buscar el juego base de un DLC al momento de mapearlo
    private Juego buscarJuegoBasePorId(Connection con, int juegoId, Desarrollador desarrollador) throws SQLException {
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

                    return new Juego(id, desarrollador, descripcion, nombre, fechaLanzamiento, pesoGB, precio, estado, duracion, edadMinima, genero, reqMin, reqRec, new ArrayList<>());
                }
            }
        }
        return null;
    }
}