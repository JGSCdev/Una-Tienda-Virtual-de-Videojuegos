import java.time.LocalDateTime;

public class Cuenta {
    private String id;
    private static Integer contadorID = 0;
    private String username;
    private String contrasenia;
    private String email;
    private boolean estado = true;
    private LocalDateTime creacionCuenta;

    //constructores

    //por defecto de cuenta


    public Cuenta(String username, String contrasenia, String email) {
        this.username = username;
        this.contrasenia = contrasenia;
        this.email = email;
        this.creacionCuenta = LocalDateTime.now();
        contadorID++;
        id = contadorID.toString();
    }

    public Cuenta() {
        contadorID++;
        id = contadorID.toString();
    }

    //Getters y setters
    public LocalDateTime getCreacionCuenta() {
        return creacionCuenta;
    }

    public boolean getEstado() {
        return estado;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static Integer getContadorID() {
        return contadorID;
    }

    public String getId() {
        return id;
    }

    // metodos

    public boolean validarConstrasenia(String aux)
    {
        if (aux == null) // valida si es nulo
        {
            return false;
        }

        if(aux.equals(contrasenia)) // valída si la constraseña ingresada es igual a la ya existente
        {
            return false;// en caso de no ser asi, retorna falso
        }

        if(aux.length() < 8 || aux.isBlank()) // valida si el tamaño de la contraseña nueva
                                // es menor a 8 o si la constraseña esta tiene solo espacios
            {
                return false;// en caso de no ser asi, retorna falso
            }

        if(aux.contains(" ")) // verifica si la contraseña tiene estapacios en blanco
        {
            return false;
        }

        return true; // si pasa los 2 filtros de verificacion devuelve verdadero
    }

    public String cambiarContrasenia(String contNueva)
    {
        if(validarConstrasenia(contNueva))
        {
            contrasenia = contNueva;
            return "contrasenia cambiado con exito";
        }

        return "la contrasenia no se pudo cambiar...";
    }

    public void switchEstado()
    {
        estado = !estado;
    }


}
