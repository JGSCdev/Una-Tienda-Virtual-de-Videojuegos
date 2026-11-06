package Collections;
import Modelo.Usuario;
import java.util.HashMap;

public class ColeccionGenericaUsuarios <V extends Usuario>{


    private HashMap<String,V> coleccion = new HashMap<>();

    //constructor


    public ColeccionGenericaUsuarios() {
    }

    //getter y setter


    public HashMap<String, V> getColeccion() {
        return coleccion;
    }

    public void setColeccion(HashMap<String, V> coleccion) {
        this.coleccion = coleccion;
    }

    //metodos

    public boolean agregar(String password, V user)
    {
        if( !(user.validarContrasenia(password)) )
        {
            return false;
        }



        coleccion.put(password, user);
        return true;
    }
}
