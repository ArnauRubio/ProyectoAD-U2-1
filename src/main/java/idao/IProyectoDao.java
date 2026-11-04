package idao;

import models.Proyecto;

import java.util.List;

public interface IProyectoDao extends IDao<Proyecto, Integer> {

    /** @return el proyecto con ese código o null si no existe */
    Proyecto buscarPorCodigo(String codigo);

    List<Proyecto> buscarPorNombre(String nombre);

    List<Proyecto> buscarPorCiudad(String ciudad);

    // Búsquedas avanzadas (bloque 4.3)
    List<Proyecto> buscarPorProveedor(String codigoProveedor);

    List<Proyecto> buscarPorPieza(String codigoPieza);
}