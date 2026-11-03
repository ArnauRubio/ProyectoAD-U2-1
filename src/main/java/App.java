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

    //static ProveedorDao proveedorDao = new ProveedorDao();


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

        Pieza pieza = new Pieza(codigo, nombre, precio, descripcion);
        piezas.add(pieza);
        //piezaDao.create(pieza);
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
//        System.out.print("Código: ");
//        String codigo = br.readLine();
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
        //piezaDao.update(pieza);
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

    }

    public static void menuProyectos() throws IOException {

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
