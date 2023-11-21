
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static com.sun.tools.javac.util.StringUtils.toLowerCase;

// Juego sencillo de piedra papel o tijera entre dos clientes y un servidor
public class Cliente {

    //IP y Puerto a la que nos vamos a conectar
    public static final int PUERTO = 2000;
    public static final String IP_SERVER = "localhost";

    public static void main(String[] args) {
        System.out.println("        BIENVENIDO AL JUEGO         ");
        System.out.println("        APLICACIÓN CLIENTE         ");
        System.out.println("-----------------------------------");

        //Socket -> es la clase que nos va a permitir comunicarnos con el servidor

        //InputStreamReader entrada -> Entrada de datos. Es el canal de entrada del
        //cliente, es decir, el canal por el cual el servidor nos va a mandar la
        //informaci�n.

        //PrintStream salida -> Salida de datos. Es el canal de salida del cliente,
        //es decir, el canal por el cual vamos a enviar informaci�n al servidor.

        //En este objeto vamos a encapsular la IP y el puerto al que nos vamos a conectar
        InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);

        //Usaremos la declaracion try-with-resources, esta declaraci�n nos asegura que
        //todos los objetos que declaremos e instanciemos dentro del bloque try, ser�n
        //cerrados cuando salgan del bloque try-catch. Es muy util parar ahorranos codigo
        //ya que sino deber�amos cerrarlos (ejecutar su metodo close()), dentro del bloque
        //finally, creando muchas m�s lineas para ello.
        try (Scanner sc = new Scanner(System.in);
             Socket socketAlServidor = new Socket()){

            //Pedimos al usuario los numeros a sumar
            System.out.println("CLIENTE: Introduzca piedra, papel o tijera");
            String eleccion = sc.nextLine(); // supongamos piedra
            //Con el método toLowerCase() convertimos la cadena a minúsculas para evitar compricaiones a la hora de enviar la información
            toLowerCase(eleccion);
            String eleccionJ1 = eleccion;

            //se mandan a traves de un socket se mandan SIEMPRE en formato cadena

            //Establecemos la conexión
            System.out.println("CLIENTE: Esperando a que el servidor acepte la conexión");
            socketAlServidor.connect(direccionServidor);
            System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER
                    + " por el puerto " + PUERTO);

            //Creamos el objeto que nos permite mandar información al servidor
            PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
            //Mandamos la información por el Stream
            salida.println(eleccionJ1);// piedra

            //Creamos el objeto que nos va a permitir leer la salida del servidor
            InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());

            //Esta clase nos ayuda a leer datos del servidor linea a linea en vez de
            //caracter a caracter como la clase InputStreamReader
            BufferedReader bf = new BufferedReader(entrada);

            System.out.println("CLIENTE: Esperando al resultado del servidor...");
            //En la siguiente linea se va a quedar parado el hilo principal
            //de ejecuci�n hasta que el servidor responda, es decir haga un println
            String resultado = bf.readLine();//7

            System.out.println("CLIENTE: El resultado es: " + resultado);

            //Nota, no cierro ni "entrada" ni "salida" ya que se encarga el objeto
            //socket cuando salgamos del bloque try-catch
        } catch (UnknownHostException e) {
            System.err.println("CLIENTE: No encuentro el servidor en la direcci�n" + IP_SERVER);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("CLIENTE: Error de entrada/salida");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("CLIENTE: Error -> " + e);
            e.printStackTrace();
        }

        System.out.println("CLIENTE: Fin del programa");
    }

}

