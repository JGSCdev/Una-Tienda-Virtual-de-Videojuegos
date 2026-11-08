package Modelo.Productos;

public enum TipoContenido {

    EXPANSION("Contenido grande que amplia la historia, misiones y mecanicas principales."),
    MODOS("Nuevas reglas o variantes de jugabilidad que cambian la forma de jugar."),
    MAPAS("Nuevos escenarios, niveles o zonas explorables para historia o multijugador."),
    EQUIPAMIENTO("Nuevas armas, armaduras o herramientas que aportan variedad jugable."),
    SKINS("Cambios esteticos para personajes, armas o vehiculos sin afectar el rendimiento."),
    TEXTURAS("Paquetes graficos que mejoran la resolucion y el detalle visual del entorno."),
    MUSICA("Pistas de audio adicionales o bandas sonoras oficiales descargables.");

    private String descriccion;

    TipoContenido(String descriccion) {
        this.descriccion = descriccion;
    }

    public String getDescriccion() {
        return descriccion;
    }

}
