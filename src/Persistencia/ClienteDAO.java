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

    public boolean insertar(Cliente cliente) {//Inserta un nuevo cliente en la base de datos.
        String sql = "INSERT INTO clientes (username, contrasenia, email, estado, creacion_cuenta, descripcion, nacionalidad, nombre, apellido, fecha_nacimiento, saldo, tiempo_conectado, fecha_ultima_conexion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {//Utiliza Statement.RETURN_GENERATED_KEYS para capturar el ID autogenerado por la base de datos y asignárselo al objeto cliente en memoria.

            // Asignación de parámetros a la consulta SQL preveniendo Inyección SQL
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
                // Recuperamos el ID autogenerado por la base de datos
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                    }
                }
                return true;// retorna true si la inserción fue exitosa
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;// retorna false en caso contrario.
    }

    public Cliente buscarPorId(int id) {//Busca un cliente por su ID único y carga automáticamente todas sus colecciones asociadas (Biblioteca, Carrito y Amigos).
        String sql = "SELECT * FROM clientes WHERE id = ?";
        Cliente cliente = null;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Mapeamos los datos personales básicos del cliente
                    cliente = mapearCliente(rs);
                    // Cargamos sus colecciones utilizando los métodos específicos
                    cliente.setBiblioteca(obtenerBiblioteca(id));
                    cliente.setCarrito(obtenerCarrito(id));
                    cliente.setAmigos(obtenerAmigos(id));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;// retorna el objeto Cliente completo o null si no se encuentra.
    }

    public List<Cliente> listarTodos() {//Obtiene una lista con todos los clientes registrados en la base de datos, cargando también sus respectivas colecciones para cada uno.
        String sql = "SELECT * FROM clientes";
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = mapearCliente(rs);
                // Cargamos colecciones para cada cliente listado
                cliente.setBiblioteca(obtenerBiblioteca(cliente.getId()));
                cliente.setCarrito(obtenerCarrito(cliente.getId()));
                cliente.setAmigos(obtenerAmigos(cliente.getId()));
                lista.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;//Retorna la lista de objetos Cliente.
    }


    public boolean actualizar(Cliente cliente) { //Actualiza la información personal de un cliente existente en la base de datos.
        String sql = "UPDATE clientes SET username = ?, contrasenia = ?, email = ?, estado = ?, descripcion = ?, nacionalidad = ?, nombre = ?, apellido = ?, fecha_nacimiento = ?, saldo = ?, tiempo_conectado = ?, fecha_ultima_conexion = ? WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getUsername());
            ps.setString(2, cliente.getContrasenia());
            ps.setString(3, cliente.getEmail());
            ps.setBoolean(4, cliente.getEstado());
            ps.setString(5, cliente.getDescripcion());
            ps.setString(6, cliente.getNacionalidad());
            ps.setString(7, cliente.getNombre());
            ps.setString(8, cliente.getApellido());
            ps.setObject(9, cliente.getFechaNacimiento());
            ps.setDouble(10, cliente.getSaldo());
            ps.setDouble(11, cliente.getTiempoConectado());
            ps.setObject(12, cliente.getFechaUltimaConexion());
            ps.setInt(13, cliente.getId());

            return ps.executeUpdate() > 0;//retorna true si la actualización fue exitosa
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;//retorna false en caso contrario
    }






    public boolean eliminar(int id) { // Elimina un cliente de la base de datos según su ID.

        String sql = "DELETE FROM clientes WHERE id = ?"; // (Las restricciones ON DELETE CASCADE en la BD se encargan de limpiar sus relaciones).

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0; // retorna true si se elimino correctamente, false en caso contrario.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // ==========================================
    //    CRUD / ACTUALIZACIÓN - BIBLIOTECA
    // ==========================================

    public boolean agregarJuegoBiblioteca(int clienteId, int productoId) { //Agrega un juego individual a la biblioteca del cliente en la tabla intermedia.
        String sql = "INSERT IGNORE INTO cliente_biblioteca (cliente_id, producto_id) VALUES (?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, productoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminarJuegoBiblioteca(int clienteId, int productoId) { //Elimina un juego específico de la biblioteca del cliente.
        String sql = "DELETE FROM cliente_biblioteca WHERE cliente_id = ? AND producto_id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, productoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarBiblioteca(int clienteId, Set<Producto> nuevaBiblioteca) {
        String sqlDelete = "DELETE FROM cliente_biblioteca WHERE cliente_id = ?"; //Borra los registros anteriores e inserta los nuevos elementos que contenga el Set.
        String sqlInsert = "INSERT IGNORE INTO cliente_biblioteca (cliente_id, producto_id) VALUES (?, ?)";//Sincroniza de forma completa la biblioteca de un cliente usando transacciones.

        Connection con = null;
        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false); // Desactivar autocommit para control transaccional

            // 1. Limpiamos la biblioteca actual en la BD
            try (PreparedStatement psDel = con.prepareStatement(sqlDelete)) {
                psDel.setInt(1, clienteId);
                psDel.executeUpdate();
            }

            // 2. Insertamos los nuevos productos en lote (Batch)
            if (nuevaBiblioteca != null && !nuevaBiblioteca.isEmpty()) {
                try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                    for (Producto p : nuevaBiblioteca) {
                        psIns.setInt(1, clienteId);
                        psIns.setInt(2, p.getId());
                        psIns.addBatch();
                    }
                    psIns.executeBatch();
                }
            }

            con.commit(); // Confirmar transacción exitosa
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } // Revertir en caso de error
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return false;
    }


    public Set<Producto> obtenerBiblioteca(int clienteId) { // Obtiene el conjunto (Set) de productos que componen la biblioteca de un cliente.
        Set<Producto> biblioteca = new HashSet<>();
        // Consulta multitabla (JOIN) para traer el producto y los datos de su desarrollador asociado
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


    // ==========================================
    //    CRUD / ACTUALIZACIÓN - CARRITO
    // ==========================================

    public boolean agregarAlCarrito(int clienteId, int productoId) {//Agrega un producto al carrito de compras del cliente.
        String sql = "INSERT INTO cliente_carrito (cliente_id, producto_id) VALUES (?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, productoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean eliminarDelCarrito(int clienteId, int productoId) {//Elimina un producto específico del carrito.
        String sql = "DELETE FROM cliente_carrito WHERE cliente_id = ? AND producto_id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, productoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean vaciarCarrito(int clienteId) {//Vacía por completo el carrito de compras de un cliente.
        String sql = "DELETE FROM cliente_carrito WHERE cliente_id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarCarrito(int clienteId, List<Producto> nuevoCarrito) {//Sincroniza de forma completa el carrito de compras usando transacciones.
        String sqlDelete = "DELETE FROM cliente_carrito WHERE cliente_id = ?";
        String sqlInsert = "INSERT INTO cliente_carrito (cliente_id, producto_id) VALUES (?, ?)";

        Connection con = null;
        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement psDel = con.prepareStatement(sqlDelete)) {
                psDel.setInt(1, clienteId);
                psDel.executeUpdate();
            }

            if (nuevoCarrito != null && !nuevoCarrito.isEmpty()) {
                try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                    for (Producto p : nuevoCarrito) {
                        psIns.setInt(1, clienteId);
                        psIns.setInt(2, p.getId());
                        psIns.addBatch();
                    }
                    psIns.executeBatch();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return false;
    }

    public List<Producto> obtenerCarrito(int clienteId) {//Obtiene la lista de productos que se encuentran en el carrito del cliente.
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
    //           ACTUALIZACIÓN - AMIGOS
    // ==========================================


    public boolean agregarAmigo(int clienteId, int amigoId) { //Agrega un amigo a la red de contactos del cliente (evitando auto-agregarse).
        if (clienteId == amigoId) return false;
        String sql = "INSERT IGNORE INTO cliente_amigos (cliente_id, amigo_id) VALUES (?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, amigoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean eliminarAmigo(int clienteId, int amigoId) { //Elimina un amigo de la lista de contactos.
        String sql = "DELETE FROM cliente_amigos WHERE cliente_id = ? AND amigo_id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, amigoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarAmigos(int clienteId, Set<Cliente> nuevosAmigos) { //Sincroniza la lista completa de amigos usando transacciones.
        String sqlDelete = "DELETE FROM cliente_amigos WHERE cliente_id = ?";
        String sqlInsert = "INSERT IGNORE INTO cliente_amigos (cliente_id, amigo_id) VALUES (?, ?)";

        Connection con = null;
        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement psDel = con.prepareStatement(sqlDelete)) {
                psDel.setInt(1, clienteId);
                psDel.executeUpdate();
            }

            if (nuevosAmigos != null && !nuevosAmigos.isEmpty()) {
                try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                    for (Cliente amigo : nuevosAmigos) {
                        if (amigo.getId() != clienteId) {
                            psIns.setInt(1, clienteId);
                            psIns.setInt(2, amigo.getId());
                            psIns.addBatch();
                        }
                    }
                    psIns.executeBatch();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return false;
    }

    public TreeSet<Cliente> obtenerAmigos(int clienteId) { //Obtiene el conjunto ordenado (TreeSet) de amigos del cliente.
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


    private Cliente mapearCliente(ResultSet rs) throws SQLException { //Transforma una fila del ResultSet en un objeto de tipo Cliente.
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



    private Producto mapearProductoConDesarrollador(Connection con, ResultSet rs) throws SQLException {//Reconstruye un producto (ya sea un Juego o un DLC) junto con su Desarrollador asociado a partir de los resultados de una consulta SQL con JOIN.
        // 1. Reconstruir el objeto Desarrollador asociado
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

        // 2. Extraer atributos comunes del producto
        int id = rs.getInt("id");
        String tipo = rs.getString("tipo"); // Identifica si es 'JUEGO' o 'DLC'
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio");
        String descripcion = rs.getString("descripcion");
        LocalDate fechaLanzamiento = rs.getDate("fecha_lanzamiento").toLocalDate();
        double pesoGB = rs.getDouble("peso_gb");
        boolean estado = rs.getBoolean("estado");

        // 3. Polimorfismo: instanciar Juego o DLC según corresponda
        if ("JUEGO".equalsIgnoreCase(tipo)) {
            double duracion = rs.getDouble("duracion");
            int edadMinima = rs.getInt("edad_minima");
            GenerosJuegos genero = GenerosJuegos.valueOf(rs.getString("genero"));
            String reqMin = rs.getString("requisitos_minimos");
            String reqRec = rs.getString("requisitos_recomendados");

            List<DLC> dlcs = new ArrayList<>();

            return new Juego(id, desarrollador, descripcion, nombre, fechaLanzamiento, pesoGB, precio, estado, duracion, edadMinima, genero, reqMin, reqRec, dlcs);

        } else {
            // Es un DLC
            TipoContenido contenido = TipoContenido.valueOf(rs.getString("contenido"));
            int juegoBaseId = rs.getInt("juego_base_id");

            // Buscamos el juego base al que pertenece este DLC de forma auxiliar
            Juego juegoBase = buscarJuegoBasePorId(con, juegoBaseId, desarrollador);

            return new DLC(id, desarrollador, descripcion, nombre, fechaLanzamiento, pesoGB, precio, estado, juegoBase, contenido);
        }
    }

    private Juego buscarJuegoBasePorId(Connection con, int juegoId, Desarrollador desarrollador) throws SQLException { //Metodo auxiliar privado para buscar el juego base requerido al reconstruir un DLC.
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