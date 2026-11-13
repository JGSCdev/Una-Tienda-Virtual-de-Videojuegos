package Modelo;

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


    //queda como posible fururo metodo privado en el que agregar cosas a la lisa, o no

    /*
    public void agregarUsuario(Modelo.Usuario Modelo.Usuario)
    {
        if (Modelo.Usuario != null) // Si la Modelo.Usuario no es null
        {
            listaUsuarios.put(Modelo.Usuario.getId(), Modelo.Usuario); // Guarda el objeto usando su ID como clave
        }
    }
*/


    public Usuario buscarUsuarioPorID(String id)
    {
        return listaUsuarios.get(id); // get(id) devuelve el objeto Modelo.Usuario asociado a esa clave
    }


    public void modificarUsuario(String id, String username, String email)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Busca la Modelo.Usuario

        if (Usuario != null) {
            Usuario.setUsername(username);
            Usuario.setEmail(email);
        }
        // Si existe, utiliza sus setters para modificar
        // sus atributos
    }


    public void darBajaUsuario(String id)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Primero comprueba que la Modelo.Usuario exista

        if (Usuario != null && Usuario.getEstado()) // Después verifica que esté activa
        {
            Usuario.darDeBaja(); // desactiva la Modelo.Usuario buscada
        }
    }


    public String restablecerContrasenia(Usuario Usuario, String nuevaContrasenia)
    {

        //ver
        return "";
    }

    public void reactivarUsuario(String id)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Busca la Modelo.Usuario por su ID

        if (Usuario != null && !Usuario.getEstado())  //Comprueba que exista
        {
            Usuario.reactivar();
        }
    }


    @Override
    public String toString() {
        return "Modelo.Gestores{" +
                "contrasenia='" + contrasenia + '\'' +
                ", pin='" + pin + '\'' +
                ", listaUsuarios=" + listaUsuarios +
                '}';
    }
}
