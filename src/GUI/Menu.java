package GUI;

import Enum.GenerosJuegos;
import Enum.TipoContenido;
import Gestion.Gestion_catalogo;
import Interfaces.iControlJuegos;
import Modelo.*;
import Persistencia.AdministradorDAO;
import Persistencia.SoporteDAO;
import Service.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Menú de consola de la tienda virtual.
 * El menú solicita datos y muestra resultados; las reglas de negocio y la
 * persistencia se delegan a los servicios y a Gestion_catalogo.
 */
public class Menu {

    private final Scanner teclado;

    private final ClienteService clienteService;
    private final DesarrolladorService desarrolladorService;
    private final AdministradorService administradorService;
    private final SoporteService soporteService;

    private final Gestion_catalogo gestionCatalogo;
    private final iControlJuegos controlJuegos;

    public Menu() throws SQLException {
        teclado = new Scanner(System.in);

        clienteService = new ClienteService();
        desarrolladorService = new DesarrolladorService();
        administradorService = new AdministradorService(new AdministradorDAO());
        soporteService = new SoporteService(new SoporteDAO());

        gestionCatalogo = new Gestion_catalogo();
        controlJuegos = gestionCatalogo;
    }

    // =====================================================
    //                     MENÚ PRINCIPAL
    // =====================================================

    public void iniciar() {

        int opcion;

        do {
            System.out.println("\n========== TIENDA VIRTUAL ==========");
            System.out.println("1. Iniciar sesión como cliente");
            System.out.println("2. Iniciar sesión como desarrollador");
            System.out.println("3. Iniciar sesión como administrador");
            System.out.println("4. Iniciar sesión como soporte");
            System.out.println("5. Registrarse como cliente");
            System.out.println("6. Registrarse como desarrollador");
            System.out.println("7. Registrar una cuenta de administrador");
            System.out.println("0. Salir");
            System.out.println("====================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> iniciarSesionCliente();
                case 2 -> iniciarSesionDesarrollador();
                case 3 -> iniciarSesionAdministrador();
                case 4 -> iniciarSesionSoporte();
                case 5 -> registrarCliente();
                case 6 -> registrarDesarrollador();
                case 7 -> registrarAdministrador();
                case 0 -> System.out.println("¡Gracias por utilizar nuestra tienda!");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    // =====================================================
    //                    INICIO DE SESIÓN
    // =====================================================

    private void iniciarSesionCliente() {

        System.out.println("\n--- INICIO DE SESIÓN DE CLIENTE ---");
        String username = leerTexto("Username: ");
        String contrasenia = leerTexto("Contraseña: ");

        for (Cliente cliente : clienteService.obtenerTodosLosClientes()) {
            if (cliente.getUsername().equals(username)
                    && cliente.validarContrasenia(contrasenia)
                    && cliente.getEstado()) {

                cliente.registrarConexion();
                try {
                    clienteService.actualizarCliente(cliente);
                } catch (Exception e) {
                    System.out.println("Aviso: no se pudo guardar la fecha de conexión: "
                            + e.getMessage());
                }

                System.out.println("\n¡Bienvenido, " + cliente.getUsername() + "!");
                menuCliente(cliente);
                return;
            }
        }

        System.out.println("Usuario o contraseña incorrectos, o cuenta inactiva.");
    }

    private void iniciarSesionDesarrollador() {

        System.out.println("\n--- INICIO DE SESIÓN DE DESARROLLADOR ---");
        String username = leerTexto("Username: ");
        String contrasenia = leerTexto("Contraseña: ");

        for (Desarrollador desarrollador :
                desarrolladorService.obtenerTodosLosDesarrolladores()) {

            if (desarrollador.getUsername().equals(username)
                    && desarrollador.validarContrasenia(contrasenia)
                    && desarrollador.getEstado()) {

                System.out.println("\n¡Bienvenido, " + desarrollador.getUsername() + "!");
                menuDesarrollador(desarrollador);
                return;
            }
        }

        System.out.println("Usuario o contraseña incorrectos, o cuenta inactiva.");
    }

    private void iniciarSesionAdministrador() {

        System.out.println("\n--- INICIO DE SESIÓN DE ADMINISTRADOR ---");
        int id = leerEntero("ID: ");
        String contrasenia = leerTexto("Contraseña: ");
        String pin = leerTexto("PIN: ");

        try {
            for (Administrador administrador : administradorService.listarTodos()) {
                if (administrador.getId() == id
                        && administrador.getContrasenia().equals(contrasenia)
                        && administrador.getPin().equals(pin)) {

                    System.out.println("\n¡Bienvenido, administrador!");
                    menuAdministrador(administrador);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudieron consultar los administradores: "
                    + e.getMessage());
            return;
        }

        System.out.println("Credenciales incorrectas.");
    }

    private void iniciarSesionSoporte() {

        System.out.println("\n--- INICIO DE SESIÓN DE SOPORTE ---");
        int id = leerEntero("ID: ");
        String contrasenia = leerTexto("Contraseña: ");
        String pin = leerTexto("PIN: ");

        try {
            for (Soporte soporte : soporteService.listarTodos()) {
                if (soporte.getId() == id
                        && soporte.getContrasenia().equals(contrasenia)
                        && soporte.getPin().equals(pin)) {

                    System.out.println("\n¡Bienvenido al área de soporte!");
                    menuSoporte(soporte);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudieron consultar las cuentas de soporte: "
                    + e.getMessage());
            return;
        }

        System.out.println("Credenciales incorrectas.");
    }

    // =====================================================
    //                         REGISTROS
    // =====================================================

    private void registrarCliente() {

        System.out.println("\n--- REGISTRO DE CLIENTE ---");

        try {
            String username = leerTextoObligatorio("Username: ");
            String contrasenia = leerTextoObligatorio("Contraseña: ");
            String email = leerTextoObligatorio("Email: ");
            String nacionalidad = leerTextoObligatorio("Nacionalidad: ");
            String nombre = leerTextoObligatorio("Nombre: ");
            String apellido = leerTextoObligatorio("Apellido: ");
            LocalDate fechaNacimiento = leerFecha("Fecha de nacimiento (AAAA-MM-DD): ");

            Cliente cliente = new Cliente(
                    username, contrasenia, email, nacionalidad,
                    nombre, apellido, fechaNacimiento
            );

            clienteService.registrarCliente(cliente);

            System.out.println("¡Cliente registrado correctamente!");
            System.out.println("Tu ID es: " + cliente.getId());

        } catch (Exception e) {
            System.out.println("No se pudo registrar el cliente: " + e.getMessage());
        }
    }

    private void registrarDesarrollador() {

        System.out.println("\n--- REGISTRO DE DESARROLLADOR ---");

        try {
            String username = leerTextoObligatorio("Username: ");
            String contrasenia = leerTextoObligatorio("Contraseña: ");
            String email = leerTextoObligatorio("Email: ");
            String nacionalidad = leerTextoObligatorio("Nacionalidad: ");
            String nombre = leerTextoObligatorio("Nombre: ");
            String apellido = leerTextoObligatorio("Apellido: ");
            LocalDate fechaNacimiento = leerFecha("Fecha de nacimiento (AAAA-MM-DD): ");

            Desarrollador desarrollador = new Desarrollador(
                    username, contrasenia, email, nacionalidad,
                    nombre, apellido, fechaNacimiento
            );

            desarrolladorService.registrarDesarrollador(desarrollador);

            System.out.println("¡Desarrollador registrado correctamente!");
            System.out.println("Tu ID es: " + desarrollador.getId());

        } catch (Exception e) {
            System.out.println("No se pudo registrar el desarrollador: " + e.getMessage());
        }
    }

    /**
     * El PIN solicitado aquí es el PIN personal de acceso del administrador.
     * El proyecto no define un PIN maestro/de invitación para autorizar altas.
     */
    private void registrarAdministrador() {

        System.out.println("\n--- REGISTRO DE ADMINISTRADOR ---");
        System.out.println("El PIN que ingrese será su credencial personal de acceso.");

        try {
            String contrasenia = leerTextoObligatorio("Contraseña: ");
            String pin = leerTextoObligatorio("PIN personal: ");

            Administrador administrador = new Administrador(contrasenia, pin);

            if (administradorService.insertar(administrador)) {
                System.out.println("Administrador registrado correctamente.");
                System.out.println("Tu ID de administrador es: " + administrador.getId());
            } else {
                System.out.println("No se pudo registrar el administrador.");
            }

        } catch (Exception e) {
            System.out.println("No se pudo registrar el administrador: " + e.getMessage());
        }
    }

    // =====================================================
    //                       MENÚ CLIENTE
    // =====================================================

    private void menuCliente(Cliente cliente) {

        int opcion;

        do {
            System.out.println("\n========== MENÚ CLIENTE ==========");
            System.out.println("1. Ver catálogo completo");
            System.out.println("2. Agregar producto al carrito");
            System.out.println("3. Ver carrito");
            System.out.println("4. Quitar producto del carrito");
            System.out.println("5. Comprar un producto del carrito");
            System.out.println("6. Comprar todo el carrito");
            System.out.println("7. Ver biblioteca");
            System.out.println("8. Agregar saldo");
            System.out.println("9. Enviar una queja a soporte");
            System.out.println("10. Ver amigos");
            System.out.println("11. Agregar amigo");
            System.out.println("12. Eliminar amigo");
            System.out.println("13. Cambiar contraseña");
            System.out.println("0. Cerrar sesión");
            System.out.println("==================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> mostrarCatalogo();
                case 2 -> agregarAlCarrito(cliente);
                case 3 -> mostrarCarrito(cliente);
                case 4 -> quitarDelCarrito(cliente);
                case 5 -> comprarUnProducto(cliente);
                case 6 -> comprarCarritoCompleto(cliente);
                case 7 -> mostrarBiblioteca(cliente);
                case 8 -> agregarSaldo(cliente);
                case 9 -> enviarQueja(cliente);
                case 10 -> mostrarAmigos(cliente);
                case 11 -> agregarAmigo(cliente);
                case 12 -> eliminarAmigo(cliente);
                case 13 -> cambiarContraseniaCliente(cliente);
                case 0 -> System.out.println("Sesión de cliente cerrada.");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void mostrarCatalogo() {

        System.out.println("\n--- CATÁLOGO DISPONIBLE ---");
        List<Producto> productos = gestionCatalogo.obtenerProductosActivos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos disponibles.");
            return;
        }

        for (Producto producto : productos) {
            mostrarProducto(producto);
        }
    }

    private void mostrarProducto(Producto producto) {

        String tipo = producto instanceof Juego ? "JUEGO" : "DLC";
        System.out.println("----------------------------------------");
        System.out.println("Tipo: " + tipo);
        System.out.println("ID: " + producto.getId());
        System.out.println("Nombre: " + producto.getNombre());
        System.out.println("Precio: $" + producto.getPrecio());
        System.out.println("Peso: " + producto.getPesoGB() + " GB");
        System.out.println("Desarrollador: " + producto.getDesarrollador().getUsername());

        if (producto instanceof Juego juego) {
            System.out.println("Género: " + juego.getGenero());
            System.out.println("Duración: " + juego.getDuracion() + " horas");
            System.out.println("Edad mínima: " + juego.getEdadMinima());
        } else if (producto instanceof DLC dlc) {
            System.out.println("Juego base: " + dlc.getJuego().getNombre());
            System.out.println("Contenido: " + dlc.getContenido());
        }

        System.out.println("Descripción: " + producto.getDescripcion());
    }

    private void agregarAlCarrito(Cliente cliente) {

        mostrarCatalogo();
        int id = leerEntero("ID del producto que desea agregar: ");
        Producto producto = gestionCatalogo.buscarProductoPorID(id);

        if (producto == null || !producto.isEstado()) {
            System.out.println("No se encontró un producto disponible con ese ID.");
            return;
        }

        try {
            if (clienteService.agregarProductoAlCarrito(cliente.getId(), producto)) {
                System.out.println("Producto agregado al carrito.");
            } else {
                System.out.println("El producto ya está en el carrito o no puede agregarse.");
            }
        } catch (Exception e) {
            System.out.println("No se pudo agregar el producto: " + e.getMessage());
        }
    }

    private void mostrarCarrito(Cliente cliente) {

        System.out.println("\n--- CARRITO ---");
        List<Producto> carrito = cliente.getCarrito();

        if (carrito.isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }

        double total = 0;
        for (int i = 0; i < carrito.size(); i++) {
            Producto producto = carrito.get(i);
            System.out.println((i + 1) + ". ID " + producto.getId()
                    + " | " + producto.getNombre()
                    + " | $" + producto.getPrecio());
            total += producto.getPrecio();
        }

        System.out.println("Total: $" + total);
        System.out.println("Saldo disponible: $" + cliente.getSaldo());
    }

    private void quitarDelCarrito(Cliente cliente) {

        mostrarCarrito(cliente);
        if (cliente.getCarrito().isEmpty()) {
            return;
        }

        int id = leerEntero("ID del producto que desea quitar: ");
        Producto producto = buscarEnColeccion(cliente.getCarrito(), id);

        if (producto == null) {
            System.out.println("Ese producto no está en el carrito.");
            return;
        }

        try {
            if (clienteService.eliminarProductoDelCarrito(cliente.getId(), producto)) {
                System.out.println("Producto quitado del carrito.");
            } else {
                System.out.println("No se pudo quitar el producto.");
            }
        } catch (Exception e) {
            System.out.println("Error al quitar el producto: " + e.getMessage());
        }
    }

    private void comprarUnProducto(Cliente cliente) {

        mostrarCarrito(cliente);
        if (cliente.getCarrito().isEmpty()) {
            return;
        }

        int id = leerEntero("ID del producto que desea comprar: ");
        Producto producto = buscarEnColeccion(cliente.getCarrito(), id);

        if (producto == null) {
            System.out.println("El producto seleccionado no está en el carrito.");
            return;
        }

        try {
            if (clienteService.comprarProducto(cliente.getId(), producto)) {
                System.out.println("¡Compra realizada! El producto ya está en tu biblioteca.");
                System.out.println("Saldo restante: $" + cliente.getSaldo());
            } else {
                System.out.println("No se pudo realizar la compra. Revisá el saldo, el estado del producto y tu biblioteca.");
            }
        } catch (Exception e) {
            System.out.println("Error al realizar la compra: " + e.getMessage());
        }
    }

    private void comprarCarritoCompleto(Cliente cliente) {

        if (cliente.getCarrito().isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }

        mostrarCarrito(cliente);
        if (!confirmar("¿Confirmás la compra de los productos del carrito? (s/n): ")) {
            System.out.println("Compra cancelada.");
            return;
        }

        // Cada compra individual es transaccional. Si un producto falla, los
        // demás pueden comprarse y se informa cuáles no se pudieron adquirir.
        List<Producto> pendientes = new ArrayList<>(cliente.getCarrito());
        int comprados = 0;
        int rechazados = 0;

        for (Producto producto : pendientes) {
            try {
                if (clienteService.comprarProducto(cliente.getId(), producto)) {
                    comprados++;
                    System.out.println("Comprado: " + producto.getNombre());
                } else {
                    rechazados++;
                    System.out.println("No se pudo comprar: " + producto.getNombre());
                }
            } catch (Exception e) {
                rechazados++;
                System.out.println("Error con " + producto.getNombre() + ": " + e.getMessage());
            }
        }

        System.out.println("Resultado: " + comprados + " compra(s), "
                + rechazados + " producto(s) no comprado(s).");
        System.out.println("Saldo restante: $" + cliente.getSaldo());
    }

    private void mostrarBiblioteca(Cliente cliente) {

        System.out.println("\n--- MI BIBLIOTECA ---");
        if (cliente.getBiblioteca().isEmpty()) {
            System.out.println("Todavía no tenés productos en tu biblioteca.");
            return;
        }

        for (Producto producto : cliente.getBiblioteca()) {
            System.out.println("ID: " + producto.getId()
                    + " | " + producto.getNombre()
                    + " | " + (producto instanceof Juego ? "Juego" : "DLC"));
        }
    }

    private void agregarSaldo(Cliente cliente) {

        double monto = leerDouble("Monto que desea agregar: ");

        try {
            if (clienteService.agregarSaldo(cliente.getId(), monto)) {
                System.out.println("Saldo agregado correctamente.");
                System.out.println("Saldo actual: $" + cliente.getSaldo());
            } else {
                System.out.println("No se pudo agregar el saldo. Verificá el monto y el estado de la cuenta.");
            }
        } catch (Exception e) {
            System.out.println("Error al agregar saldo: " + e.getMessage());
        }
    }

    private void enviarQueja(Cliente cliente) {

        List<Soporte> soportes;
        try {
            soportes = soporteService.listarTodos();
        } catch (Exception e) {
            System.out.println("No se pudo consultar el área de soporte: " + e.getMessage());
            return;
        }

        if (soportes.isEmpty()) {
            System.out.println("No hay cuentas de soporte registradas. No se puede enviar la queja.");
            return;
        }

        System.out.println("\n--- ENVIAR QUEJA ---");
        for (Soporte soporte : soportes) {
            System.out.println("ID de soporte: " + soporte.getId());
        }

        int idSoporte = leerEntero("Seleccione el ID del soporte: ");
        boolean existe = soportes.stream().anyMatch(s -> s.getId() == idSoporte);
        if (!existe) {
            System.out.println("No existe una cuenta de soporte con ese ID.");
            return;
        }

        String detalle = leerTextoObligatorio("Describa su queja: ");
        String queja = "Cliente #" + cliente.getId() + " (" + cliente.getUsername() + "): " + detalle;

        try {
            if (soporteService.insertarQueja(idSoporte, queja)) {
                System.out.println("Tu queja fue enviada correctamente.");
            } else {
                System.out.println("No se pudo registrar la queja.");
            }
        } catch (Exception e) {
            System.out.println("Error al enviar la queja: " + e.getMessage());
        }
    }

    private void mostrarAmigos(Cliente cliente) {

        System.out.println("\n--- MIS AMIGOS ---");
        if (cliente.getAmigos().isEmpty()) {
            System.out.println("Todavía no agregaste amigos.");
            return;
        }

        for (Cliente amigo : cliente.getAmigos()) {
            System.out.println("ID: " + amigo.getId() + " | " + amigo.getUsername());
        }
    }

    private void agregarAmigo(Cliente cliente) {

        int idAmigo = leerEntero("ID del cliente que desea agregar: ");
        try {
            clienteService.agregarAmigo(cliente.getId(), idAmigo);
            System.out.println("Amigo agregado correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo agregar al amigo: " + e.getMessage());
        }
    }

    private void eliminarAmigo(Cliente cliente) {

        mostrarAmigos(cliente);
        if (cliente.getAmigos().isEmpty()) {
            return;
        }

        int idAmigo = leerEntero("ID del amigo que desea eliminar: ");
        try {
            if (clienteService.eliminarAmigo(cliente.getId(), idAmigo)) {
                System.out.println("Amigo eliminado.");
            } else {
                System.out.println("No se pudo eliminar al amigo.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cambiarContraseniaCliente(Cliente cliente) {

        String actual = leerTexto("Contraseña actual: ");
        String nueva = leerTexto("Nueva contraseña: ");

        if (!cliente.cambiarContrasenia(actual, nueva)) {
            System.out.println("No se pudo cambiar la contraseña. Revisá la contraseña actual y la nueva.");
            return;
        }

        try {
            clienteService.actualizarCliente(cliente);
            System.out.println("Contraseña actualizada correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo guardar la nueva contraseña: " + e.getMessage());
        }
    }

    // =====================================================
    //                    MENÚ DESARROLLADOR
    // =====================================================

    private void menuDesarrollador(Desarrollador desarrollador) {

        int opcion;

        do {
            System.out.println("\n======= MENÚ DESARROLLADOR =======");
            System.out.println("1. Ver mi catálogo");
            System.out.println("2. Ver catálogo general");
            System.out.println("3. Publicar un juego");
            System.out.println("4. Publicar un DLC para uno de mis juegos");
            System.out.println("5. Modificar uno de mis productos");
            System.out.println("6. Dar de baja uno de mis productos");
            System.out.println("7. Reactivar uno de mis productos");
            System.out.println("8. Ver ganancias y valoración");
            System.out.println("9. Cambiar contraseña");
            System.out.println("0. Cerrar sesión");
            System.out.println("=================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> mostrarCatalogoDesarrollador(desarrollador);
                case 2 -> mostrarCatalogo();
                case 3 -> publicarJuego(desarrollador);
                case 4 -> publicarDLC(desarrollador);
                case 5 -> modificarProductoDesarrollador(desarrollador);
                case 6 -> cambiarEstadoProductoDesarrollador(desarrollador, false);
                case 7 -> cambiarEstadoProductoDesarrollador(desarrollador, true);
                case 8 -> mostrarEstadisticasDesarrollador(desarrollador);
                case 9 -> cambiarContraseniaDesarrollador(desarrollador);
                case 0 -> System.out.println("Sesión de desarrollador cerrada.");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void mostrarCatalogoDesarrollador(Desarrollador desarrollador) {

        System.out.println("\n--- MI CATÁLOGO ---");
        Collection<Producto> productos = desarrollador.obtenerCatalogoPropio();

        if (productos.isEmpty()) {
            System.out.println("Todavía no publicaste productos.");
            return;
        }

        for (Producto producto : productos) {
            mostrarProducto(producto);
            System.out.println("Estado: " + (producto.isEstado() ? "Activo" : "Inactivo"));
        }
    }

    private void publicarJuego(Desarrollador desarrollador) {

        System.out.println("\n--- PUBLICAR JUEGO ---");
        try {
            String nombre = leerTextoObligatorio("Nombre: ");
            String descripcion = leerTextoObligatorio("Descripción: ");
            double peso = leerDouble("Peso en GB: ");
            double precio = leerDouble("Precio: ");
            double duracion = leerDouble("Duración estimada en horas: ");
            int edadMinima = leerEntero("Edad mínima: ");
            GenerosJuegos genero = seleccionarGenero();
            String requisitosMinimos = leerTextoObligatorio("Requisitos mínimos: ");
            String requisitosRecomendados = leerTextoObligatorio("Requisitos recomendados: ");

            Juego juego = new Juego(
                    desarrollador, descripcion, nombre, peso, precio,
                    duracion, edadMinima, genero,
                    requisitosMinimos, requisitosRecomendados
            );

            if (controlJuegos.darAltaJuego(juego)) {
                System.out.println("Juego publicado correctamente. ID generado: " + juego.getId());
            } else {
                System.out.println("No se pudo publicar el juego. Verificá los datos y el estado de tu cuenta.");
            }

        } catch (Exception e) {
            System.out.println("Error al publicar el juego: " + e.getMessage());
        }
    }

    private void publicarDLC(Desarrollador desarrollador) {

        List<Juego> juegosPropios = new ArrayList<>();
        for (Producto producto : gestionCatalogo.obtenerCatalogo()) {
            if (producto instanceof Juego juego
                    && juego.getDesarrollador().getId() == desarrollador.getId()) {
                juegosPropios.add(juego);
            }
        }

        if (juegosPropios.isEmpty()) {
            System.out.println("Primero necesitás publicar un juego para asociarle un DLC.");
            return;
        }

        System.out.println("\n--- JUEGOS PROPIOS ---");
        for (Juego juego : juegosPropios) {
            System.out.println("ID: " + juego.getId() + " | " + juego.getNombre());
        }

        int idJuego = leerEntero("ID del juego al que pertenecerá el DLC: ");
        Juego juegoBase = null;
        for (Juego juego : juegosPropios) {
            if (juego.getId() == idJuego) {
                juegoBase = juego;
                break;
            }
        }

        if (juegoBase == null) {
            System.out.println("El juego no pertenece a tu catálogo.");
            return;
        }

        try {
            String nombre = leerTextoObligatorio("Nombre del DLC: ");
            String descripcion = leerTextoObligatorio("Descripción: ");
            double peso = leerDouble("Peso en GB: ");
            double precio = leerDouble("Precio: ");
            TipoContenido contenido = seleccionarTipoContenido();

            DLC dlc = new DLC(
                    desarrollador, descripcion, nombre, peso, precio,
                    juegoBase, contenido
            );

            if (controlJuegos.darAltaDLC(dlc)) {
                System.out.println("DLC publicado correctamente. ID generado: " + dlc.getId());
            } else {
                System.out.println("No se pudo publicar el DLC.");
            }

        } catch (Exception e) {
            System.out.println("Error al publicar el DLC: " + e.getMessage());
        }
    }

    private void modificarProductoDesarrollador(Desarrollador desarrollador) {

        mostrarCatalogoDesarrollador(desarrollador);
        int id = leerEntero("ID del producto que desea modificar: ");
        Producto producto = gestionCatalogo.buscarProductoPorID(id);

        if (producto == null || producto.getDesarrollador().getId() != desarrollador.getId()) {
            System.out.println("El producto no existe o no pertenece a tu cuenta.");
            return;
        }

        modificarDatosProducto(producto);
    }

    private void cambiarEstadoProductoDesarrollador(Desarrollador desarrollador,
                                                    boolean activar) {

        mostrarCatalogoDesarrollador(desarrollador);
        int id = leerEntero("ID del producto: ");
        Producto producto = gestionCatalogo.buscarProductoPorID(id);

        if (producto == null || producto.getDesarrollador().getId() != desarrollador.getId()) {
            System.out.println("El producto no existe o no pertenece a tu cuenta.");
            return;
        }

        boolean resultado;
        if (activar) {
            resultado = gestionCatalogo.reactivarProducto(id);
        } else if (producto instanceof Juego) {
            resultado = controlJuegos.darBajaJuego(id);
        } else {
            resultado = controlJuegos.darBajaDLC(id);
        }

        System.out.println(resultado
                ? (activar ? "Producto reactivado." : "Producto dado de baja.")
                : "No se pudo cambiar el estado del producto.");
    }

    private void mostrarEstadisticasDesarrollador(Desarrollador desarrollador) {
        System.out.println("\n--- ESTADÍSTICAS ---");
        System.out.println("Ganancias acumuladas: $" + desarrollador.getGanancias());
        System.out.println("Valoración general: " + desarrollador.getValoracionGeneral());
        System.out.println("Productos publicados: " + desarrollador.obtenerCatalogoPropio().size());
    }

    private void cambiarContraseniaDesarrollador(Desarrollador desarrollador) {

        String actual = leerTexto("Contraseña actual: ");
        String nueva = leerTexto("Nueva contraseña: ");

        if (!desarrollador.cambiarContrasenia(actual, nueva)) {
            System.out.println("No se pudo cambiar la contraseña.");
            return;
        }

        try {
            desarrolladorService.actualizarDesarrollador(desarrollador);
            System.out.println("Contraseña actualizada.");
        } catch (Exception e) {
            System.out.println("No se pudo guardar la contraseña: " + e.getMessage());
        }
    }

    // =====================================================
    //                    MENÚ ADMINISTRADOR
    // =====================================================

    private void menuAdministrador(Administrador administrador) {

        int opcion;

        do {
            System.out.println("\n======= MENÚ ADMINISTRADOR =======");
            System.out.println("1. Ver catálogo");
            System.out.println("2. Publicar un juego para un desarrollador");
            System.out.println("3. Publicar un DLC para un desarrollador");
            System.out.println("4. Dar de baja un juego");
            System.out.println("5. Dar de baja un DLC");
            System.out.println("6. Reactivar un producto");
            System.out.println("7. Modificar un producto");
            System.out.println("8. Eliminar un producto definitivamente");
            System.out.println("9. Realizar backup (simulado)");
            System.out.println("10. Restaurar backup (simulado)");
            System.out.println("11. Cambiar contraseña y PIN");
            System.out.println("0. Cerrar sesión");
            System.out.println("=================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> mostrarCatalogoAdministrador();
                case 2 -> publicarJuegoComoAdministrador();
                case 3 -> publicarDLCComoAdministrador();
                case 4 -> bajaPorTipo(true);
                case 5 -> bajaPorTipo(false);
                case 6 -> reactivarProductoAdministrador();
                case 7 -> modificarProductoAdministrador();
                case 8 -> eliminarProductoAdministrador();
                case 9 -> administrador.realizarBackup();
                case 10 -> administrador.restaurarBackup();
                case 11 -> cambiarCredencialesAdministrador(administrador);
                case 0 -> System.out.println("Sesión de administrador cerrada.");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void mostrarCatalogoAdministrador() {

        System.out.println("\n--- CATÁLOGO COMPLETO (ACTIVOS E INACTIVOS) ---");
        List<Producto> productos = gestionCatalogo.obtenerCatalogo();

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto producto : productos) {
            mostrarProducto(producto);
            System.out.println("Estado: " + (producto.isEstado() ? "Activo" : "Inactivo"));
        }
    }

    private void publicarJuegoComoAdministrador() {

        Desarrollador desarrollador = seleccionarDesarrollador();
        if (desarrollador == null) {
            return;
        }

        System.out.println("El juego se publicará a nombre de " + desarrollador.getUsername());
        crearJuegoParaDesarrollador(desarrollador);
    }

    private void crearJuegoParaDesarrollador(Desarrollador desarrollador) {

        try {
            String nombre = leerTextoObligatorio("Nombre: ");
            String descripcion = leerTextoObligatorio("Descripción: ");
            double peso = leerDouble("Peso en GB: ");
            double precio = leerDouble("Precio: ");
            double duracion = leerDouble("Duración estimada en horas: ");
            int edadMinima = leerEntero("Edad mínima: ");
            GenerosJuegos genero = seleccionarGenero();
            String requisitosMinimos = leerTextoObligatorio("Requisitos mínimos: ");
            String requisitosRecomendados = leerTextoObligatorio("Requisitos recomendados: ");

            Juego juego = new Juego(
                    desarrollador, descripcion, nombre, peso, precio,
                    duracion, edadMinima, genero,
                    requisitosMinimos, requisitosRecomendados
            );

            if (controlJuegos.darAltaJuego(juego)) {
                System.out.println("Juego creado. ID: " + juego.getId());
            } else {
                System.out.println("No se pudo crear el juego.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void publicarDLCComoAdministrador() {

        Desarrollador desarrollador = seleccionarDesarrollador();
        if (desarrollador == null) {
            return;
        }

        List<Juego> juegosPropios = new ArrayList<>();
        for (Producto producto : gestionCatalogo.obtenerCatalogo()) {
            if (producto instanceof Juego juego
                    && juego.getDesarrollador().getId() == desarrollador.getId()) {
                juegosPropios.add(juego);
            }
        }

        if (juegosPropios.isEmpty()) {
            System.out.println("Ese desarrollador todavía no tiene juegos para asociar un DLC.");
            return;
        }

        for (Juego juego : juegosPropios) {
            System.out.println("ID: " + juego.getId() + " | " + juego.getNombre());
        }

        int idJuego = leerEntero("ID del juego base: ");
        Juego juegoBase = null;
        for (Juego juego : juegosPropios) {
            if (juego.getId() == idJuego) {
                juegoBase = juego;
                break;
            }
        }

        if (juegoBase == null) {
            System.out.println("El juego no pertenece al desarrollador seleccionado.");
            return;
        }

        try {
            String nombre = leerTextoObligatorio("Nombre del DLC: ");
            String descripcion = leerTextoObligatorio("Descripción: ");
            double peso = leerDouble("Peso en GB: ");
            double precio = leerDouble("Precio: ");
            TipoContenido contenido = seleccionarTipoContenido();

            DLC dlc = new DLC(
                    desarrollador, descripcion, nombre, peso, precio,
                    juegoBase, contenido
            );

            if (controlJuegos.darAltaDLC(dlc)) {
                System.out.println("DLC creado. ID: " + dlc.getId());
            } else {
                System.out.println("No se pudo crear el DLC.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void bajaPorTipo(boolean juego) {

        mostrarCatalogoAdministrador();
        int id = leerEntero("ID del producto: ");
        Producto producto = gestionCatalogo.buscarProductoPorID(id);

        if (producto == null || (juego && !(producto instanceof Juego))
                || (!juego && !(producto instanceof DLC))) {
            System.out.println("No se encontró un producto del tipo seleccionado.");
            return;
        }

        boolean resultado = juego
                ? controlJuegos.darBajaJuego(id)
                : controlJuegos.darBajaDLC(id);

        System.out.println(resultado ? "Producto dado de baja." : "No se pudo dar de baja.");
    }

    private void reactivarProductoAdministrador() {

        mostrarCatalogoAdministrador();
        int id = leerEntero("ID del producto que desea reactivar: ");
        boolean resultado = gestionCatalogo.reactivarProducto(id);
        System.out.println(resultado ? "Producto reactivado." : "No se pudo reactivar el producto.");
    }

    private void modificarProductoAdministrador() {

        mostrarCatalogoAdministrador();
        int id = leerEntero("ID del producto que desea modificar: ");
        Producto producto = gestionCatalogo.buscarProductoPorID(id);

        if (producto == null) {
            System.out.println("No existe un producto con ese ID.");
            return;
        }

        modificarDatosProducto(producto);
    }

    /** Modifica los datos disponibles del producto seleccionado. */
    private void modificarDatosProducto(Producto producto) {

        System.out.println("Ingrese los nuevos datos. No deje campos vacíos.");
        try {
            producto.setNombre(leerTextoObligatorio("Nuevo nombre: "));
            producto.setDescripcion(leerTextoObligatorio("Nueva descripción: "));
            producto.setPrecio(leerDouble("Nuevo precio: "));
            producto.setPesoGB(leerDouble("Nuevo peso en GB: "));

            boolean resultado;
            if (producto instanceof Juego juego) {
                juego.setGenero(seleccionarGenero());
                juego.setDuracion(leerDouble("Nueva duración estimada en horas: "));
                juego.setEdadMinima(leerEntero("Nueva edad mínima: "));
                juego.setRequisitosMinimos(leerTextoObligatorio("Nuevos requisitos mínimos: "));
                juego.setRequisitosRecomendados(
                        leerTextoObligatorio("Nuevos requisitos recomendados: ")
                );
                resultado = controlJuegos.modificarJuego(juego);
            } else if (producto instanceof DLC dlc) {
                dlc.setContenido(seleccionarTipoContenido());
                resultado = controlJuegos.modificarDLC(dlc);
            } else {
                resultado = false;
            }

            System.out.println(resultado
                    ? "Producto modificado correctamente."
                    : "No se pudo persistir la modificación.");

        } catch (Exception e) {
            System.out.println("No se pudo modificar el producto: " + e.getMessage());
        }
    }

    private void eliminarProductoAdministrador() {

        mostrarCatalogoAdministrador();
        int id = leerEntero("ID del producto que desea eliminar definitivamente: ");
        Producto producto = gestionCatalogo.buscarProductoPorID(id);

        if (producto == null) {
            System.out.println("No existe un producto con ese ID.");
            return;
        }

        System.out.println("ATENCIÓN: esta operación elimina el registro de la base de datos.");
        if (!confirmar("¿Está seguro? (s/n): ")) {
            System.out.println("Operación cancelada.");
            return;
        }

        boolean resultado = gestionCatalogo.eliminarProducto(id);
        System.out.println(resultado ? "Producto eliminado." : "No se pudo eliminar el producto.");
    }

    private Desarrollador seleccionarDesarrollador() {

        Collection<Desarrollador> desarrolladores =
                desarrolladorService.obtenerTodosLosDesarrolladores();

        if (desarrolladores.isEmpty()) {
            System.out.println("No hay desarrolladores registrados.");
            return null;
        }

        System.out.println("\n--- DESARROLLADORES ---");
        for (Desarrollador desarrollador : desarrolladores) {
            System.out.println("ID: " + desarrollador.getId()
                    + " | " + desarrollador.getUsername()
                    + " | Estado: " + (desarrollador.getEstado() ? "Activo" : "Inactivo"));
        }

        int id = leerEntero("ID del desarrollador: ");
        for (Desarrollador desarrollador : desarrolladores) {
            if (desarrollador.getId() == id && desarrollador.getEstado()) {
                return desarrollador;
            }
        }

        System.out.println("No se encontró un desarrollador activo con ese ID.");
        return null;
    }

    private void cambiarCredencialesAdministrador(Administrador administrador) {

        String contraseniaActual = leerTexto("Contraseña actual: ");
        String pinActual = leerTexto("PIN actual: ");

        if (!administrador.getContrasenia().equals(contraseniaActual)
                || !administrador.getPin().equals(pinActual)) {
            System.out.println("Credenciales actuales incorrectas.");
            return;
        }

        String nuevaContrasenia = leerTextoObligatorio("Nueva contraseña: ");
        String nuevoPin = leerTextoObligatorio("Nuevo PIN: ");

        try {
            boolean cambioContrasenia = administradorService.actualizarContrasenia(
                    administrador.getId(), nuevaContrasenia
            );
            boolean cambioPin = administradorService.actualizarPin(
                    administrador.getId(), nuevoPin
            );

            if (cambioContrasenia && cambioPin) {
                System.out.println("Contraseña y PIN actualizados.");
            } else {
                System.out.println("No se pudieron actualizar ambas credenciales. Revisá el estado en la base de datos.");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar credenciales: " + e.getMessage());
        }
    }

    // =====================================================
    //                        MENÚ SOPORTE
    // =====================================================

    private void menuSoporte(Soporte soporte) {

        int opcion;

        do {
            System.out.println("\n========== MENÚ SOPORTE ==========");
            System.out.println("1. Consultar mis quejas pendientes");
            System.out.println("2. Atender una queja");
            System.out.println("3. Cambiar contraseña");
            System.out.println("4. Cambiar PIN");
            System.out.println("0. Cerrar sesión");
            System.out.println("==================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> consultarQuejas(soporte);
                case 2 -> atenderQueja(soporte);
                case 3 -> cambiarCredencialSoporte(soporte, true);
                case 4 -> cambiarCredencialSoporte(soporte, false);
                case 0 -> System.out.println("Sesión de soporte cerrada.");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private List<String> consultarQuejas(Soporte soporte) {

        List<String> quejas = soporteService.consultarQuejas(soporte.getId());
        System.out.println("\n--- QUEJAS PENDIENTES ---");

        if (quejas.isEmpty()) {
            System.out.println("No hay quejas pendientes.");
            return quejas;
        }

        for (int i = 0; i < quejas.size(); i++) {
            System.out.println((i + 1) + ". " + quejas.get(i));
        }

        return quejas;
    }

    private void atenderQueja(Soporte soporte) {

        List<String> quejas = consultarQuejas(soporte);
        if (quejas.isEmpty()) {
            return;
        }

        int numero = leerEntero("Número de la queja que desea atender: ");
        if (numero < 1 || numero > quejas.size()) {
            System.out.println("Número de queja inválido.");
            return;
        }

        String queja = quejas.get(numero - 1);
        try {
            if (soporteService.atenderQueja(soporte.getId(), queja)) {
                System.out.println("Queja atendida correctamente.");
            } else {
                System.out.println("No se pudo atender la queja.");
            }
        } catch (Exception e) {
            System.out.println("Error al atender la queja: " + e.getMessage());
        }
    }

    private void cambiarCredencialSoporte(Soporte soporte, boolean cambiarContrasenia) {

        String actual = leerTexto(cambiarContrasenia ? "Contraseña actual: " : "PIN actual: ");
        String guardada = cambiarContrasenia
                ? soporte.getContrasenia()
                : soporte.getPin();

        if (!guardada.equals(actual)) {
            System.out.println("El dato actual es incorrecto.");
            return;
        }

        String nuevo = leerTextoObligatorio(
                cambiarContrasenia ? "Nueva contraseña: " : "Nuevo PIN: "
        );

        try {
            boolean resultado = cambiarContrasenia
                    ? soporteService.actualizarContrasenia(soporte.getId(), nuevo)
                    : soporteService.actualizarPin(soporte.getId(), nuevo);

            System.out.println(resultado ? "Dato actualizado." : "No se pudo actualizar el dato.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // =====================================================
    //                      MÉTODOS AUXILIARES
    // =====================================================

    private Producto buscarEnColeccion(Collection<Producto> productos, int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    private GenerosJuegos seleccionarGenero() {
        GenerosJuegos[] generos = GenerosJuegos.values();
        System.out.println("\nSeleccione un género:");
        for (int i = 0; i < generos.length; i++) {
            System.out.println((i + 1) + ". " + generos[i]);
        }
        return generos[leerOpcionEnRango("Opción: ", 1, generos.length) - 1];
    }

    private TipoContenido seleccionarTipoContenido() {
        TipoContenido[] tipos = TipoContenido.values();
        System.out.println("\nSeleccione el tipo de contenido:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i]);
        }
        return tipos[leerOpcionEnRango("Opción: ", 1, tipos.length) - 1];
    }

    private int leerOpcionEnRango(String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("Ingrese un valor entre " + minimo + " y " + maximo + ".");
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje));
            } catch (Exception e) {
                System.out.println("Fecha inválida. Utilice el formato AAAA-MM-DD.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private String leerTextoObligatorio(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (!texto.isBlank()) {
                return texto;
            }
            System.out.println("El campo no puede quedar vacío.");
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                String valor = leerTexto(mensaje).replace(',', '.');
                double numero = Double.parseDouble(valor);
                if (Double.isFinite(numero)) {
                    return numero;
                }
            } catch (NumberFormatException ignored) {
                // Se muestra el mensaje común de validación debajo.
            }
            System.out.println("Debe ingresar un número válido.");
        }
    }

    private boolean confirmar(String mensaje) {
        String respuesta = leerTexto(mensaje);
        return respuesta.equalsIgnoreCase("s")
                || respuesta.equalsIgnoreCase("si")
                || respuesta.equalsIgnoreCase("sí");
    }
}
