package dao;

import idao.IGestionDao;
import models.Gestion;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;

import java.time.LocalDate;
import java.util.List;

public class GestionDao extends Dao<Gestion, Integer> implements IGestionDao {

    // JOIN FETCH carga proveedor, pieza y proyecto en la misma consulta
    private static final String LISTAR_CON_DETALLE =
            "SELECT g FROM Gestion g "
                    + "LEFT JOIN FETCH g.proveedor LEFT JOIN FETCH g.pieza LEFT JOIN FETCH g.proyecto "
                    + "ORDER BY g.fecha DESC";
    private static final String POR_FECHA =
            "FROM Gestion g WHERE g.fecha BETWEEN :desde AND :hasta ORDER BY g.fecha DESC";
    private static final String POR_CANTIDAD =
            "FROM Gestion g WHERE g.cantidad BETWEEN :min AND :max ORDER BY g.fecha DESC";
    private static final String POR_PROVEEDOR =
            "FROM Gestion g WHERE g.proveedor.codigo = :codigo ORDER BY g.fecha DESC";
    private static final String POR_PIEZA =
            "FROM Gestion g WHERE g.pieza.codigo = :codigo ORDER BY g.fecha DESC";
    private static final String POR_PROYECTO =
            "FROM Gestion g WHERE g.proyecto.codigo = :codigo ORDER BY g.fecha DESC";

    public GestionDao() {
        super(Gestion.class);
    }

    @Override
    public List<Gestion> findAll() {
        return consulta(LISTAR_CON_DETALLE).getResultList();
    }

    @Override
    protected void validar(Gestion g) {
        comprobarCodigoUnico(g.getCodigo(), g.getId());
        if (g.getCantidad() == null || g.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
        }
        if (g.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (g.getProveedor() == null || !existe(Proveedor.class, g.getProveedor().getId())) {
            throw new IllegalArgumentException("El proveedor indicado no existe.");
        }
        if (g.getPieza() == null || !existe(Pieza.class, g.getPieza().getId())) {
            throw new IllegalArgumentException("La pieza indicada no existe.");
        }
        if (g.getProyecto() == null || !existe(Proyecto.class, g.getProyecto().getId())) {
            throw new IllegalArgumentException("El proyecto indicado no existe.");
        }
    }

    @Override
    public Gestion buscarPorCodigo(String codigo) {
        return porCodigo(codigo);
    }

    @Override
    public List<Gestion> buscarPorIntervaloFecha(LocalDate desde, LocalDate hasta) {
        return consulta(POR_FECHA)
                .setParameter("desde", desde).setParameter("hasta", hasta).getResultList();
    }

    @Override
    public List<Gestion> buscarPorIntervaloCantidad(Integer min, Integer max) {
        return consulta(POR_CANTIDAD)
                .setParameter("min", min).setParameter("max", max).getResultList();
    }

    @Override
    public List<Gestion> buscarPorProveedor(String codigoProveedor) {
        return consulta(POR_PROVEEDOR).setParameter("codigo", codigoProveedor).getResultList();
    }

    @Override
    public List<Gestion> buscarPorPieza(String codigoPieza) {
        return consulta(POR_PIEZA).setParameter("codigo", codigoPieza).getResultList();
    }

    @Override
    public List<Gestion> buscarPorProyecto(String codigoProyecto) {
        return consulta(POR_PROYECTO).setParameter("codigo", codigoProyecto).getResultList();
    }

    private boolean existe(Class<?> clase, Integer id) {
        return id != null && em.find(clase, id) != null;
    }
}