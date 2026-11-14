package Enum;

public enum GenerosJuegos {

    ACCION("Desafios fisicos basados en reflejos rapidos, velocidad y combate en tiempo real."),
    SHOOTERS("Combate a distancia centrado en el uso de armas de fuego o proyectiles."),
    RPG("Control de personajes con progresion de niveles, habilidades y decisiones narrativas."),
    AVENTURA("Enfoque en la historia, exploracion e interaccion con el entorno por encima del combate."),
    ESTRATEGIA("Planificacion tactica y gestion de recursos para vencer al rival por turnos o en tiempo real."),
    SIMULACION("Recreacion realista de actividades, vehiculos, sistemas o profesiones de la vida real."),
    DEPORTES("Adaptacion virtual de disciplinas deportivas reales con sus reglas y fisicas."),
    HORROR("Juegos enfocados en provocar miedo, tension y supervivencia con recursos limitados."),
    PUZZLES("Desafios mentales basados en la logica, el ingenio y la resolucion de acertijos.");

    private String descriccion;

    GenerosJuegos(String descriccion) {
        this.descriccion = descriccion;
    }

    public String getDescriccion() {
        return descriccion;
    }
}