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
        return enTransaccion(() -> {
            Object id = idDe(t);
            if (id != null && em.find(clase, id) != null) {
                throw new IllegalStateException("Ya existe un registro con el código '" + id + "'.");
            }
            em.persist(t);
            return t;
        });
    }

    @Override
    public T update(T t) {
        return enTransaccion(() -> {
            Object id = idDe(t);
            if (id == null || em.find(clase, id) == null) {
                throw new IllegalStateException("No existe ningún registro con el código '" + id + "'.");
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
                throw new IllegalStateException("No existe ningún registro con el código '" + id + "'.");
            }
            Integer n = contarSuministros(id);
            if (n > 0) {
                throw new IllegalStateException(
                        "No se puede eliminar: tiene " + n + " suministro(s) asociado(s).");
            }
            em.remove(existente);
            return null;
        });
    }

    // ---------- Utilidades para las subclases ----------

    /** Nº de suministros asociados. Por defecto 0; lo sobrescriben Proveedor, Pieza y Proyecto. */
    protected Integer contarSuministros(Object id) {
        return 0;
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