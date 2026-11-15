package Modelo;

import java.util.ArrayList;
import java.util.List;

public final class Soporte extends Gestores {

    private final List<String> quejas;

    public Soporte(String contrasenia, String pin) {
        super(contrasenia, pin);

        this.quejas = new ArrayList<>();
    }

    // Recuperar una cuenta existente de MySQL
    public Soporte(int id, String contrasenia, String pin) {
        super(id, contrasenia, pin);

        this.quejas = new ArrayList<>();
    }

    // -------------------- GESTIÓN DE QUEJAS --------------------

    // Recibir una queja y agregarla a la colección
    public void recibirQueja(String queja) {

        if (queja == null || queja.isBlank()) {  //verifica que la queja no sea nula ni este vacia
            throw new IllegalArgumentException(
                    "La queja no puede estar vacía"
            );
        }

        quejas.add(queja);
    }

    // Consultar las quejas recibidas
    // Devuelve una copia no modificable de la colección
    public List<String> consultarQuejas() {
        return List.copyOf(quejas);
    }

    // Atender una queja y eliminarla de la colección
    // Devuelve true si la queja estaba registrada
    public boolean atenderQueja(String queja) {

        if (queja == null || queja.isBlank()) { //verifica que la queja no sea nula ni este vacia
            throw new IllegalArgumentException(
                    "La queja no puede estar vacía"
            );
        }

        return quejas.remove(queja);
    }
}