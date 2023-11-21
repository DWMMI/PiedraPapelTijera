
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor {

    public static final int PUERTO = 2000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("      BIENVENIDO AL JUEGO       ");
        System.out.println("      APLICACIÓN DEL JUGADOR 2      ");
        System.out.println("----------------------------------");

        //ServidorSocket -> Este objeto es el que estar� escuchando peticiones por un puerto
        //y creara un objeto socket por cada peticion

        //Entrada de datos. Es el canal de entrada del servidor, es decir, el canal por
        //el cual el cliente nos va a mandar la informaci�n.
        InputStreamReader entrada = null;
        //Salida de datos. Es el canal de salida del servidor, es decir, el canal por
        //el cual vamos a enviar informaci�n al cliente.
        PrintStream salida = null;

        //Notese como ahora:
        //1. El PrintStream del cliente es el InputStreamReader del servidor
        //2. El PrintStream del servidor es el InputStreamReader del cliente

        //Socket -> es la clase que nos va a permitir comunicarnos con el cliente,
        //en este caso no lo crearemos nosotros, sino que sera el SocketServer quien
        //lo cree cuando acepte una peticion de un cliente.
        Socket socketAlCliente = null;

        InetSocketAddress direccion = new InetSocketAddress(PUERTO);

        //En este caso no podemos hacer la declaracion try-with-resources como antes
        //ya que el servidor es un hilo que no para nunca y por cada peticion
        //crea un nuevo objeto Socket a partir del objeto ServerSocket, es decir,
        //sera el objeto ServerSocket el que nos crerara el objeto Socket por nosostros
        try (ServerSocket serverSocket = new ServerSocket()) {

            //Decimos al server socket que escuche peticiones desde el puerto
            //que hayamos establecido
            serverSocket.bind(direccion);

            //Vamos a llevar la cuenta del numero de peticiones que nos llegan
            int peticion = 0;

            //Estamos continuamente escuchando, es lo normal dentro del comportamiento
            //de un servidor, un programa que no para nunca
            while (true) {

                int i = 0;
                int j = 0;
                System.out.println("SERVIDOR: Esperando peticion por el puerto " + PUERTO);

                //En este punto, se parara el programa, hasta que entre la peticion de
                //un cliente, y sera en ese momento cuando se cree un objeto Socket
                socketAlCliente = serverSocket.accept();
                System.out.println("SERVIDOR: peticion " + ++peticion + " recibida");

                do {
                    entrada = new InputStreamReader(socketAlCliente.getInputStream());
                    BufferedReader bf = new BufferedReader(entrada);

                    //El servidor se quedar�a aqu� parado hasta que el cliente nos mande
                    //informacion, es decir, cuando haga un salida.println(INFORMACION)


                    String stringRecibido = bf.readLine();
                    System.out.println("Jugador2 escribe tu elección: piedra, papel o tijera");

                    //introducir en el servidor la eleccion del jugador 2
                    Scanner sc = new Scanner(System.in);
                    String respuesta2 = sc.next();
                    String eleccionJ2 = respuesta2.toLowerCase();

                    //determinar ganador
                    String resultado = determinarGanador(eleccionJ2, stringRecibido);
                    System.out.println("SERVIDOR: " + resultado);

                    //Hay que tener en cuenta que toda comunicacion entre cliente y servidor
                    //esta en formato de cadena de texto
                    System.out.println("SERVIDOR: Me ha llegado del cliente: " + stringRecibido);
                    if (resultado.equals("¡Perdiste!")) {
                        i++;
                    } else if (resultado.equals("¡Ganaste!")) j++;

                    String iString = Integer.toString(i);
                    String jString = Integer.toString(j);

                    //Mandamos el resultado al cliente
                    salida = new PrintStream(socketAlCliente.getOutputStream());
                    salida.println(stringRecibido + "-" + eleccionJ2 + "-" + iString + "-" + jString);
                    System.out.println(stringRecibido + "-" + eleccionJ2 + "-" + iString + "-" + jString);

                    if (i == 3 || j == 3) {
                        stringRecibido = bf.readLine();
                        if (stringRecibido.equals("finCliente")) {

                            System.out.println("----El servidor ha perdido----");

                        } else if (stringRecibido.equals("finServidor")) {

                            System.out.println("----El servidor ha ganado----");

                        }
                    }
                } while (i < 3 || j < 3);
                socketAlCliente.close();
            }
        } catch (IOException e) {
            System.err.println("SERVIDOR: Error de entrada/salida");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("SERVIDOR: Error -> " + e);
            e.printStackTrace();
        }
    }//FIN DEL PROGRAMA

    public static String determinarGanador(String jugador1, String jugador2) {
        if (jugador1.equals(jugador2)) {
            return "¡Es un empate!";
        } else if ((jugador1.equals("piedra") && jugador2.equals("tijera")) ||
                (jugador1.equals("papel") && jugador2.equals("piedra")) ||
                (jugador1.equals("tijera") && jugador2.equals("papel"))) {
            return "¡Ganaste!";
        } else {
            return "¡Perdiste!";
        }
    }
}

