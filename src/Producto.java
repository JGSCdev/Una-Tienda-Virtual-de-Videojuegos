public class Producto {
     private int id;
     private boolean estado;


    public int getId()
    {
        return id;
    }

    public void darDeBaja() {
        this.estado = false;
    }

    public void reactivar() {
        this.estado = true;
    }
}
