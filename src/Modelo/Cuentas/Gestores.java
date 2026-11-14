package Modelo.Cuentas;

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
    public void agregarUsuario(Modelo.Cuentas.Usuario Modelo.Cuentas.Usuario)
    {
        if (Modelo.Cuentas.Usuario != null) // Si la Modelo.Cuentas.Usuario no es null
        {
            listaUsuarios.put(Modelo.Cuentas.Usuario.getId(), Modelo.Cuentas.Usuario); // Guarda el objeto usando su ID como clave
        }
    }
*/


    public Usuario buscarUsuarioPorID(String id)
    {
        return listaUsuarios.get(id); // get(id) devuelve el objeto Modelo.Cuentas.Usuario asociado a esa clave
    }


    public void modificarUsuario(String id, String username, String email)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Busca la Modelo.Cuentas.Usuario

        if (Usuario != null) {
            Usuario.setUsername(username);
            Usuario.setEmail(email);
        }
        // Si existe, utiliza sus setters para modificar
        // sus atributos
    }


    public void darBajaUsuario(String id)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Primero comprueba que la Modelo.Cuentas.Usuario exista

        if (Usuario != null && Usuario.getEstado()) // Después verifica que esté activa
        {
            Usuario.darDeBaja(); // desactiva la Modelo.Cuentas.Usuario buscada
        }
    }


    public String restablecerContrasenia(Usuario Usuario, String nuevaContrasenia)
    {

        //ver
        return "";
    }

    public void reactivarUsuario(String id)
    {
        Usuario Usuario = buscarUsuarioPorID(id); // Busca la Modelo.Cuentas.Usuario por su ID

        if (Usuario != null && !Usuario.getEstado())  //Comprueba que exista
        {
            Usuario.reactivar();
        }
    }


    @Override
    public String toString() {
        return "Modelo.Cuentas.Gestores{" +
                "contrasenia='" + contrasenia + '\'' +
                ", pin='" + pin + '\'' +
                ", listaUsuarios=" + listaUsuarios +
                '}';
    }
}
