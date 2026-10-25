import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Usuario {
    private final String id;
    private String username;
    private String contrasenia;
    private String email;
    private boolean estado = true;
    private final LocalDateTime creacionCuenta = LocalDateTime.now();
    private String descripcion = null;
    private String nacionalidad;
    private String nombre;
    private String apellido;
    private LocalDateTime fechaNacimiento;

    // constructores

    public Usuario(String username, String contrasenia, String email, String nacionalidad, String nombre, String apellido, LocalDateTime fechaNacimiento) {
        if (username == null || username.isBlank()) { // verifica si el username ingresado es null o tiene espacios
            throw new IllegalArgumentException("El usuario no puede estar vacio");
        }
        if (email == null || email.isBlank()) { // verifica si el email ingresado es null o tiene espacios
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        if (!cumpleRequisitosContrasenia(contrasenia)) { //verifica si la constraseña cumple con los requisitos
            throw new IllegalArgumentException("La contrasenia debe tener al menos 8 caracteres y no contener espacios");
        }

        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.contrasenia = contrasenia;
        this.email = email;
        this.nacionalidad = nacionalidad;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Usuario(String id, String username, String contrasenia, String email, boolean estado, String descripcion, String nacionalidad, String nombre, String apellido, LocalDateTime fechaNacimiento) {
        this.id = id;
        this.username = username;
        this.contrasenia = contrasenia;
        this.email = email;
        this.estado = estado;
        this.descripcion = descripcion;
        this.nacionalidad = nacionalidad;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    //getters y setters

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


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDateTime getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateTime fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    //metodos

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

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Usuario)) {
            return false;
        }
        Usuario otra = (Usuario) obj;
        return id.equals(otra.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "\n----------- Usuario ----------\n" +
                "ID: " + id + '\n' +
                "Username: " + username + '\n' +
                "Email: " + email + '\n' +
                "Estado: " + estado + '\n' +
                "Creacion de la Cuenta: " + creacionCuenta + '\n' +
                "-----------------------------\n";
    }

}
