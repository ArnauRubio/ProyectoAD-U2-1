package idao;

import models.Proveedor;

import java.util.List;

public interface IProveedorDao extends IDao<Proveedor, String> {

    List<Proveedor> buscarPorNombre(String nombre);

    List<Proveedor> buscarPorDireccion(String direccion);

    // Búsquedas avanzadas (bloque 4.3)
    List<Proveedor> buscarPorApellidos(String apellidos);

    List<Proveedor> buscarPorPieza(String codigoPieza);
}