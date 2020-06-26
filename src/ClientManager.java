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
        String length;
        String time;
        boolean go = true;
        while (go){
            //String message_from_client = client_scanner.nextLine();  //leggo messaggio ricevuto dal client
            String command = client_scanner.nextLine(); //del messaggio letto che si è ricevuto dal client si legge la prima parola che racchiude il COMANDO


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
                //client_scanner.useDelimiter("\\s*|\\s*"); //uso come delimitatore l'apostrofo così posso distringuere i vari token
                //date = client_scanner.next();
                //length = client_scanner.nextInt();
                //time = client_scanner.nextInt();

                String message = client_scanner.nextLine();
                int fdate = message.indexOf("*");
                int flentgh = message.indexOf("|");
                int ftime = message.indexOf("-");

                date = message.substring(0,fdate);
                length = message.substring(fdate+1,flentgh);
                time = message.substring(flentgh+1,ftime);

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
               // client_scanner.useDelimiter("\\s*'\\s*");
                //date = client_scanner.next();
                //length = client_scanner.nextInt();
                //time = client_scanner.nextInt();

                String message = client_scanner.nextLine();
                int fdate = message.indexOf("*");
                int flentgh = message.indexOf("|");
                int ftime = message.indexOf("-");

                date = message.substring(0,fdate);
                length = message.substring(fdate+1,flentgh);
                time = message.substring(flentgh+1,ftime);

                Run r = new Run();
                r.setDate(date);
                r.setLength(length);
                r.setTime(time);

                ArrayList<Run> r_tmp;
                r_tmp = runList.getcopy();

                for (Run run : r_tmp){

                    int x = run.compareTo(r);
                    if (x == 0){
                        runList.remove(run);
                        System.out.println(">>> Removed succesfully -> " + run);
                        pw.println("REMOVE_CORRECTLY");
                        pw.flush();
                    }
                }


            } else if(command.equals("SHOW")){
                System.out.println(">>> Executing -> SHOW");

                pw.println("START");
                pw.flush();

                ArrayList<Run> r_tmp;
                r_tmp = runList.show();

                for (Run r : r_tmp){
                    pw.println(r);
                    pw.flush();
                }

                pw.println("FINISH");
                pw.flush();

                /*pw.println(runList);
                pw.flush();*/

            } else if(command.equals("SAVE")){
                System.out.println(">>> Executing -> SAVE");
                String file_name = client_scanner.nextLine();

                try {
                    //FileOutputStream fos = new FileOutputStream(file_name);
                    //ObjectOutputStream oos = new ObjectOutputStream(fos);
                    FileWriter fw = new FileWriter(file_name);

                    ArrayList<Run> r_tmp;
                    r_tmp = runList.show();

                    for (Run r : r_tmp){
                        fw.write("Date: " + r.getDate() + ", Length: " + r.getLength() + ", Time: " + r.getTime());
                        fw.close();
                    }

                    //oos.writeObject(r_tmp);
                    //oos.close();

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
