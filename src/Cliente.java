import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

    public static final int PUERTO = 2000;
    public static final String IP_SERVER = "10.34.121.253";

    public static void main(String[] args) {
        System.out.println("        BIENVENIDO AL JUEGO         ");
        System.out.println("        APLICACIÓN CLIENTE         ");
        System.out.println("-----------------------------------");

        InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
        String[] situacionPartida;
        try (Scanner sc = new Scanner(System.in); Socket socketAlServidor = new Socket()) {

            System.out.println("CLIENTE: Esperando a que el servidor acepte la conexión");
            socketAlServidor.connect(direccionServidor);
            System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER + " por el puerto " + PUERTO);

            do {

                System.out.println("CLIENTE: Introduzca piedra, papel o tijera");
                String eleccion = sc.nextLine();

                String eleccionJ1 = eleccion;

                PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
                salida.println(eleccionJ1);

                InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());

                BufferedReader bf = new BufferedReader(entrada);

                System.out.println("CLIENTE: Esperando al resultado del servidor...");

                String resultado = bf.readLine();
                situacionPartida = resultado.split("-");
                System.out.println("Has elegido: " + situacionPartida[0] + " y tu contrincante ha elegido: "
                        + situacionPartida[1]);
                if ("3".equals(situacionPartida[2])) {
                    System.out.println("Has ganado");
                    salida.println("finCliente");
                }

                if ("3".equals(situacionPartida[3])) {
                    System.out.println("Has perdido");
                    salida.println("finServidor");

                }
            } while (!(situacionPartida[2].equals("3") || situacionPartida[3].equals("3")));

        } catch (UnknownHostException e) {
            System.err.println("CLIENTE: No encuentro el servidor en la direcci n" + IP_SERVER);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("CLIENTE: Error de entrada/salida");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("CLIENTE: Error -> " + e);
            e.printStackTrace();
        }

    }

}
