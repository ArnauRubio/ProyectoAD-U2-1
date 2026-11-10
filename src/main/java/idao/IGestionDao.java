package idao;

import models.Gestion;

import java.time.LocalDate;
import java.util.List;

/** CRUD y búsquedas para gestiones. */
public interface IGestionDao extends IDao<Gestion, Integer> {

    /** @return la gestión con ese código o null si no existe */
    Gestion buscarPorCodigo(String codigo);

    List<Gestion> buscarPorIntervaloFecha(LocalDate desde, LocalDate hasta);

    List<Gestion> buscarPorIntervaloCantidad(Integer min, Integer max);

    List<Gestion> buscarPorProveedor(String codigoProveedor);

    List<Gestion> buscarPorPieza(String codigoPieza);

    List<Gestion> buscarPorProyecto(String codigoProyecto);
}