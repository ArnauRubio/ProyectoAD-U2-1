package idao;

import models.Proveedor;

import java.util.List;

public interface IProveedorDao extends IDao<Proveedor, Integer> {

    /** @return el proveedor con ese código o null si no existe */
    Proveedor buscarPorCodigo(String codigo);

    List<Proveedor> buscarPorNombre(String nombre);

    List<Proveedor> buscarPorDireccion(String direccion);

    // Búsquedas avanzadas (bloque 4.3)
    List<Proveedor> buscarPorApellidos(String apellidos);

    List<Proveedor> buscarPorPieza(String codigoPieza);
}