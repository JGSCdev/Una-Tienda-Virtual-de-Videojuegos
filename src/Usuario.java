import java.time.LocalDateTime;

public class Usuario extends Cuenta{
    private String descripcion;
    private String nacionalidad;
    private String alias;
    private String nombre;
    private String apellido;
    private LocalDateTime fechaNacimiento;

    // constructores


    public Usuario(String username, String contrasenia, String email, String descripcion, String nacionalidad, String alias, String nombre, String apellido, LocalDateTime fechaNacimiento) {
        super(username, contrasenia, email);
        this.descripcion = descripcion;
        this.nacionalidad = nacionalidad;
        this.alias = alias;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Usuario() {
    }

    //getters y setters


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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    //por definir...


}
