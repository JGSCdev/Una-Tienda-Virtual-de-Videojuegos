import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Productos implements Comparable<Productos>{

    private String nombre;
    private double precio;
    private String descripcion;
    private LocalDate fecha_Lanzamiento;
    private double peso_GB;
    private Desarrollador desarrollador;
    private int id;
    private boolean estado;

    //constructor
    public Productos(Desarrollador desarrollador, String descripcion, String nombre, double peso_GB, double precio) {
        this.desarrollador = desarrollador;
        this.descripcion = descripcion;
        this.estado = true;
        this.fecha_Lanzamiento = LocalDate.now();
        this.nombre = nombre;
        this.peso_GB = peso_GB;
        this.precio = precio;
    }

    //metodos
    public double precioFinal(double descuento){
        if(descuento <= 0) return precio;
        else return (precio + (precio * (descuento / 100)));
    }

    //GET Y SET
    public Desarrollador getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void darDeAlta()
    {
        this.estado = true;
    }

    public void darDeBaja()
    {
        this.estado = false;
    }

    public LocalDate getFecha_Lanzamiento() {
        return fecha_Lanzamiento;
    }

    public void setFecha_Lanzamiento(LocalDate fecha_Lanzamiento) {
        this.fecha_Lanzamiento = fecha_Lanzamiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPeso_GB() {
        return peso_GB;
    }

    public void setPeso_GB(double peso_GB) {
        this.peso_GB = peso_GB;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }


    //comparator
    @Override
    public int compareTo(Productos otro) {

        if (otro == null) {
            throw new NullPointerException("El producto no puede ser null"); // si llega a ser null, el compare to va a lanzar una NullPointerException
        }

        return Integer.compare(this.getId(), otro.getId());
    }

    //equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Productos productos = (Productos) o;
        return Objects.equals(id, productos.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
