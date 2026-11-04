import dao.PiezaDao;
import dao.ProveedorDao;
import dao.ProyectoDao;
import models.Gestion;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class App {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static List<Proveedor> proveedores = new ArrayList<>();
    static List<Pieza> piezas = new ArrayList<>();
    static List<Proyecto> proyectos = new ArrayList<>();
    static List<Gestion> gestiones = new ArrayList<>();

    static PiezaDao piezaDao = new PiezaDao();
    static ProveedorDao proveedorDao = new ProveedorDao();
    static ProyectoDao proyectoDao = new ProyectoDao();


    public static void main(String[] args) throws IOException {
        int opcion = -1;
        do {
            System.out.print("=========\n" +
                    "\tMENÚ\n" +
                    "=========\n" +
                    "\t1) Proveedores\n" +
                    "\t2) Piezas\n" +
                    "\t3) Proyectos\n" +
                    "\t4) Salir\n" +
                    "--> Opción: "
            );
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido");
                return;
            } catch (IOException e) {
                System.out.println("Error");
            }

            switch (opcion) {
                case 1:
                    menuProveedores();
                    break;
                case 2:
                    menuPiezas();
                    break;
                case 3:
                    menuProyectos();
                    break;
                default:
                    System.out.println("Selecciona una opción válida");
            }
        } while (opcion != 4);
    }

    // PIEZAS
    public static void menuPiezas() throws IOException {
        int opcion = -1;
        do {
            System.out.println("------");
            System.out.print("=== Menú piezas ===" +
                    "\t1) Crear pieza\n" +
                    "\t2) Editar pieza\n" +
                    "\t3) Borrar pieza\n" +
                    "\t4) Listado de piezas\n" +
                    "\t5) Búsqueda por nombre de pieza\n" +
                    "\t6) Búsqueda por id de pieza\n" +
                    "\t7) Volver\n" +
                    "--> Opción: "
            );
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido");
            } catch (IOException e) {
                System.out.println("Error");
            }

            switch (opcion) {
                case 1:
                    crearPieza();
                    break;
                case 2:
                    editarPieza();
                    break;
                case 3:
                    borrarPieza();
                    break;
                case 4:
                    mostrarLista(piezas);
                    break;
                case 5:
                    System.out.println("Introduce el nombre de la pieza a buscar:");
                    String nomPiezaBuscar = br.readLine();
                    break;
                case 6:
                    System.out.println("Introduce el id de la pieza a buscar:");
                    int idPiezaBuscar = Integer.parseInt(br.readLine());
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Error: Introduce un numero entero del 1 al 7");
            }
        } while (opcion != 7);
    }

    public static void crearPieza() throws IOException {
        System.out.println("------");
        System.out.println("--- Crear pieza ---");

        System.out.print("Código: ");
        String codigo = br.readLine();
        System.out.print("Nombre: ");
        String nombre = br.readLine();

        Double precio = 0.0;
        try {
            System.out.print("Precio: ");
            precio = Double.valueOf(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
        } catch (IOException e) {
            System.out.println("Error");
        }
        System.out.print("Descripción: ");
        String descripcion = br.readLine();

        //Dao
        Pieza pieza = new Pieza(codigo, nombre, precio, descripcion);
        piezas.add(pieza);
        piezaDao.create(new Pieza(codigo, nombre, precio, descripcion));
    }

    public static void editarPieza() throws IOException {
        System.out.println("------");
        System.out.println("--- Editar pieza ---");

        // Se muestra lista de piezas y eligen una
        mostrarLista(piezas);

        System.out.print("--> Editar: ");
        int opcion = -1;
        try {
            opcion = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcion < 0 || opcion >= piezas.size()) {
            System.out.println("Índice no válido");
            return;
        }

        /* Nuevos valores
          System.out.print("Código: ");
          String codigo = br.readLine();*/
        System.out.print("Nombre: ");
        String nombre = br.readLine();

        Double precio = 0.0;
        try {
            System.out.print("Precio: ");
            precio = Double.valueOf(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
        } catch (IOException e) {
            System.out.println("Error");
        }
        System.out.print("Descripción: ");
        String descripcion = br.readLine();

        if (precio < 0) {
            System.out.println("Precio no válido");
            return;
        }
        // Sobreescribir
        Pieza pieza = piezas.get(opcion);

        pieza.setNombre(nombre);
        pieza.setPrecio(precio);
        pieza.setDescripcion(descripcion);

        //Dao
        Pieza piezaEditada = piezaDao.buscarPorCodigo(pieza.getCodigo());
        if (piezaEditada == null) {
            System.out.println("No existe esa pieza.");
        } else {
            piezaEditada.setNombre(nombre);
            piezaDao.update(piezaEditada);
            piezaEditada.setPrecio(precio);
            piezaDao.update(piezaEditada);
            piezaEditada.setDescripcion(descripcion);
            piezaDao.update(piezaEditada);
        }
    }

    public static void borrarPieza() {
        System.out.println("------");
        System.out.println("--- Eliminar pieza ---");

        // Se muestra lista de jugadores y eligen uno
        mostrarLista(piezas);
        System.out.print("--> Opción: ");

        int opcion = -1;
        try {
            opcion = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcion < 0 || opcion >= piezas.size()) {
            System.out.println("Índice no válido");
            return;
        }

        Pieza aEliminar = piezas.get(opcion);
        // Una pieza está en varias gestiones
        for (Gestion gestion : aEliminar.getGestiones()) {
            // Para cada una de esas gestiones
            // Quitamos de la lista de gestiones de su proveedor
            gestion.getProveedor().getGestiones().remove(gestion);
            // Quitamos de la lista de gestiones de su proyecto
            gestion.getProyecto().getGestiones().remove(gestion);
            // Quitamos de la lista de gestiones de su pieza
            aEliminar.getGestiones().remove(gestion);
            gestiones.remove(gestion);
        }
        piezas.remove(aEliminar);

        //Dao
        Pieza piezaEliminada = piezaDao.buscarPorCodigo(aEliminar.getCodigo());
        if (piezaEliminada != null) piezaDao.delete(piezaEliminada);   // lanza excepción si tiene suministros
    }

    public static void buscarPiezaPorNombre() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar pieza por nombre ---");

        System.out.print("Nombre: ");
        String nombre = br.readLine();

        for (Pieza pieza : piezas) {
            if (pieza.getNombre().equalsIgnoreCase(nombre)) {
            }
        }
    }

    public static void menuProveedores() throws IOException {
        int opcion = -1;
        do {
            System.out.println("------");
            System.out.print("=========\n" +
                    "\tGestion de Proveedores\n" +
                    "=========\n" +
                    "\t1) Crear Proveedor\n" +
                    "\t2) Editar Proveedor\n" +
                    "\t3) Borrar Proveedor\n" +
                    "\t4) Listado de Proveedores\n" +
                    "\t5) Busqueda por Nombre de Proveedor\n" +
                    "\t6) Busqueda por id de Proveedor\n" +
                    "\t7) Busqueda por Direccion de Proveedor\n" +
                    "\t8) Volver\n" +
                    "--> Opción: "
            );
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido");
            } catch (IOException e) {
                System.out.println("Error");
            }

            switch (opcion) {
                case 1:
                    crearProveedor();
                    break;
                case 2:
                    editarProveedor();
                    break;
                case 3:
                    borrarProveedor();
                    break;
                case 4:
                    // mostrarProveedor(proveedores);
                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:
                    break;
                default:
                    System.out.println("Error: Introduce un numero entero del 1 al 7");
            }
        } while (opcion != 7);
    }

    public static void crearProveedor() throws IOException {
        System.out.println("------");
        System.out.println("--- Crear Proveedor ---");

        System.out.print("Código: ");
        String codigo = br.readLine();
        System.out.print("Nombre: ");
        String nombre = br.readLine();
        System.out.print("Apellidos: ");
        String apellidos = br.readLine();
        System.out.print("Direccion: ");
        String direccion = br.readLine();

        //Dao
        Proveedor proveedor = new Proveedor(codigo, nombre, apellidos, direccion);
        proveedores.add(proveedor);

        proveedorDao.create(new Proveedor(codigo, nombre, apellidos, direccion));
    }

    public static void editarProveedor() throws IOException {
        System.out.println("------");
        System.out.println("--- Editar Proveedor ---");

        // Se muestra lista de proveedores y eligen una
        mostrarLista(proveedores);

        System.out.print("--> Editar: ");
        int opcion = -1;
        try {
            opcion = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcion < 0 || opcion >= proveedores.size()) {
            System.out.println("Índice no válido");
            return;
        }

        // Nuevos valores
//        System.out.print("Código: ");
//        String codigo = br.readLine();
        System.out.print("Nombre: ");
        String nombre = br.readLine();
        System.out.print("Apellidos: ");
        String apellidos = br.readLine();
        System.out.print("Direccion: ");
        String direccion = br.readLine();

        // Sobreescribir
        Proveedor proveedor = proveedores.get(opcion);

        proveedor.setNombre(nombre);
        proveedor.setApellidos(apellidos);
        proveedor.setDireccion(direccion);

        //Dao
        Proveedor proveedorEditado = proveedorDao.buscarPorCodigo(proveedor.getCodigo());
        if (proveedorEditado == null) {
            System.out.println("No existe ese proveedor.");
        } else {
            proveedorEditado.setNombre(nombre);
            proveedorDao.update(proveedorEditado);
            proveedorEditado.setApellidos(apellidos);
            proveedorDao.update(proveedorEditado);
            proveedorEditado.setDireccion(direccion);
            proveedorDao.update(proveedorEditado);

        }
    }

    public static void borrarProveedor() {
        System.out.println("------");
        System.out.println("--- Eliminar Proveedor ---");

        // Se muestra lista de proveedores y eligen uno
        mostrarLista(proveedores);
        System.out.print("--> Opción: ");

        int opcion = -1;
        try {
            opcion = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcion < 0 || opcion >= proveedores.size()) {
            System.out.println("Índice no válido");
            return;
        }

        Proveedor aEliminar = proveedores.get(opcion);
        // Un proveedor está en varias gestiones
        for (Gestion gestion : aEliminar.getGestiones()) {
            // Para cada una de esas gestiones
            // Quitamos de la lista de gestiones de su pieza
            gestion.getPieza().getGestiones().remove(gestion);
            // Quitamos de la lista de gestiones de su proyecto
            gestion.getProyecto().getGestiones().remove(gestion);
            // Quitamos de la lista de gestiones de su proveedor
            aEliminar.getGestiones().remove(gestion);
            gestiones.remove(gestion);
        }
        proveedores.remove(aEliminar);

        //Dao
        Proveedor proveedorEliminado = proveedorDao.buscarPorCodigo(aEliminar.getCodigo());
        if (proveedorEliminado != null) proveedorDao.delete(proveedorEliminado);   // lanza excepción si tiene suministros
    }

    public static void menuProyectos() throws IOException {
        int opcion = -1;
        do {
            System.out.println("------");
            System.out.print("=========\n" +
                    "\tGestion de Proyectos\n" +
                    "=========\n" +
                    "\t1) Crear Proyecto\n" +
                    "\t2) Editar Proyecto\n" +
                    "\t3) Borrar Proyecto\n" +
                    "\t4) Listado de Proyectos\n" +
                    "\t5) Busqueda por Nombre de Proyectos\n" +
                    "\t6) Busqueda por id de Proyectos\n" +
                    "\t7) Busqueda por Ciudad de Proyectos\n" +
                    "\t8) Volver\n" +
                    "--> Opción: "
            );
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido");
            } catch (IOException e) {
                System.out.println("Error");
            }

            switch (opcion) {
                case 1:
                    crearProyecto();
                    break;
                case 2:
                    editarProyecto();
                    break;
                case 3:
                    borrarProyecto();
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:
                    break;
                default:
                    System.out.println("Error: Introduce un numero entero del 1 al 7");
            }
        } while (opcion != 7);
    }

    public static void crearProyecto() throws IOException {
        System.out.println("------");
        System.out.println("--- Crear Proyecto ---");

        System.out.print("Código: ");
        String codigo = br.readLine();
        System.out.print("Nombre: ");
        String nombre = br.readLine();
        System.out.print("Ciudad: ");
        String ciudad = br.readLine();


        Proyecto proyecto = new Proyecto(codigo, nombre, ciudad);
        proyectos.add(proyecto);

        //Dao
        proyectoDao.create(new Proyecto(codigo, nombre, ciudad));
    }

    public static void editarProyecto() throws IOException {
        System.out.println("------");
        System.out.println("--- Editar Proyecto ---");

        // Se muestra lista de proyectos y eligen una
        mostrarLista(proyectos);

        System.out.print("--> Editar: ");
        int opcion = -1;
        try {
            opcion = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcion < 0 || opcion >= proyectos.size()) {
            System.out.println("Índice no válido");
            return;
        }

        // Nuevos valores
//        System.out.print("Código: ");
//        String codigo = br.readLine();
        System.out.print("Nombre: ");
        String nombre = br.readLine();
        System.out.print("Ciudad: ");
        String ciudad = br.readLine();

        // Sobreescribir
        Proyecto proyecto = proyectos.get(opcion);

        proyecto.setNombre(nombre);
        proyecto.setCiudad(ciudad);

        //Dao
        Proyecto proyectoEditado = proyectoDao.buscarPorCodigo(proyecto.getCodigo());
        if (proyectoEditado == null) {
            System.out.println("No existe ese proyecto.");
        } else {
            proyectoEditado.setNombre(nombre);
            proyectoDao.update(proyectoEditado);
             proyectoEditado.setCiudad(ciudad);
            proyectoDao.update(proyectoEditado);

        }
    }

    public static void borrarProyecto() {
        System.out.println("------");
        System.out.println("--- Eliminar Proyecto ---");

        // Se muestra lista de proveedores y eligen uno
        mostrarLista(proyectos);
        System.out.print("--> Opción: ");

        int opcion = -1;
        try {
            opcion = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcion < 0 || opcion >= proyectos.size()) {
            System.out.println("Índice no válido");
            return;
        }

        Proyecto aEliminar = proyectos.get(opcion);
        // Un proveedor está en varias gestiones
        for (Gestion gestion : aEliminar.getGestiones()) {
            // Para cada una de esas gestiones
            // Quitamos de la lista de gestiones de su pieza
            gestion.getPieza().getGestiones().remove(gestion);
            // Quitamos de la lista de gestiones de su proveedor
            gestion.getProveedor().getGestiones().remove(gestion);
            // Quitamos de la lista de gestiones de su proyecto
            aEliminar.getGestiones().remove(gestion);
            gestiones.remove(gestion);
        }
        proyectos.remove(aEliminar);

        //Dao
        Proyecto proyectoEliminado = proyectoDao.buscarPorCodigo(aEliminar.getCodigo());
        if (proyectoEliminado != null) proyectoDao.delete(proyectoEliminado);   // lanza excepción si tiene suministros
    }


    // MÉTODOS COMUNES
    // Recibe un List de objetos y los muestra
    public static void mostrarLista(List<?> lista) {
        System.out.println("------");
        System.out.println("--- Mostrar lista ---");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(i + " - " + lista.get(i));
        }
    }
}
