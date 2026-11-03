package dao;

import idao.IProveedorDao;
import models.Proveedor;

import java.util.List;

public class ProveedorDao extends Dao<Proveedor, String> implements IProveedorDao {

    // ---- Consultas JPQL ----
    private static final String CONTAR_SUMINISTROS =
            "SELECT COUNT(g) FROM Gestion g WHERE g.proveedor.codigo = :cod";
    private static final String POR_NOMBRE =
            "FROM Proveedor p WHERE LOWER(p.nombre) LIKE :v";
    private static final String POR_APELLIDOS =
            "FROM Proveedor p WHERE LOWER(p.apellidos) LIKE :v";
    private static final String POR_DIRECCION =
            "FROM Proveedor p WHERE LOWER(p.direccion) LIKE :v";
    private static final String POR_PIEZA =
            "SELECT DISTINCT g.proveedor FROM Gestion g WHERE g.pieza.codigo = :cod";

    public ProveedorDao() {
        super(Proveedor.class);
    }

    @Override
    protected Integer contarSuministros(Object codigo) {
        return em.createQuery(CONTAR_SUMINISTROS, Integer.class)
                .setParameter("cod", codigo).getSingleResult();
    }

    @Override
    public List<Proveedor> buscarPorNombre(String nombre) {
        return consulta(POR_NOMBRE).setParameter("v", like(nombre)).getResultList();
    }

    @Override
    public List<Proveedor> buscarPorDireccion(String direccion) {
        return consulta(POR_DIRECCION).setParameter("v", like(direccion)).getResultList();
    }

    @Override
    public List<Proveedor> buscarPorApellidos(String apellidos) {
        return consulta(POR_APELLIDOS).setParameter("v", like(apellidos)).getResultList();
    }

    @Override
    public List<Proveedor> buscarPorPieza(String codigoPieza) {
        return consulta(POR_PIEZA).setParameter("cod", codigoPieza).getResultList();
    }
}