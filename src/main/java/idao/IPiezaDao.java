package idao;

import models.Pieza;

import java.util.List;

public interface IPiezaDao extends IDao<Pieza, String> {

    List<Pieza> buscarPorNombre(String nombre);

    // Búsquedas avanzadas (bloque 4.3)
    List<Pieza> buscarPorIntervaloPrecio(double min, double max);

    List<Pieza> buscarPorProveedor(String codigoProveedor);

    List<Pieza> buscarPorProyecto(String codigoProyecto);
}