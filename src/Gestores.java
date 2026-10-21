
import java.util.HashMap;
import java.util.Map;

public class Gestores extends Cuenta {


    private String pin;
    private Map<String, Cuenta> listaUsuarios;

    public Gestores(String pin)
    {
        super();
        this.pin = pin;
        this.listaUsuarios = new HashMap<>();
    }

    public Gestores()
    {
        super();
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }


    public Map<String, Cuenta> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(Map<String, Cuenta> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }




    //-------------------------------------METODOS---------------------------------------------------

    public void agregarCuenta(Cuenta cuenta)
    {
        if (cuenta != null) // Si la cuenta no es null
        {
            listaUsuarios.put(cuenta.getId(), cuenta); // Guarda el objeto usando su ID como clave
        }
    }


    public Cuenta buscarCuentaPorID(String id)
    {
        return listaUsuarios.get(id); // get(id) devuelve el objeto Cuenta asociado a esa clave
    }


    public void modificarCuenta(String id, String username, String email)
    {
        Cuenta cuenta = buscarCuentaPorID(id); // Busca la cuenta

        if (cuenta != null) {
            cuenta.setUsername(username);
            cuenta.setEmail(email);
        }
       // Si existe, utiliza sus setters para modificar
        // sus atributos

    }


    public void darBajaCuenta(String id)
    {
        Cuenta cuenta = buscarCuentaPorID(id); // Primero comprueba que la cuenta exista

        if (cuenta != null && cuenta.getEstado()) // Después verifica que esté activa
        {
            cuenta.switchEstado(); // switchEstado() invierte el estado
        }
    }


    public String restablecerContrasenia(Cuenta cuenta, String nuevaContrasenia)
    {

        if (cuenta == null) // Si la cuenta existe delega la operación
        {
            return "La cuenta no existe";
        }

        return cuenta.cambiarContrasenia(nuevaContrasenia);
    }


    public void darAltaCuenta(String id)
    {
        Cuenta cuenta = buscarCuentaPorID(id); // Busca la cuenta por su ID

        if (cuenta != null && !cuenta.getEstado())  //Comprueba que exista
        {
            cuenta.switchEstado();
        }
    }


    public void reactivarCuenta(String id)
    {
        darAltaCuenta(id); //Reactiva una cuenta llamando a darAltaCuenta()
    }





}
