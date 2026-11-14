package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Soporte extends Gestores {

    private List<String> quejas;

    public Soporte(String contrasenia, String pin) {
        super(contrasenia, pin);
        this.quejas = new ArrayList<>();
    }

    public void recibirQueja(String queja) {
        if (queja == null || queja.isBlank()) {
            throw new IllegalArgumentException("La queja no puede estar vacía");
        }
        quejas.add(queja);
    }

    /*posible cambio en el umodifyable*/

    public List<String> consultarQuejas() {
        return Collections.unmodifiableList(quejas);
    }

    public void atenderQueja(String queja) {
        quejas.remove(queja);
    }

}