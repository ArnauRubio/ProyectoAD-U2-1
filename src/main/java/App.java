import dao.GestionDao;
import dao.PiezaDao;
import dao.ProveedorDao;
import dao.ProyectoDao;
import idao.IGestionDao;
import models.Gestion;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static List<Proveedor> proveedores = new ArrayList<>();
    static List<Pieza> piezas = new ArrayList<>();
    static List<Proyecto> proyectos = new ArrayList<>();
    static List<Gestion> gestiones = new ArrayList<>();

    static PiezaDao piezaDao = new PiezaDao();
    static ProveedorDao proveedorDao = new ProveedorDao();
    static ProyectoDao proyectoDao = new ProyectoDao();
    static GestionDao gestionDao = new GestionDao();


    public static void main(String[] args) throws IOException {
        cargarDatosIniciales();

        int opcion = -1;
        do {
            System.out.print("=========\n" +
                    "\tMENÚ\n" +
                    "=========\n" +
                    "\t1) Proveedores\n" +
                    "\t2) Piezas\n" +
                    "\t3) Proyectos\n" +
                    "\t4) Gestiones\n" +
                    "\t5) Salir\n" +
                    "--> Opción: "
            );
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido");
                continue;
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
                case 4:
                    menuGestiones();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Selecciona una opción válida");
            }
        } while (opcion != 5);
    }

    private static void cargarDatosIniciales() {
        proveedores.clear();
        proveedores.addAll(proveedorDao.findAll());

        piezas.clear();
        piezas.addAll(piezaDao.findAll());

        proyectos.clear();
        proyectos.addAll(proyectoDao.findAll());

        gestiones.clear();
        gestiones.addAll(gestionDao.findAll());
        enlazarGestionesCargadas();
    }

    private static void enlazarGestionesCargadas() {
        Map<Integer, Proveedor> proveedoresPorId = new HashMap<>();
        for (Proveedor proveedor : proveedores) {
            proveedor.setGestiones(new ArrayList<>());
            proveedoresPorId.put(proveedor.getId(), proveedor);
        }

        Map<Integer, Pieza> piezasPorId = new HashMap<>();
        for (Pieza pieza : piezas) {
            pieza.setGestiones(new ArrayList<>());
            piezasPorId.put(pieza.getId(), pieza);
        }

        Map<Integer, Proyecto> proyectosPorId = new HashMap<>();
        for (Proyecto proyecto : proyectos) {
            proyecto.setGestiones(new ArrayList<>());
            proyectosPorId.put(proyecto.getId(), proyecto);
        }

        for (Gestion gestion : gestiones) {
            if (gestion.getProveedor() != null) {
                Proveedor proveedor = proveedoresPorId.get(gestion.getProveedor().getId());
                if (proveedor != null) {
                    gestion.setProveedor(proveedor);
                    proveedor.getGestiones().add(gestion);
                }
            }
            if (gestion.getPieza() != null) {
                Pieza pieza = piezasPorId.get(gestion.getPieza().getId());
                if (pieza != null) {
                    gestion.setPieza(pieza);
                    pieza.getGestiones().add(gestion);
                }
            }
            if (gestion.getProyecto() != null) {
                Proyecto proyecto = proyectosPorId.get(gestion.getProyecto().getId());
                if (proyecto != null) {
                    gestion.setProyecto(proyecto);
                    proyecto.getGestiones().add(gestion);
                }
            }
        }
    }

    // PIEZAS
    public static void menuPiezas() throws IOException {
        int opcion = -1;
        do {
            System.out.println("------");
            System.out.print("=== Menú piezas ===\n" +
                    "\t1) Crear pieza\n" +
                    "\t2) Editar pieza\n" +
                    "\t3) Borrar pieza\n" +
                    "\t4) Listado de piezas\n" +
                    "\t5) Búsqueda por nombre de pieza\n" +
                    "\t6) Búsqueda por código de pieza\n" +
                    "\t7) Volver\n" +
                    "--> Opción: "
            );
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido");
                continue;
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
                    buscarPiezaPorNombre();
                    break;
                case 6:
                    buscarPiezaPorCodigo();
                    break;
                case 7:
                    System.out.println("*Volviendo al menú principal");
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
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }
        System.out.print("Descripción: ");
        String descripcion = br.readLine();

        //Dao
        Pieza pieza = new Pieza(codigo, nombre, precio, descripcion);
        piezas.add(pieza);
        piezaDao.create(pieza);
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

        // Nuevos valores
        System.out.print("Nombre: ");
        String nombre = br.readLine();

        Double precio = 0.0;
        try {
            System.out.print("Precio: ");
            precio = Double.valueOf(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
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
        piezaDao.update(pieza);
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
//        for (Gestion gestion : aEliminar.getGestiones()) {
//            if(gestion.getProveedor() != null){
//                gestion.getProveedor().getGestiones().remove(gestion);
//            }
//            if(gestion.getProyecto() != null){
//                gestion.getProyecto().getGestiones().remove(gestion);
//            }
//            aEliminar.getGestiones().remove(gestion);
//            gestiones.remove(gestion);
//        }
if (!piezaDao.tieneSuministrosAsociados(aEliminar)) {
    piezaDao.delete(aEliminar);
    piezas.remove(aEliminar);
    System.out.println("Pieza eliminada");
        } else {
            System.out.println("No se ha podido eliminar porque hay gestiones asociadas");
        }
    }

    public static void buscarPiezaPorNombre() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar pieza por nombre ---");

        System.out.print("Nombre: ");
        String nombre = br.readLine();

        boolean encontrada = false;
        for (Pieza pieza : piezas) {
            if (pieza.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("- Pieza encontrada -");
                System.out.println("--> Código: " + pieza.getCodigo());
                System.out.println("--> Nombre: " + pieza.getNombre());
                System.out.println("--> Precio: " + pieza.getPrecio());
                System.out.println("--> Descripción: " + pieza.getDescripcion());

                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar la pieza");
        }
    }

    public static void buscarPiezaPorCodigo() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar pieza por código ---");

        System.out.print("Código: ");
        String cod = br.readLine();

        boolean encontrada = false;
        for (Pieza pieza : piezas) {
            if (pieza.getCodigo().equalsIgnoreCase(cod)) {
                System.out.println("- Pieza encontrada -");
                System.out.println("--> Código: " + pieza.getCodigo());
                System.out.println("--> Nombre: " + pieza.getNombre());
                System.out.println("--> Precio: " + pieza.getPrecio());
                System.out.println("--> Descripción: " + pieza.getDescripcion());

                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar la pieza");
        }
    }

    public static void menuProveedores() throws IOException {
        int opcion = -1;
        do {
            System.out.println("------");
            System.out.print("=== Menú proveedores ===\n" +
                    "\t1) Crear proveedor\n" +
                    "\t2) Editar proveedor\n" +
                    "\t3) Borrar proveedor\n" +
                    "\t4) Listado de proveedores\n" +
                    "\t5) Búsqueda por nombre de proveedor\n" +
                    "\t6) Búsqueda por código de proveedor\n" +
                    "\t7) Búsqueda por dirección de proveedor\n" +
                    "\t8) Volver\n" +
                    "--> Opción: "
            );
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido");
                continue;
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
                    mostrarLista(proveedores);
                    break;
                case 5:
                    buscarProveedorPorNombre();
                    break;
                case 6:
                    buscarProveedorPorCodigo();
                    break;
                case 7:
                    buscarProveedorPorDireccion();
                    break;
                case 8:
                    System.out.println("*Volviendo al menú principal");
                    break;
                default:
                    System.out.println("Error: Introduce un numero entero del 1 al 7");
            }
        } while (opcion != 8);
    }

    public static void crearProveedor() throws IOException {
        System.out.println("------\n");
        System.out.println("--- Crear proveedor ---");

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
        proveedorDao.create(proveedor);
    }

    public static void editarProveedor() throws IOException {
        System.out.println("------");
        System.out.println("--- Editar proveedor ---");

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
        System.out.println("--- Eliminar proveedor ---");

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
//        for (Gestion gestion : aEliminar.getGestiones()) {
//            // Para cada una de esas gestiones
//            // Quitamos de la lista de gestiones de su pieza
//            gestion.getPieza().getGestiones().remove(gestion);
//            // Quitamos de la lista de gestiones de su proyecto
//            gestion.getProyecto().getGestiones().remove(gestion);
//            // Quitamos de la lista de gestiones de su proveedor
//            aEliminar.getGestiones().remove(gestion);
//            gestiones.remove(gestion);
//        }
if (!proveedorDao.tieneSuministrosAsociados(aEliminar)) {
    proveedorDao.delete(aEliminar);
    proveedores.remove(aEliminar);
    System.out.println("Proveedor eliminado");
        } else {
            System.out.println("No se ha podido eliminar porque hay gestiones asociadas");
        }
    }

    public static void buscarProveedorPorNombre() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar proveedor por nombre ---");

        System.out.print("Nombre: ");
        String nombre = br.readLine();

        boolean encontrada = false;
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("- Proveedor encontrado -");
                System.out.println("--> Código: " + proveedor.getCodigo());
                System.out.println("--> Nombre: " + proveedor.getNombre());
                System.out.println("--> Apellidos: " + proveedor.getApellidos());
                System.out.println("--> Dirección: " + proveedor.getDireccion());

                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar el proveedor");
        }
    }

    public static void buscarProveedorPorCodigo() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar proveedor por código ---");

        System.out.print("Código: ");
        String cod = br.readLine();

        boolean encontrada = false;
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getCodigo().equalsIgnoreCase(cod)) {
                System.out.println("- Proveedor encontrado -");
                System.out.println("--> Código: " + proveedor.getCodigo());
                System.out.println("--> Nombre: " + proveedor.getNombre());
                System.out.println("--> Apellidos: " + proveedor.getApellidos());
                System.out.println("--> Dirección: " + proveedor.getDireccion());

                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar el proveedor");
        }
    }

    public static void buscarProveedorPorDireccion() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar proveedor por dirección ---");

        System.out.print("Dirección: ");
        String dir = br.readLine();

        boolean encontrada = false;
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getDireccion().equalsIgnoreCase(dir)) {
                System.out.println("- Proveedor encontrado -");
                System.out.println("--> Código: " + proveedor.getCodigo());
                System.out.println("--> Nombre: " + proveedor.getNombre());
                System.out.println("--> Apellidos: " + proveedor.getApellidos());
                System.out.println("--> Dirección: " + proveedor.getDireccion());

                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar el proveedor");
        }
    }

    public static void menuProyectos() throws IOException {
        int opcion = -1;
        do {
            System.out.println("------");
            System.out.print("=== Menú proyectos ===\n" +
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
                continue;
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
                    mostrarLista(proyectos);
                    break;
                case 5:
                    buscarProyectoPorNombre();
                    break;
                case 6:
                    buscarProyectoPorCodigo();
                    break;
                case 7:
                    buscarProyectoPorCiudad();
                    break;
                case 8:
                    System.out.println("*Volviendo al menú principal");
                default:
                    System.out.println("Error: Introduce un numero entero del 1 al 7");
            }
        } while (opcion != 8);
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
        proyectoDao.create(proyecto);
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
        System.out.println(aEliminar);
        // Un proveedor está en varias gestiones
//        for (Gestion gestion : aEliminar.getGestiones()) {
//            // Para cada una de esas gestiones
//            // Quitamos de la lista de gestiones de su pieza
//            gestion.getPieza().getGestiones().remove(gestion);
//            // Quitamos de la lista de gestiones de su proveedor
//            gestion.getProveedor().getGestiones().remove(gestion);
//            // Quitamos de la lista de gestiones de su proyecto
//            aEliminar.getGestiones().remove(gestion);
//            gestiones.remove(gestion);
//        }
if (!proyectoDao.tieneSuministrosAsociados(aEliminar)) {
    proyectoDao.delete(aEliminar);
    proyectos.remove(aEliminar);
    System.out.println("Proyecto eliminada");
        } else {
            System.out.println("No se ha podido eliminar porque hay gestiones asociadas");
        }
    }

    public static void buscarProyectoPorNombre() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar proyecto por nombre ---");

        System.out.print("Nombre: ");
        String nombre = br.readLine();

        boolean encontrada = false;
        for (Proyecto proyecto : proyectos) {
            if (proyecto.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("- Proyecto encontrado -");
                System.out.println("--> Código: " + proyecto.getCodigo());
                System.out.println("--> Nombre: " + proyecto.getNombre());
                System.out.println("--> Ciudad: " + proyecto.getCiudad());

                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar el proyecto");
        }
    }

    public static void buscarProyectoPorCodigo() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar proyecto por código ---");

        System.out.print("Código: ");
        String cod = br.readLine();

        boolean encontrada = false;
        for (Proyecto proyecto : proyectos) {
            if (proyecto.getCodigo().equalsIgnoreCase(cod)) {
                System.out.println("- Proyecto encontrado -");
                System.out.println("--> Código: " + proyecto.getCodigo());
                System.out.println("--> Nombre: " + proyecto.getNombre());
                System.out.println("--> Ciudad: " + proyecto.getCiudad());

                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar el proyecto");
        }
    }

    public static void buscarProyectoPorCiudad() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar proyecto por ciudad ---");

        System.out.print("Ciudad: ");
        String ciudad = br.readLine();

        boolean encontrada = false;
        List<Proyecto> encontradas = new ArrayList<>();
        for (Proyecto proyecto : proyectos) {
            if (proyecto.getCiudad().equalsIgnoreCase(ciudad)) {
                encontradas.add(proyecto);
                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar el proyecto");
        } else {
            System.out.println("- Proyecto(s) encontrado(s) -");
            for (Proyecto pr : encontradas) {
                System.out.println("--> Código: " + pr.getCodigo());
                System.out.println("--> Nombre: " + pr.getNombre());
                System.out.println("--> Ciudad: " + pr.getCiudad());
                System.out.println("----------");
            }
        }
    }

    public static void menuGestiones() throws IOException {
        int opcion = -1;
        do {
            System.out.println("------");
            System.out.print("=== Menú gestiones ===\n" +
                    "\t1) Crear Gestion\n" +
                    "\t2) Editar Gestion\n" +
                    "\t3) Borrar Gestion\n" +
                    "\t4) Listado de Gestiones\n" +
                    "\t5) Busqueda por id de Gestiones\n" +
                    "\t6) Volver\n" +
                    "--> Opción: "
            );
            try {
                opcion = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido");
                continue;
            } catch (IOException e) {
                System.out.println("Error");
            }

            switch (opcion) {
                case 1:
                    crearGestion();
                    break;
                case 2:
                    editarGestion();
                    break;
                case 3:
                    borrarGestion();
                    break;
                case 4:
                    mostrarLista(gestiones);
                    break;
                case 5:
                    buscarGestionPorCodigo();
                    break;
                case 6:
                    System.out.println("*Volviendo al menú principal");
                    break;
                default:
                    System.out.println("Error: Introduce un numero entero del 1 al 6");
            }
        } while (opcion != 6);
    }

    public static void crearGestion() throws IOException {
        System.out.println("------");
        System.out.println("--- Crear Gestion ---");

        System.out.print("Código: ");
        String codigo = br.readLine();
        LocalDate fecha;
        try {
            System.out.print("Fecha (yyyy-mm-dd): ");
            fecha = LocalDate.parse(br.readLine());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha no válida. Usa el formato yyyy-MM-dd.");
            return;
        } catch (IOException e) {
            System.out.println("Error al leer la fecha.");
            return;
        }
        System.out.print("Cantidad: ");
        Integer cantidad;
        try {
            cantidad = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("La cantidad debe ser un número entero.");
            return;
        }

        System.out.println("Elige que proveedor formara parte de esta gestion:");
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

        Proveedor proveedor = proveedores.get(opcion);

        System.out.println("Elige que pieza formara parte de esta gestion:");
        mostrarLista(piezas);
        System.out.print("--> Opción: ");

        int opcionPieza = -1;
        try {
            opcionPieza = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcionPieza < 0 || opcionPieza >= piezas.size()) {
            System.out.println("Índice no válido");
            return;
        }

        Pieza pieza = piezas.get(opcionPieza);

        System.out.println("Elige que Proyecto formara parte de esta gestion:");
        mostrarLista(proyectos);
        System.out.print("--> Opción: ");

        int opcionProyecto = -1;
        try {
            opcionProyecto = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcionProyecto < 0 || opcionProyecto >= proyectos.size()) {
            System.out.println("Índice no válido");
            return;
        }

        Proyecto proyecto = proyectos.get(opcionProyecto);

        //Dao
        Gestion gestion = new Gestion(codigo, fecha, cantidad, proveedor, pieza, proyecto);
        proveedor.getGestiones().add(gestion);
        pieza.getGestiones().add(gestion);
        proyecto.getGestiones().add(gestion);

        gestion.setProveedor(proveedor);
        gestion.setPieza(pieza);
        gestion.setProyecto(proyecto);

        gestiones.add(gestion);
        gestionDao.create(gestion);

    }

    public static void editarGestion() throws IOException {
        System.out.println("------");
        System.out.println("--- Editar Gestion ---");

        // Se muestra lista de gestiones y eligen una
        mostrarLista(gestiones);

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

        if (opcion < 0 || opcion >= gestiones.size()) {
            System.out.println("Índice no válido");
            return;
        }
        int indiceGestion = opcion;

        // Nuevos valores
        System.out.print("codigo: ");
        String codigo = br.readLine();
        System.out.print("fecha: ");
        LocalDate fecha;
        try {
            System.out.print("Fecha (yyyy-mm-dd): ");
            fecha = LocalDate.parse(br.readLine());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha no válida. Usa el formato yyyy-MM-dd.");
            return;
        } catch (IOException e) {
            System.out.println("Error al leer la fecha.");
            return;
        }
        System.out.print("cantidad: ");
        int cantidad;
        try {
            cantidad = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("La cantidad debe ser un número entero.");
            return;
        }
        System.out.print("proveedor: ");
        System.out.println("Elige que proveedor formara parte de esta gestion:");
        mostrarLista(proveedores);
        System.out.print("--> Opción: ");

        opcion = -1;
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

        Proveedor proveedor = proveedores.get(opcion);


        System.out.print("pieza: ");
        System.out.println("Elige que pieza formara parte de esta gestion:");
        mostrarLista(piezas);
        System.out.print("--> Opción: ");

        int opcionPieza = -1;
        try {
            opcionPieza = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcionPieza < 0 || opcionPieza >= piezas.size()) {
            System.out.println("Índice no válido");
            return;
        }

        Pieza pieza = piezas.get(opcionPieza);


        System.out.print("proyecto: ");
        System.out.println("Elige que Proyecto formara parte de esta gestion:");
        mostrarLista(proyectos);
        System.out.print("--> Opción: ");

        int opcionProyecto = -1;
        try {
            opcionProyecto = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido");
            return;
        } catch (IOException e) {
            System.out.println("Error");
        }

        if (opcionProyecto < 0 || opcionProyecto >= proyectos.size()) {
            System.out.println("Índice no válido");
            return;
        }

        Proyecto proyecto = proyectos.get(opcionProyecto);

        // Sobreescribir
        Gestion gestion = gestiones.get(indiceGestion);
        Proveedor proveedorAnterior = gestion.getProveedor();
        Pieza piezaAnterior = gestion.getPieza();
        Proyecto proyectoAnterior = gestion.getProyecto();

        gestion.setCodigo(codigo);
        gestion.setFecha(fecha);
        gestion.setCantidad(cantidad);
        gestion.setProveedor(proveedor);
        gestion.setPieza(pieza);
        gestion.setProyecto(proyecto);

        gestionDao.update(gestion);
        if (proveedorAnterior != null && !proveedorAnterior.equals(proveedor)) {
            proveedorAnterior.getGestiones().remove(gestion);
        }
        if (piezaAnterior != null && !piezaAnterior.equals(pieza)) {
            piezaAnterior.getGestiones().remove(gestion);
        }
        if (proyectoAnterior != null && !proyectoAnterior.equals(proyecto)) {
            proyectoAnterior.getGestiones().remove(gestion);
        }
        if (!proveedor.getGestiones().contains(gestion)) {
            proveedor.getGestiones().add(gestion);
        }
        if (!pieza.getGestiones().contains(gestion)) {
            pieza.getGestiones().add(gestion);
        }
        if (!proyecto.getGestiones().contains(gestion)) {
            proyecto.getGestiones().add(gestion);
        }
    }

    public static void borrarGestion() throws IOException {
        System.out.println("------");
        System.out.println("--- Eliminar Gestion ---");

        // Se muestra lista de gestiones y eligen una
        mostrarLista(gestiones);
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

        if (opcion < 0 || opcion >= gestiones.size()) {
            System.out.println("Índice no válido");
            return;
        }

        Gestion aEliminar = gestiones.get(opcion);
        System.out.print("¿Seguro que deseas eliminar esta gestión? (s|n): ");
        String confirmacion = br.readLine();
        if (confirmacion.equalsIgnoreCase("s")) {
            gestionDao.delete(aEliminar);
            if (aEliminar.getProyecto() != null) {
                aEliminar.getProyecto().getGestiones().remove(aEliminar);
            }
            if (aEliminar.getProveedor() != null) {
                aEliminar.getProveedor().getGestiones().remove(aEliminar);
            }
            if (aEliminar.getPieza() != null) {
                aEliminar.getPieza().getGestiones().remove(aEliminar);
            }
            gestiones.remove(opcion);
        } else if (confirmacion.equalsIgnoreCase("n")) {
            System.out.println("Operación cancelada");
        } else {
            System.out.println("Opción no válida");
        }
    }

    public static void buscarGestionPorCodigo() throws IOException {
        System.out.println("------");
        System.out.println("--- Buscar gestion por código ---");

        System.out.print("Código: ");
        String cod = br.readLine();

        boolean encontrada = false;
        for (Gestion gestion : gestiones) {
            if (gestion.getCodigo().equalsIgnoreCase(cod)) {
                System.out.println("- Gestion encontrada -");
                System.out.println("--> Código: " + gestion.getCodigo());
                System.out.println("--> Fecha: " + gestion.getFecha());
                System.out.println("--> Cantidad: " + gestion.getCantidad());
                System.out.println("--> Proveedor: " + (gestion.getProveedor() == null
                        ? "no encontrado" : gestion.getProveedor().getNombre()));
                System.out.println("--> Pieza: " + (gestion.getPieza() == null
                        ? "no encontrada" : gestion.getPieza().getNombre()));
                System.out.println("--> Proyecto: " + (gestion.getProyecto() == null
                        ? "no encontrado" : gestion.getProyecto().getNombre()));

                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha podido encontrar la gestion");
        }
    }

    // MÉTODOS COMUNES
    // Recibe un List de objetos y los muestra
    public static void mostrarLista(List<?> lista) {
        System.out.println("------");
        System.out.println("--- Mostrar lista ---");
        for (int i = 0; i < lista.size(); i++) {
            Object elemento = lista.get(i);
            System.out.println(i + " - " + elemento);
            if (elemento instanceof Proveedor) {
                mostrarGestionesAsociadas(((Proveedor) elemento).getGestiones());
            } else if (elemento instanceof Pieza) {
                mostrarGestionesAsociadas(((Pieza) elemento).getGestiones());
            } else if (elemento instanceof Proyecto) {
                mostrarGestionesAsociadas(((Proyecto) elemento).getGestiones());
            }
        }
    }

    private static void mostrarGestionesAsociadas(List<Gestion> gestionesAsociadas) {
        if (gestionesAsociadas.isEmpty()) {
            System.out.println("   Gestiones asociadas: ninguna");
            return;
        }
        System.out.println("   Gestiones asociadas:");
        for (Gestion gestion : gestionesAsociadas) {
            System.out.println("      " + gestion.getCodigo() + " (" + gestion.getFecha()
                    + "), cantidad: " + gestion.getCantidad());
        }
    }
}
