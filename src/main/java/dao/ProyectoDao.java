package dao;

import idao.IProyectoDao;
import models.Proyecto;

import java.util.List;

public class ProyectoDao extends Dao<Proyecto, String> implements IProyectoDao {

    // ---- Consultas JPQL ----
    private static final String CONTAR_SUMINISTROS =
            "SELECT COUNT(g) FROM Gestion g WHERE g.proyecto.codigo = :cod";
    private static final String POR_NOMBRE =
            "FROM Proyecto p WHERE LOWER(p.nombre) LIKE :v";
    private static final String POR_CIUDAD =
            "FROM Proyecto p WHERE LOWER(p.ciudad) LIKE :v";
    private static final String POR_PROVEEDOR =
            "SELECT DISTINCT g.proyecto FROM Gestion g WHERE g.proveedor.codigo = :cod";
    private static final String POR_PIEZA =
            "SELECT DISTINCT g.proyecto FROM Gestion g WHERE g.pieza.codigo = :cod";

    public ProyectoDao() {
        super(Proyecto.class);
    }

    @Override
    protected Integer contarSuministros(Object codigo) {
        return em.createQuery(CONTAR_SUMINISTROS, Integer.class)
                .setParameter("cod", codigo).getSingleResult();
    }

    @Override
    public List<Proyecto> buscarPorNombre(String nombre) {
        return consulta(POR_NOMBRE).setParameter("v", like(nombre)).getResultList();
    }

    @Override
    public List<Proyecto> buscarPorCiudad(String ciudad) {
        return consulta(POR_CIUDAD).setParameter("v", like(ciudad)).getResultList();
    }

    @Override
    public List<Proyecto> buscarPorProveedor(String codigoProveedor) {
        return consulta(POR_PROVEEDOR).setParameter("cod", codigoProveedor).getResultList();
    }

    @Override
    public List<Proyecto> buscarPorPieza(String codigoPieza) {
        return consulta(POR_PIEZA).setParameter("cod", codigoPieza).getResultList();
    }
}