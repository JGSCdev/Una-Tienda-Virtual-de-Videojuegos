public class Administrador extends Gestores {

    public Administrador(String username, String contrasenia, String email, String pin)
    {
        super(username, contrasenia, email, pin);
    }


    public void realizarBackup() {
        System.out.println("Operación de backup pendiente.");
    }

    public void restaurarBackup() {
        System.out.println("Operación de restauración pendiente.");
    }

}