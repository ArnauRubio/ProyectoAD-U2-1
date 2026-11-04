package idao;

import java.util.List;

/** Operaciones comunes a todos los DAOs. K es el tipo del id (Integer). */
public interface IDao<T, K> {

    /** Busca por id. @return la entidad o null si no existe */
    T find(K id);

    List<T> findAll();

    /**
     * @throws IllegalStateException si el código ya existe
     * @throws IllegalArgumentException si los datos no son válidos
     */
    T create(T t);

    /**
     * La entidad debe tener su id (obtenerla antes con buscarPorCodigo / find).
     * @throws IllegalStateException si no existe o el código ya lo usa otro registro
     */
    T update(T t);

    /** @throws IllegalStateException si no existe o tiene suministros asociados */
    void delete(T t);
}