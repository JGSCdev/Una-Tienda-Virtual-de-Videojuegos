package GUI;

import Modelo.*;
import Service.*;

import Persistencia.AdministradorDAO;
import Persistencia.SoporteDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner teclado;

    // Servicios
    private final ClienteService clienteService;
    private final DesarrolladorService desarrolladorService;
    private final JuegoService juegoService;
    private final DLCservice dlcService;
    private final AdministradorService administradorService;
    private final SoporteService soporteService;

    public Menu() throws SQLException {

        teclado = new Scanner(System.in);

        clienteService = new ClienteService();
        desarrolladorService = new DesarrolladorService();
        juegoService = new JuegoService();
        dlcService = new DLCservice();

        administradorService =
                new AdministradorService(new AdministradorDAO());

        soporteService =
                new SoporteService(new SoporteDAO());
    }

    // ==========================================
    //              MENÚ PRINCIPAL
    // ==========================================

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
            System.out.println("0. Salir");
            System.out.println("====================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {

                case 1:
                    iniciarSesionCliente();
                    break;

                case 2:
                    iniciarSesionDesarrollador();
                    break;

                case 3:
                    iniciarSesionAdministrador();
                    break;

                case 4:
                    iniciarSesionSoporte();
                    break;

                case 5:
                    registrarCliente();
                    break;

                case 6:
                    registrarDesarrollador();
                    break;

                case 0:
                    System.out.println("¡Gracias por utilizar nuestra tienda!");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    // ==========================================
    //              INICIO DE SESIÓN
    // ==========================================

    private void iniciarSesionCliente() {

        System.out.println("\n--- INICIO DE SESIÓN ---");

        String username = leerTexto("Username: ");
        String contrasenia = leerTexto("Contraseña: ");

        for (Cliente cliente : clienteService.obtenerTodosLosClientes()) {

            if (cliente.getUsername().equals(username)
                    && cliente.validarContrasenia(contrasenia)
                    && cliente.getEstado()) {

                System.out.println("\n¡Bienvenido, " + cliente.getUsername() + "!");
                menuCliente(cliente);
                return;
            }
        }

        System.out.println("Usuario o contraseña incorrectos.");
    }

    private void iniciarSesionDesarrollador() {

        System.out.println("\n--- INICIO DE SESIÓN ---");

        String username = leerTexto("Username: ");
        String contrasenia = leerTexto("Contraseña: ");

        for (Desarrollador desarrollador :
                desarrolladorService.obtenerTodosLosDesarrolladores()) {

            if (desarrollador.getUsername().equals(username)
                    && desarrollador.validarContrasenia(contrasenia)
                    && desarrollador.getEstado()) {

                System.out.println("\n¡Bienvenido, "
                        + desarrollador.getUsername() + "!");

                menuDesarrollador(desarrollador);
                return;
            }
        }

        System.out.println("Usuario o contraseña incorrectos.");
    }

    private void iniciarSesionAdministrador() {

        System.out.println("\n--- INICIO DE SESIÓN ---");

        int id = leerEntero("ID: ");
        String contrasenia = leerTexto("Contraseña: ");
        String pin = leerTexto("PIN: ");

        for (Administrador administrador :
                administradorService.listarTodos()) {

            if (administrador.getId() == id
                    && administrador.getContrasenia().equals(contrasenia)
                    && administrador.getPin().equals(pin)) {

                System.out.println("\n¡Bienvenido, administrador!");
                menuAdministrador(administrador);
                return;
            }
        }

        System.out.println("Credenciales incorrectas.");
    }

    private void iniciarSesionSoporte() {

        System.out.println("\n--- INICIO DE SESIÓN ---");

        int id = leerEntero("ID: ");
        String contrasenia = leerTexto("Contraseña: ");
        String pin = leerTexto("PIN: ");

        for (Soporte soporte : soporteService.listarTodos()) {

            if (soporte.getId() == id
                    && soporte.getContrasenia().equals(contrasenia)
                    && soporte.getPin().equals(pin)) {

                System.out.println("\n¡Bienvenido al área de soporte!");
                menuSoporte(soporte);
                return;
            }
        }

        System.out.println("Credenciales incorrectas.");
    }

    // ==========================================
    //                REGISTROS
    // ==========================================

    private void registrarCliente() {

        System.out.println("\n--- REGISTRO DE CLIENTE ---");

        try {

            String username = leerTexto("Username: ");
            String contrasenia = leerTexto("Contraseña: ");
            String email = leerTexto("Email: ");
            String nacionalidad = leerTexto("Nacionalidad: ");
            String nombre = leerTexto("Nombre: ");
            String apellido = leerTexto("Apellido: ");

            LocalDate fechaNacimiento = LocalDate.parse(
                    leerTexto("Fecha de nacimiento (AAAA-MM-DD): ")
            );

            Cliente cliente = new Cliente(
                    username,
                    contrasenia,
                    email,
                    nacionalidad,
                    nombre,
                    apellido,
                    fechaNacimiento
            );

            clienteService.registrarCliente(cliente);

            System.out.println("¡Cliente registrado correctamente!");
            System.out.println("Tu ID es: " + cliente.getId());

        } catch (Exception e) {

            System.out.println("No se pudo registrar el cliente: "
                    + e.getMessage());
        }
    }

    private void registrarDesarrollador() {

        System.out.println("\n--- REGISTRO DE DESARROLLADOR ---");

        try {

            String username = leerTexto("Username: ");
            String contrasenia = leerTexto("Contraseña: ");
            String email = leerTexto("Email: ");
            String nacionalidad = leerTexto("Nacionalidad: ");
            String nombre = leerTexto("Nombre: ");
            String apellido = leerTexto("Apellido: ");

            LocalDate fechaNacimiento = LocalDate.parse(
                    leerTexto("Fecha de nacimiento (AAAA-MM-DD): ")
            );

            Desarrollador desarrollador = new Desarrollador(
                    username,
                    contrasenia,
                    email,
                    nacionalidad,
                    nombre,
                    apellido,
                    fechaNacimiento
            );

            desarrolladorService.registrarDesarrollador(desarrollador);

            System.out.println("¡Desarrollador registrado correctamente!");
            System.out.println("Tu ID es: " + desarrollador.getId());

        } catch (Exception e) {

            System.out.println("No se pudo registrar el desarrollador: "
                    + e.getMessage());
        }
    }

    // ==========================================
    //                MENÚ CLIENTE
    // ==========================================

    private void menuCliente(Cliente cliente) {

        int opcion;

        do {

            System.out.println("\n========== MENÚ CLIENTE ==========");
            System.out.println("1. Ver catálogo");
            System.out.println("2. Agregar producto al carrito");
            System.out.println("3. Ver carrito");
            System.out.println("4. Ver biblioteca");
            System.out.println("5. Agregar saldo");
            System.out.println("0. Cerrar sesión");
            System.out.println("==================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {

                case 1:
                    mostrarCatalogo();
                    break;

                case 2:
                    agregarAlCarrito(cliente);
                    break;

                case 3:
                    mostrarCarrito(cliente);
                    break;

                case 4:
                    mostrarBiblioteca(cliente);
                    break;

                case 5:
                    agregarSaldo(cliente);
                    break;

                case 0:
                    System.out.println("Sesión cerrada.");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void mostrarCatalogo() {

        System.out.println("\n--- CATÁLOGO DE JUEGOS ---");

        List<Juego> juegos = juegoService.listarTodos();

        if (juegos.isEmpty()) {
            System.out.println("No hay juegos disponibles.");
        }

        for (Juego juego : juegos) {

            if (juego.isEstado()) {

                System.out.println(
                        "ID: " + juego.getId()
                                + " | Nombre: " + juego.getNombre()
                                + " | Precio: $" + juego.getPrecio()
                );
            }
        }

        System.out.println("\n--- CATÁLOGO DE DLC ---");

        for (DLC dlc : dlcService.listarTodos()) {

            if (dlc.isEstado()) {

                System.out.println(
                        "ID: " + dlc.getId()
                                + " | Nombre: " + dlc.getNombre()
                                + " | Precio: $" + dlc.getPrecio()
                );
            }
        }
    }

    private void agregarAlCarrito(Cliente cliente) {

        mostrarCatalogo();

        int id = leerEntero("\nIngrese el ID del juego: ");

        Juego juego = juegoService.buscarPorId(id);

        if (juego == null || !juego.isEstado()) {
            System.out.println("No se encontró el juego.");
            return;
        }

        try {

            clienteService.agregarProductoAlCarrito(
                    cliente.getId(),
                    juego
            );

            System.out.println("Producto agregado al carrito.");

        } catch (Exception e) {

            System.out.println("No se pudo agregar el producto: "
                    + e.getMessage());
        }
    }

    private void mostrarCarrito(Cliente cliente) {

        System.out.println("\n--- CARRITO ---");

        if (cliente.getCarrito().isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }

        for (Producto producto : cliente.getCarrito()) {

            System.out.println(
                    producto.getNombre()
                            + " | $" + producto.getPrecio()
            );
        }
    }

    private void mostrarBiblioteca(Cliente cliente) {

        System.out.println("\n--- BIBLIOTECA ---");

        if (cliente.getBiblioteca().isEmpty()) {
            System.out.println("Todavía no tenés juegos.");
            return;
        }

        for (Producto producto : cliente.getBiblioteca()) {
            System.out.println(producto.getNombre());
        }
    }

    private void agregarSaldo(Cliente cliente) {

        double monto = leerDouble("Ingrese el monto que desea agregar: ");

        if (cliente.agregarSaldo(monto)) {
            System.out.println("Saldo agregado correctamente.");
            System.out.println("Saldo actual: $" + cliente.getSaldo());
        } else {
            System.out.println("No se pudo agregar el saldo.");
        }
    }

    // ==========================================
    //             MENÚ DESARROLLADOR
    // ==========================================

    private void menuDesarrollador(Desarrollador desarrollador) {

        int opcion;

        do {

            System.out.println("\n======= MENÚ DESARROLLADOR =======");
            System.out.println("1. Ver mi catálogo");
            System.out.println("2. Ver todos los juegos publicados");
            System.out.println("0. Cerrar sesión");
            System.out.println("==================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {

                case 1:
                    mostrarCatalogoDesarrollador(desarrollador);
                    break;

                case 2:
                    mostrarCatalogo();
                    break;

                case 0:
                    System.out.println("Sesión cerrada.");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void mostrarCatalogoDesarrollador(Desarrollador desarrollador) {

        System.out.println("\n--- MI CATÁLOGO ---");

        Collection<Producto> catalogo =
                desarrollador.obtenerCatalogoPropio();

        if (catalogo.isEmpty()) {
            System.out.println("Todavía no publicaste productos.");
            return;
        }

        for (Producto producto : catalogo) {

            System.out.println(
                    "ID: " + producto.getId()
                            + " | Nombre: " + producto.getNombre()
                            + " | Precio: $" + producto.getPrecio()
            );
        }
    }

    // ==========================================
    //             MENÚ ADMINISTRADOR
    // ==========================================

    private void menuAdministrador(Administrador administrador) {

        int opcion;

        do {

            System.out.println("\n======= MENÚ ADMINISTRADOR =======");
            System.out.println("1. Ver catálogo");
            System.out.println("2. Dar de baja un juego");
            System.out.println("3. Reactivar un juego");
            System.out.println("4. Dar de baja un DLC");
            System.out.println("5. Reactivar un DLC");
            System.out.println("6. Realizar backup");
            System.out.println("7. Restaurar backup");
            System.out.println("0. Cerrar sesión");
            System.out.println("==================================");

            opcion = leerEntero("Seleccione una opción: ");

            int id;

            switch (opcion) {

                case 1:
                    mostrarCatalogo();
                    break;

                case 2:
                    id = leerEntero("ID del juego: ");

                    if (juegoService.darDeBaja(id)) {
                        System.out.println("Juego dado de baja.");
                    } else {
                        System.out.println("No se pudo dar de baja.");
                    }
                    break;

                case 3:
                    id = leerEntero("ID del juego: ");

                    if (juegoService.darDeAlta(id)) {
                        System.out.println("Juego reactivado.");
                    } else {
                        System.out.println("No se pudo reactivar.");
                    }
                    break;

                case 4:
                    id = leerEntero("ID del DLC: ");

                    if (dlcService.darDeBaja(id)) {
                        System.out.println("DLC dado de baja.");
                    } else {
                        System.out.println("No se pudo dar de baja.");
                    }
                    break;

                case 5:
                    id = leerEntero("ID del DLC: ");

                    if (dlcService.darDeAlta(id)) {
                        System.out.println("DLC reactivado.");
                    } else {
                        System.out.println("No se pudo reactivar.");
                    }
                    break;

                case 6:
                    administrador.realizarBackup();
                    break;

                case 7:
                    administrador.restaurarBackup();
                    break;

                case 0:
                    System.out.println("Sesión cerrada.");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    // ==========================================
    //                MENÚ SOPORTE
    // ==========================================

    private void menuSoporte(Soporte soporte) {

        int opcion;

        do {

            System.out.println("\n========== MENÚ SOPORTE ==========");
            System.out.println("1. Consultar mis quejas");
            System.out.println("2. Atender una queja");
            System.out.println("0. Cerrar sesión");
            System.out.println("==================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {

                case 1:
                    consultarQuejas(soporte);
                    break;

                case 2:
                    atenderQueja(soporte);
                    break;

                case 0:
                    System.out.println("Sesión cerrada.");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void consultarQuejas(Soporte soporte) {

        List<String> quejas =
                soporteService.consultarQuejas(soporte.getId());

        System.out.println("\n--- QUEJAS RECIBIDAS ---");

        if (quejas.isEmpty()) {
            System.out.println("No hay quejas pendientes.");
            return;
        }

        for (int i = 0; i < quejas.size(); i++) {
            System.out.println((i + 1) + ". " + quejas.get(i));
        }
    }

    private void atenderQueja(Soporte soporte) {

        consultarQuejas(soporte);

        String queja = leerTexto(
                "\nEscriba exactamente la queja que desea atender: "
        );

        try {

            if (soporteService.atenderQueja(soporte.getId(), queja)) {
                System.out.println("Queja atendida correctamente.");
            } else {
                System.out.println("No se pudo atender la queja.");
            }

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==========================================
    //           MÉTODOS AUXILIARES
    // ==========================================

    private String leerTexto(String mensaje) {

        System.out.print(mensaje);
        return teclado.nextLine();
    }

    private int leerEntero(String mensaje) {

        while (true) {

            try {

                System.out.print(mensaje);
                return Integer.parseInt(teclado.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    private double leerDouble(String mensaje) {

        while (true) {

            try {

                System.out.print(mensaje);
                return Double.parseDouble(teclado.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Debe ingresar un número válido.");
            }
        }
    }
}
