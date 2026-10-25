import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Gestores  {

    private String contrasenia;
    private String pin;
    private Map<String, Usuario> listaUsuarios = new HashMap<>();

    public Gestores(String contrasenia, String pin)
    {
        this.contrasenia = contrasenia;
        this.pin = pin;

    }

    public Gestores() {
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getPin() {
        return pin;
    }

    public Map<String, Usuario> getListaUsuarios() {
        return listaUsuarios;
    }



    //-------------------------------------METODOS---------------------------------------------------

    public void agregarUsuario(Usuario Usuario)
    {
        if (Usuario != null) // Si la Usuario no es null
        {
            listaUsuarios.put(Usuario.getId(), Usuario); // Guarda el objeto usando su ID como clave
        }
    }


    public Usuario buscarUsuarioPorID(String id)
    {
        return listaUsuarios.get(id); // get(id) devuelve el objeto Usuario asociado a esa clave
    }


    public void modificarUsuario(String id, String username, String email)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Busca la Usuario

        if (Usuario != null) {
            Usuario.setUsername(username);
            Usuario.setEmail(email);
        }
        // Si existe, utiliza sus setters para modificar
        // sus atributos

    }


    public void darBajaUsuario(String id)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Primero comprueba que la Usuario exista

        if (Usuario != null && Usuario.getEstado()) // Después verifica que esté activa
        {
            Usuario.desactivar(); // desactiva la Usuario buscada
        }
    }


    public String restablecerContrasenia(Usuario Usuario, String nuevaContrasenia)
    {

        //ver
        return "";
    }



    public void reactivarUsuario(String id)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Busca la Usuario por su ID

        if (Usuario != null && !Usuario.getEstado())  //Comprueba que exista
        {
            Usuario.reactivar();
        }
    }


    public void reactivarUsuario(String id)
    {
        darAltaUsuario(id); //Reactiva una Usuario llamando a darAltaUsuario()
    }


    @Override
    public String toString() {
        return super.toString();
    }




}
