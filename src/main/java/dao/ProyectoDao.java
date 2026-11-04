package dao;

import idao.IProyectoDao;
import models.Proyecto;

import java.util.List;

public class ProyectoDao extends Dao<Proyecto, Integer> implements IProyectoDao {

    // ---- Consultas JPQL ----
    private static final String CONTAR_SUMINISTROS =
            "SELECT COUNT(g) FROM Gestion g WHERE g.proyecto.id = :id";
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
    protected void validar(Proyecto p) {
        comprobarCodigoUnico(p.getCodigo(), p.getId());
    }

    @Override
    protected long contarSuministros(Object id) {
        return em.createQuery(CONTAR_SUMINISTROS, Long.class)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public Proyecto buscarPorCodigo(String codigo) {
        return porCodigo(codigo);
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