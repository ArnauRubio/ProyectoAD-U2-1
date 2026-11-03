package idao;

import models.Gestion;

/** Solo CRUD: create y update validan que proveedor, pieza y proyecto existan. */
public interface IGestionDao extends IDao<Gestion, Integer> {
}