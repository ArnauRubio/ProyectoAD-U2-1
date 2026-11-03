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
    public Gestion create(Gestion g) {
        validar(g);
        return super.create(g);
    }

    @Override
    public Gestion update(Gestion g) {
        validar(g);
        return super.update(g);
    }

    /** @throws IllegalArgumentException si los datos no son válidos */
    private void validar(Gestion g) {
        if (g.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
        }
        if (g.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (g.getProveedor() == null || em.find(Proveedor.class, g.getProveedor().getCodigo()) == null) {
            throw new IllegalArgumentException("El proveedor indicado no existe.");
        }
        if (g.getPieza() == null || em.find(Pieza.class, g.getPieza().getCodigo()) == null) {
            throw new IllegalArgumentException("La pieza indicada no existe.");
        }
        if (g.getProyecto() == null || em.find(Proyecto.class, g.getProyecto().getCodigo()) == null) {
            throw new IllegalArgumentException("El proyecto indicado no existe.");
        }
    }
}