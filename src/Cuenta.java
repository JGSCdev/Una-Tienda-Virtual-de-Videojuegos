import java.time.LocalDateTime;
import java.util.UUID;

public class Cuenta {
    private final String id;
    private String username;
    private String contrasenia;
    private String email;
    private boolean estado;
    private final LocalDateTime creacionCuenta;

    // Constructor
    public Cuenta(String username, String contrasenia, String email) throws IllegalArgumentException{
        if (username == null || username.isBlank()) { // verifica si el username ingresado es null o tiene espacios
            throw new IllegalArgumentException("El usuario no puede estar vacio");
        }
        if (email == null || email.isBlank()) { // verifica si el email ingresado es null o tiene espacios
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        if (!cumpleRequisitosContrasenia(contrasenia)) { //verifica si la constraseña cumple con los requisitos
            throw new IllegalArgumentException("La contrasenia debe tener al menos 8 caracteres y no contener espacios");
        }

        //usamos la excepcion de IllegalArgumentException para mostrar que el usuario puso mal alguno de los 3 parametros

        this.id = UUID.randomUUID().toString(); // genera un uuid unico para cada cuenta en general
        this.username = username;
        this.contrasenia = contrasenia;
        this.email = email;
        this.estado = true; // por defecto
        this.creacionCuenta = LocalDateTime.now();
    }

    // Recuperar una cuenta existente de la base de datos.
    // Este constructor NO crea otro ID ni cambia la fecha original.
    protected Cuenta(String id, String username, String contrasenia,
                     String email, boolean estado, LocalDateTime creacionCuenta) {
        this.id = id;
        this.username = username;
        this.contrasenia = contrasenia;
        this.email = email;
        this.estado = estado;
        this.creacionCuenta = creacionCuenta;
    }

    private boolean cumpleRequisitosContrasenia(String clave) { //comprueba los requisitos que necesita la contraseña para ser correcta

        if (clave == null) { // si es nula
            return false;
        }
        if (clave.length() < 10) { // comprueba si su longitud es mayor a 10
            return false;
        }
        if (clave.isBlank() || clave.contains(" ")) { // verifica si contiene espacios en blanco
            return false;
        }
        return true;
    }

    public boolean validarContrasenia(String ingresada) { //este metodo se va a usar mas que nada en el inicio de sesion

        return ingresada != null && ingresada.equals(contrasenia);
    }

    public boolean validarContraseniaNueva(String nueva) {
        if (!cumpleRequisitosContrasenia(nueva)) {  // usa el metodo para verificar la contraseña
            return false;
        }
        if (nueva.equals(contrasenia)) { // revisa si la contraseña nueva es igual a la que se quiere cambiar
            return false;
        }
        return true;
    }

    public boolean cambiarContrasenia(String actual, String nueva) {
        if (!estado) { //primero se fija si esta activa la cuenta
            return false;
        }
        if (!validarContrasenia(actual)) { // despues valida si la contraseña puesta como actual es realmente la actual
            return false;
        }
        if (!validarContraseniaNueva(nueva)) { // antes de terminar lavida la contraseña nueva
            return false;
        }

        // si logro pasar todos los filtros se cambia la contraseña
        this.contrasenia = nueva;
        return true;
    }

    public void darDeBaja() {
        estado = false;
    }

    public void reactivar() {
        estado = true;
    }

    //Getters y setters

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public boolean getEstado() {
        return estado;
    }
    public LocalDateTime getCreacionCuenta() {
        return creacionCuenta;
    }

    public void setUsername(String username) throws IllegalArgumentException {
        if (username == null || username.isBlank()) { // aca valida si no es nulo y si tiene espacios en blanco
            throw new IllegalArgumentException("El usuario no puede estar vacio");
        }
        this.username = username;
    }

    public void setEmail(String email) throws IllegalArgumentException{
        if (email == null || email.isBlank()) { // aca valida si no es nulo y si tiene espacios en blanco
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        this.email = email;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cuenta)) {
            return false;
        }
        Cuenta otra = (Cuenta) obj;
        return id.equals(otra.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "\n----------- Cuenta ----------\n" +
                "ID: " + id + '\n' +
                "Username: " + username + '\n' +
                "Email: " + email + '\n' +
                "Estado: " + estado + '\n' +
                "Creacion de la Cuenta: " + creacionCuenta + '\n' +
                "-----------------------------\n";
    }
}
