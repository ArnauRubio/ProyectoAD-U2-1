import models.Gestion;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws IOException {

        mostrarMenuPrincipal();


    }

    public static void mostrarMenuPrincipal() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int opcion = 0;
        do {
            System.out.print("=========\n" +
                    "\tMENÚ\n" +
                    "=========\n" +
                    "\t1) Piezas\n" +
                    "\t2) Proveedores\n" +
                    "\t3) Proyectos\n" +
                    "\t4) Salir\n" +
                    "--> Opción: "
            );
            opcion = Integer.parseInt(br.readLine());

            switch (opcion) {
                case 1:
                    menuPiezas();
                    break;
                case 2:
                    menuProveedores();
                    break;
                case 3:
                    menuProyectos();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Introduce un numero entero entre 1 y 4");
            }
        } while (opcion != 4);
    }

    public static void menuPiezas() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Gestion gestion = new Gestion();
        int opcion = 0;
        do {
            System.out.print("=========\n" +
                    "\tGestion de piezas\n" +
                    "=========\n" +
                    "\t1) Crear Pieza\n" +
                    "\t2) Editar Pieza\n" +
                    "\t3) Borrar Pieza\n" +
                    "\t4) Listado de Piezas\n" +
                    "\t5) Busqueda por Nombre de Pieza\n" +
                    "\t6) Busqueda por id de Pieza\n" +
                    "\t7) Volver\n" +
                    "--> Opción: "

            );
            opcion = Integer.parseInt(br.readLine());

            switch (opcion) {
                case 1:
                    System.out.println("¿Cual es su codigo?:");
                    String codigo = br.readLine();
                    System.out.println("¿Cual es su nombre?:");
                    String nombre = br.readLine();
                    System.out.println("¿Cual es su precio?:");
                    double precio = Double.parseDouble(br.readLine());
                    System.out.println("¿Cual es su descripcion?:");
                    String descripcion = br.readLine();

                    Pieza pieza = new Pieza(codigo, nombre, precio, descripcion);
                    gestion.setPieza(pieza);
                    break;
                case 2:
                    System.out.println("Elije que pieza editar:");

                    break;
                case 3:
                    System.out.println("Elije que pieza borrar:");
                    break;
                case 4:
                    System.out.println("Aqui estan todas las piezas:");
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

    public static void menuProveedores() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Gestion gestion = new Gestion();
        int opcion = 0;

        do {
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
            opcion = Integer.parseInt(br.readLine());

            switch (opcion) {
                case 1:
                    System.out.println("¿Cual es el codigo de tu proveedor?:");
                    String codigo = br.readLine();
                    System.out.println("¿Cual es su nombre?:");
                    String nombre = br.readLine();
                    System.out.println("¿Cual es su apellido?:");
                    String apellido = br.readLine();
                    System.out.println("¿Cual es su direccion?:");
                    String direccion = br.readLine();

                    Proveedor prov = new Proveedor(codigo, nombre, apellido, direccion);
                    gestion.setProveedor(prov);
                    break;
                case 2:
                    System.out.println("Elije que proveedor editar:");
                    break;
                case 3:
                    System.out.println("Elije que proveedor borrar:");
                    break;
                case 4:
                    System.out.println("Aqui estan todos los proveedor:");
                    break;
                case 5:
                    System.out.println("Introduce el nombre del proveedor a buscar:");
                    String nomProvBuscar = br.readLine();
                    break;
                case 6:
                    System.out.println("Introduce el id del proveedor a buscar:");
                    int idProvBuscar = Integer.parseInt(br.readLine());
                    break;
                case 7:
                    System.out.println("Introduce la direccion del proveedor a buscar:");
                    int direccionProvBuscar = Integer.parseInt(br.readLine());
                    break;
                case 8:
                    break;
                default:
                    System.out.println("Error: Introduce un numero entero del 1 al 7");

            }
        } while (opcion != 8);
    }

    public static void menuProyectos() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Gestion gestion = new Gestion();
        int opcion = 0;

        do {
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
            opcion = Integer.parseInt(br.readLine());

            switch (opcion) {
                case 1:
                    System.out.println("¿Cual es el codigo de tu proyecto?:");
                    String codigo = br.readLine();
                    System.out.println("¿Cual es su nombre?:");
                    String nombre = br.readLine();
                    System.out.println("¿Cual es su ciudad?:");
                    String ciudad = br.readLine();

                    Proyecto proy = new Proyecto(codigo, nombre, ciudad);
                    gestion.setProyecto(proy);
                    break;
                case 2:
                    System.out.println("Elije que proyecto editar:");
                    break;
                case 3:
                    System.out.println("Elije que proyecto borrar:");
                    break;
                case 4:
                    System.out.println("Aqui estan todos los proyecto:");
                    break;
                case 5:
                    System.out.println("Introduce el nombre del proyecto a buscar:");
                    String nomProyBuscar = br.readLine();
                    break;
                case 6:
                    System.out.println("Introduce el id del proyecto a buscar:");
                    int idProyBuscar = Integer.parseInt(br.readLine());
                    break;
                case 7:
                    System.out.println("Introduce la ciudad del proyecto a buscar:");
                    int ciudadProyBuscar = Integer.parseInt(br.readLine());
                    break;
                case 8:
                    break;
                default:
                    System.out.println("Error: Introduce un numero entero del 1 al 7");
            }
        } while (opcion != 8);
    }
}
