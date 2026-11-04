package dao;

import idao.IGestionDao;
import models.Gestion;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;

import java.util.List;

public class GestionDao extends Dao<Gestion, Integer> implements IGestionDao {

    // JOIN FETCH carga proveedor, pieza y proyecto en la misma consulta
    private static final String LISTAR_CON_DETALLE =
            "SELECT g FROM Gestion g "
                    + "JOIN FETCH g.proveedor JOIN FETCH g.pieza JOIN FETCH g.proyecto "
                    + "ORDER BY g.fecha DESC";

    public GestionDao() {
        super(Gestion.class);
    }

    @Override
    public List<Gestion> findAll() {
        return consulta(LISTAR_CON_DETALLE).getResultList();
    }

    @Override
    protected void validar(Gestion g) {
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

    private boolean existe(Class<?> clase, Integer id) {
        return id != null && em.find(clase, id) != null;
    }
}