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

        String verifica = null;
        String verifica_file = null;

        String date;
        String length;
        String time;
        String file_name;
        String file_code;
        String message_to_client;
        //File f_code = new File("codes.ser");
        //if (f_code.createNewFile()){}
        FileWriter fw_code = null;
        //PrintWriter pw_code = null;
        try {
            fw_code = new FileWriter("codes.ser", true);
            //pw_code = new PrintWriter((fw_code));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

                for (Run run : r_tmp) {

                    int x = run.compareTo(r);
                    /*if (x == 0){
                        runList.remove(run);
                        System.out.println(">>> Removed succesfully -> " + run);
                        pw.println("REMOVE_CORRECTLY");
                        pw.flush();
                    }*/
                    if (x == 0) {
                        runList.remove(run);
                        /*System.out.println(">>> Removed succesfully -> " + run);
                        pw.println("REMOVE_CORRECTLY");
                        pw.flush();*/
                        verifica = "REMOVE_CORRECTLY";
                        break;
                    } else {
                        verifica = "REMOVE_NOT_CORRECTLY";
                    }
                }
                System.out.println(">>> " + verifica);
                if (verifica.equals("REMOVE_CORRECTLY")){
                    pw.println(verifica);
                    pw.flush();
                } else {
                    pw.println(verifica);
                    pw.flush();
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


            } else if(command.equals("SAVE")){
                System.out.println(">>> Executing -> SAVE");
                file_name = client_scanner.next();
                file_code = client_scanner.next();

                //FileWriter fw_code = new FileWriter(f_code);
                try {
                    fw_code.write(file_name + " " + file_code + "\r\n");
                    fw_code.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    FileWriter fw = new FileWriter(file_name);

                    ArrayList<Run> r_tmp;
                    r_tmp = runList.show();

                    for (Run r : r_tmp){
                        fw.write("Date: " + r.getDate() + ", Length: " + r.getLength() + ", Time: " + r.getTime() + "\n");
                    }
                    fw.flush();
                    fw.close();

                    pw.println("SAVE_CORRECTLY");
                    pw.flush();
                } catch (IOException e) {
                    pw.println("SAVE_NOT_CORRECTLY");
                    pw.flush();
                    e.printStackTrace();
                }
            } else if(command.equals("LOAD")){
                System.out.println(">>> Executing -> LOAD");
                file_name = client_scanner.next();
                file_code = client_scanner.next();

                Scanner f_code_scanner = null;
                try {
                    f_code_scanner = new Scanner(new FileReader("codes.ser"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                /*while (f_code_scanner.hasNextLine()) {
                    String info_file = f_code_scanner.nextLine();
                    Scanner s_info_file = new Scanner(info_file);
                    String nomefile = s_info_file.next();
                    String codefile = s_info_file.next();
                    if (nomefile.equals(file_name)) {
                        if (codefile.equals(file_code)) {
                            Scanner file_scanner = null;
                            try {
                                file_scanner = new Scanner(new FileReader(file_name));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            System.out.println(">>> Reading from: " + nomefile);

                            pw.println("START");
                            pw.flush();

                            while (file_scanner.hasNextLine()) {
                                message_to_client = file_scanner.nextLine();
                                System.out.println(">>> Loading: " + message_to_client);
                                pw.println(message_to_client);
                                pw.flush();
                            }
                            pw.println("FINISH");
                            pw.flush();

                        /*} else {
                            System.out.println(">>> Cant't find code for " + file_name);
                            pw.println("CODE_ERROR");
                            pw.flush();
                        }
                    } else {
                        System.out.println(">>> Cant't find file " + file_name);
                        pw.println("FILE_ERROR");
                        pw.flush();
                    }
                        }
                    }
                }*/
                while (f_code_scanner.hasNextLine()) {
                    String info_file = f_code_scanner.nextLine();
                    Scanner s_info_file = new Scanner(info_file);
                    String nomefile = s_info_file.next();
                    String codefile = s_info_file.next();
                    if (nomefile.equals(file_name) && codefile.equals(file_code)) {
                        verifica_file = "FILE_EXIST";
                        break;
                    } else {
                        verifica_file = "FILE_NOT_EXIST";
                    }
                }

                System.out.println(">>> " + verifica_file);

                if (verifica_file.equals("FILE_EXIST")){
                    Scanner file_scanner = null;
                    try {
                        file_scanner = new Scanner(new FileReader(file_name));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println(">>> Reading from: " + file_name);

                    pw.println("START");
                    pw.flush();

                    while (file_scanner.hasNextLine()) {
                        message_to_client = file_scanner.nextLine();
                        System.out.println(">>> Loading: " + message_to_client);
                        pw.println(message_to_client);
                        pw.flush();
                    }
                    pw.println("FINISH");
                    pw.flush();
                } else {
                    pw.println("File doesn't exist");
                    pw.flush();
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
