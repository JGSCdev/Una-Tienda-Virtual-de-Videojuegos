package Modelo;

import Interfaces.iControlJuegos;
import Modelo.DLC;
import Modelo.Juego;
import Service.DLCservice;
import Service.JuegoService;

public final class Administrador extends Gestores implements iControlJuegos {

    // Servicios encargados de administrar juegos y DLC.
    private final JuegoService juegoService;
    private final DLCservice dlcService;

    // Crear una cuenta nueva.
    public Administrador(String contrasenia, String pin) {
        super(contrasenia, pin);

        this.juegoService = new JuegoService();
        this.dlcService = new DLCservice();
    }

    // Constructor vacío.
    public Administrador() {
        super();

        this.juegoService = new JuegoService();
        this.dlcService = new DLCservice();
    }

    // Recuperar una cuenta existente de MySQL.
    public Administrador(int id, String contrasenia, String pin) {
        super(id, contrasenia, pin);

        this.juegoService = new JuegoService();
        this.dlcService = new DLCservice();
    }

    // -------------------- MÉTODOS PROPIOS --------------------

    // Simula la realización de una copia de seguridad.
    // No genera un archivo real ni modifica la base de datos.
    public void realizarBackup() {
        System.out.println(
                "Backup realizado correctamente (operación simulada)."
        );
    }

    // Simula la restauración de una copia de seguridad.
    // No recupera información real.
    public void restaurarBackup() {
        System.out.println(
                "Restauración realizada correctamente (operación simulada)."
        );
    }

    // -------------------- MÉTODOS DE LA INTERFAZ --------------------

    // Dar de alta un juego.
    @Override
    public boolean darAltaJuego(Juego juego) {
        return juegoService.agregar(juego);
    }

    // Dar de alta un DLC.
    @Override
    public boolean darAltaDLC(DLC dlc) {
        return dlcService.agregar(dlc);
    }

    // Dar de baja un juego.
    @Override
    public boolean darBajaJuego(int id) {
        return juegoService.darDeBaja(id);
    }

    // Dar de baja un DLC.
    @Override
    public boolean darBajaDLC(int id) {
        return dlcService.darDeBaja(id);
    }

    // Modificar un juego.
    @Override
    public boolean modificarJuego(Juego juego) {
        return juegoService.modificar(juego);
    }

    // Modificar un DLC.
    @Override
    public boolean modificarDLC(DLC dlc) {
        return dlcService.modificar(dlc);
    }
}