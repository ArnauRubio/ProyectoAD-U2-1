package idao;

import models.Proyecto;

import java.util.List;

public interface IProyectoDao extends IDao<Proyecto, String> {

    List<Proyecto> buscarPorNombre(String nombre);

    List<Proyecto> buscarPorCiudad(String ciudad);

    // Búsquedas avanzadas (bloque 4.3)
    List<Proyecto> buscarPorProveedor(String codigoProveedor);

    List<Proyecto> buscarPorPieza(String codigoPieza);
}