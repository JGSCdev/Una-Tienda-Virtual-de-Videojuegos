package Interfaces;

import Modelo.DLC;
import Modelo.Juego;

public interface iControlJuegos {

    public void darAltaJuego(Juego juego);
    public void darAltaDLC(DLC dlc);
    public void DarBajaJuego(int id);
    public void DarBajaDLC(int id);
    public boolean modificarJuego(Juego juego);
    public boolean modificarDLC(DLC dlc);

}
