public class Administrador extends Gestores implements iControlJuegos{

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
    public void darAltaJuego(Juego juego) {

    }

    @Override
    public void darAltaDLC(DLC dlc) {

    }

    @Override
    public void DarBajaJuego(int id) {

    }

    @Override
    public void modificarJuego(Juego juego) {

    }

    @Override
    public void DarBajaDLC(int id) {

    }

    @Override
    public void modificarDLC(DLC dlc) {

    }
}