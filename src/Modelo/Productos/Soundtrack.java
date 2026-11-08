package Modelo.Productos;

import Modelo.Cuentas.Desarrollador;

import java.time.LocalTime;

public final class Soundtrack extends Productos{

    private LocalTime duracion;

    public Soundtrack(Desarrollador desarrollador, String descripcion, String nombre, double peso_GB, double precio, LocalTime duracion) {
        super(desarrollador, descripcion, nombre, peso_GB, precio);
        this.duracion = duracion;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }


}
