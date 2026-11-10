package dao;

import idao.IDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.function.Supplier;

public abstract class Dao<T, K> implements IDao<T, K> {

    // Una única factory para toda la aplicación (se crea al primer uso)
    private static EntityManagerFactory emf;

    private static synchronized EntityManagerFactory emf() {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("default");
            } catch (RuntimeException e) {
                throw new IllegalStateException(
                        "No se pudo conectar con la base de datos: " + e.getMessage(), e);
            }
        }
        return emf;
    }

    /** Llamar al salir de la aplicación. */
    public static synchronized void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    protected final EntityManager em;
    private final Class<T> clase;

    protected Dao(Class<T> clase) {
        this.clase = clase;
        this.em = emf().createEntityManager();
    }

    // ---------- Lecturas ----------

    @Override
    public T find(K id) {
        em.clear(); // evita datos antiguos en caché
        return em.find(clase, id);
    }

    @Override
    public List<T> findAll() {
        return consulta("FROM " + clase.getSimpleName()).getResultList();
    }

    // ---------- Escrituras ----------

    @Override
    public T create(T t) {
        validar(t);
        return enTransaccion(() -> {
            em.persist(t);
            return t;
        });
    }

    @Override
    public T update(T t) {
        validar(t);
        return enTransaccion(() -> {
            Object id = idDe(t);
            if (id == null || em.find(clase, id) == null) {
                throw new IllegalStateException("El registro que se quiere modificar no existe.");
            }
            return em.merge(t);
        });
    }

    @Override
    public void delete(T t) {
        enTransaccion(() -> {
            Object id = idDe(t);
            T existente = (id == null) ? null : em.find(clase, id);
            if (existente == null) {
                throw new IllegalStateException("El registro que se quiere eliminar no existe.");
            }

            em.remove(existente);
            return null;
        });
    }

    // ---------- Para las subclases ----------

    /** Validaciones previas a create y update. Por defecto no hace nada. */
    protected void validar(T t) {
    }

    /** Nº de suministros asociados. Por defecto 0; lo sobrescriben Proveedor, Pieza y Proyecto. */
    protected long contarSuministros(Object id) {
        return 0;
    }

    /** Busca por el campo "codigo". @return la entidad o null */
    protected T porCodigo(String codigo) {
        List<T> r = consulta("FROM " + clase.getSimpleName() + " e WHERE e.codigo = :c")
                .setParameter("c", codigo).getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    /**
     * Comprueba que el código no esté vacío y que no lo use otro registro.
     * @param idPropio id de la entidad que se guarda (null si es nueva)
     */
    protected void comprobarCodigoUnico(String codigo, Integer idPropio) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        T otro = porCodigo(codigo);
        if (otro != null && !idDe(otro).equals(idPropio)) {
            throw new IllegalStateException("Ya existe un registro con el código '" + codigo + "'.");
        }
    }

    /** Crea una consulta JPQL que devuelve entidades de este DAO. */
    protected TypedQuery<T> consulta(String jpql) {
        em.clear();
        return em.createQuery(jpql, clase);
    }

    /** Texto para LIKE, sin distinguir mayúsculas (usar con LOWER(campo) LIKE :v). */
    protected static String like(String texto) {
        return "%" + texto.trim().toLowerCase() + "%";
    }

    // ---------- Internos ----------

    private Object idDe(T t) {
        return emf().getPersistenceUnitUtil().getIdentifier(t);
    }

    /** Transacción con commit, o rollback si algo falla. */
    private <R> R enTransaccion(Supplier<R> accion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            R resultado = accion.get();
            tx.commit();
            return resultado;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            em.clear();
            throw e;
        }
    }
}