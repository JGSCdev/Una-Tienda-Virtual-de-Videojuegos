package Modelo;
import Interfaces.iControlJuegos;

public final class Administrador extends Gestores implements iControlJuegos {

    public Administrador(String contrasenia, String pin) {
        super(contrasenia, pin);
    }

    public Administrador() {
    }

    public void realizarBackup() {
        System.out.println("Operación de backup pendiente.");
    }

    public void restaurarBackup() {
        System.out.println("Operación de restauración pendiente.");
    }

    @Override
    public boolean darAltaJuego(Juego juego) {

    }

    @Override
    public boolean darAltaDLC(DLC dlc) {

    }

    @Override
    public boolean darBajaJuego(int id) {

    }

    @Override
    public boolean darBajaDLC(int id) {

    }

    @Override
    public boolean modificarJuego(Juego juego) {
        return false;
    }

    @Override
    public boolean modificarDLC(DLC dlc) {
        return false;
    }
}