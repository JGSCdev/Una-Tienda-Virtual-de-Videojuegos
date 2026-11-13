public interface iControlJuegos {

    public void darAltaJuego(Juegos juego);
    public void darAltaDLC(DLCs dlc);
    public void DarBajaJuego(int id);
    public void DarBajaDLC(int id);
    public boolean modificarJuego(Juegos juego);
    public boolean modificarDLC(DLCs dlc);

}
