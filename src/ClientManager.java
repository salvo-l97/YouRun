import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientManager implements Runnable {

    private Socket client_socket;
    private RunList runList;

    public ClientManager(Socket socket, RunList list){
        client_socket = socket;
        this.runList = list;
    }

    @Override
    public void run() {
        System.out.println(">>> Accepted connection from "+" -> "+ Thread.currentThread().getName() + " -> " + client_socket.getRemoteSocketAddress());
        Scanner client_scanner = null;
        PrintWriter pw = null;

        try {
            client_scanner = new Scanner(client_socket.getInputStream());
            pw = new PrintWriter( client_socket.getOutputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }

        String date;
        int length;
        int time;
        boolean go = true;
        while (go){
            String message_from_client = client_scanner.nextLine();  //leggo messaggio ricevuto dal client
            String command = client_scanner.next(); //del messaggio letto che si è ricevuto dal client si legge la prima parola che racchiude il COMANDO


            if (command.equals("QUIT")){
                System.out.println(">>> Closing connection to "+client_socket.getRemoteSocketAddress());
                try {
                    client_socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                go = false;
            } else if (command.equals("ADD")){
                System.out.println(">>> Executing -> ADD");
                client_scanner.useDelimiter("\\s*'\\s*"); //uso come delimitatore l'apostrofo così posso distringuere i vari token
                date = client_scanner.next();
                length = client_scanner.nextInt();
                time = client_scanner.nextInt();

                Run r = new Run();
                r.setDate(date);
                r.setLength(length);
                r.setTime(time);

                runList.add(r);
                System.out.println(">>> Added successfully -> "+r);
                pw.println("ADD_CORRECTLY");
                pw.flush();
            } else if (command.equals("REMOVE")) {
                System.out.println(">>> Executing -> REMOVE");
                client_scanner.useDelimiter("\\s*'\\s*");
                date = client_scanner.next();
                length = client_scanner.nextInt();
                time = client_scanner.nextInt();

                Run r = new Run();
                r.setDate(date);
                r.setLength(length);
                r.setTime(time);

                for (Run run : runList.getList()){
                    int x = run.compareTo(r);
                    if (x == 0){
                        runList.remove(run);
                        System.out.println(">>> Removed succesfully -> "+r);
                        pw.println("REMOVE_CORRECTLY");
                        pw.flush();
                    } else {
                        System.out.println(">>> Run not found, cannot remove -> "+r);
                        pw.println("REMOVE_NOT_CORRECTLY");
                        pw.flush();
                    }
                }
            } else if(command.equals("SHOW")){
                System.out.println(">>> Executing -> SHOW");

                pw.println(runList);
                pw.flush();

            } else if(command.equals("SAVE")){
                System.out.println(">>> Executing -> SAVE");
                String file_name = client_scanner.nextLine();

                try {
                    FileOutputStream fos = new FileOutputStream(file_name);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);

                    oos.writeObject(runList);
                    oos.close();

                    pw.println("SAVE_CORRECTLY");
                    pw.flush();
                } catch (IOException e) {
                    pw.println("SAVE_NOT_CORRECTLY");
                    pw.flush();

                    e.printStackTrace();
                }
            } else if (command.equals("CLEAR")){
                System.out.println(">>> Executing -> CLEAR");
                runList.clear();
                pw.println("CLEAR_CORRECTLY");
                pw.flush();
            }
        }
    }
}
