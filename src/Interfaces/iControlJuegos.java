package Interfaces;

import Modelo.DLC;
import Modelo.Juego;

public interface iControlJuegos {

    public boolean darAltaJuego(Juego juego);
    public boolean darAltaDLC(DLC dlc);

    public boolean darBajaJuego(int id);
    public boolean darBajaDLC(int id);

    public boolean modificarJuego(Juego juego);
    public boolean modificarDLC(DLC dlc);




}
