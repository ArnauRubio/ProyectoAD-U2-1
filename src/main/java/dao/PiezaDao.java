package dao;

import idao.IPiezaDao;
import models.Pieza;

import java.util.List;

public class PiezaDao extends Dao<Pieza, Integer> implements IPiezaDao {

    // ---- Consultas JPQL ----
    private static final String CONTAR_SUMINISTROS =
            "SELECT COUNT(g) FROM Gestion g WHERE g.pieza.id = :id";
    private static final String POR_NOMBRE =
            "FROM Pieza p WHERE LOWER(p.nombre) LIKE :v";
    private static final String POR_PRECIO =
            "FROM Pieza p WHERE p.precio BETWEEN :min AND :max ORDER BY p.precio";
    private static final String POR_PROVEEDOR =
            "SELECT DISTINCT g.pieza FROM Gestion g WHERE g.proveedor.codigo = :cod";
    private static final String POR_PROYECTO =
            "SELECT DISTINCT g.pieza FROM Gestion g WHERE g.proyecto.codigo = :cod";

    public PiezaDao() {
        super(Pieza.class);
    }

    @Override
    protected void validar(Pieza p) {
        comprobarCodigoUnico(p.getCodigo(), p.getId());
        if (p.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
    }

    @Override
    protected long contarSuministros(Object id) {
        return em.createQuery(CONTAR_SUMINISTROS, Long.class)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public Pieza buscarPorCodigo(String codigo) {
        return porCodigo(codigo);
    }

    @Override
    public List<Pieza> buscarPorNombre(String nombre) {
        return consulta(POR_NOMBRE).setParameter("v", like(nombre)).getResultList();
    }

    @Override
    public List<Pieza> buscarPorIntervaloPrecio(double min, double max) {
        return consulta(POR_PRECIO)
                .setParameter("min", min).setParameter("max", max).getResultList();
    }

    @Override
    public List<Pieza> buscarPorProveedor(String codigoProveedor) {
        return consulta(POR_PROVEEDOR).setParameter("cod", codigoProveedor).getResultList();
    }

    @Override
    public List<Pieza> buscarPorProyecto(String codigoProyecto) {
        return consulta(POR_PROYECTO).setParameter("cod", codigoProyecto).getResultList();
    }
}