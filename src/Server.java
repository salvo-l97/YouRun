import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    ServerSocket server_socket;
    Socket client_socket;
    //RunList list = new RunList();
    private int port;
    //File f_code = new File("code.ser");

   /* FileWriter code_file;

    {
        try {
            code_file = new FileWriter("codes.ser",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public Server(int a_port){
        this.port = a_port;
    }

    public static void main(String args[]){

        if(args.length != 1){
            System.out.println("USE -> java Server 'port'");
            return;
        }

        Server server = new Server(Integer.parseInt(args[0]));
        server.start();
    }

    public void start(){
        try {
            System.out.println("Starting server on port "+port);
            server_socket = new ServerSocket(port);
            System.out.println("Server started");

            while(true){
                System.out.println(">>> Server waits for connection on port "+port);
                client_socket = server_socket.accept();
                System.out.println(">>> Server accepted connection from "+client_socket.getRemoteSocketAddress());

                RunList list = new RunList();
                ClientManager cm = new ClientManager(client_socket,list);
                Thread t = new Thread(cm);
                t.start();

            }


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(">>> Server cannot start on port "+port);
        }
    }


}
