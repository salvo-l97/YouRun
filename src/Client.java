import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    private int port;
    private String address;

    public Client(String an_address, int a_port){
        this.address = an_address;
        this.port = a_port;
    }

    public static void main(String args[]){

        if(args.length != 2){
            System.out.println("USE -> java Client 'address' 'port'");
            return;
        }

        Client client = new Client(args[0], Integer.parseInt(args[1]));
        client.start();
    }

    public void start(){
        System.out.println("Connecting to "+ address + port);
        try {
            socket = new Socket(address,port);
            System.out.println("Connection established with "+ address + port);

            Scanner user_scanner = new Scanner(System.in);
            Scanner server_scanner = new Scanner(socket.getInputStream());
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            String message_to_server;
            String message_from_server;
            String date;
            String length;
            String time;
            String file_name;
            String file_code;
            boolean go = true;
            int c;
            //System.out.println("Welcome to YouRun");
            while (go){

                //System.out.println("Welcome to YouRun");
                System.out.println("Choose one of the following options:");
                System.out.println("   (0) -> Quit connection with server");
                System.out.println("   (1) -> Add a run");
                System.out.println("   (2) -> Remove a run");
                System.out.println("   (3) -> Show the list");
                System.out.println("   (4) -> Create a file txt and save list");
                System.out.println("   (5) -> Load list from a file txt");
                System.out.println("   (6) -> Clear the list");
                System.out.println("Type your choice -> ");
                c = user_scanner.nextInt();

                switch (c){
                    case 0:
                        //quit
                        go = false;
                        System.out.println(">>> Closing connection with the server at "+address+port);
                        message_to_server = "QUIT";
                        pw.println(message_to_server);
                        pw.flush();
                        break;
                    case 1:
                        //add
                        message_to_server = "ADD";
                        pw.println(message_to_server);
                        pw.flush();

                        user_scanner.useDelimiter("\\s*\n\\s*"); //il delimitatore sarà uno o più spazi seguito da un ritorno a capo
                        System.out.println("Insert date: ");
                        date = user_scanner.next();
                        System.out.println("Insert length with its unit of measurement: ");
                        length = user_scanner.next();
                        System.out.println("Insert time with its unit of measurement: ");
                        time = user_scanner.next();


                        message_to_server = date+"*"+length+"|"+time+"-";
                        pw.println(message_to_server);
                        pw.flush();

                        message_from_server = server_scanner.nextLine();
                        if(message_from_server.equals("ADD_CORRECTLY")){
                            System.out.println("The run has been added");
                        } else {
                            System.out.println("UNKNOWN ERROR -> "+message_from_server);
                        }

                        break;

                    case 2:
                        //remove
                        message_to_server = "REMOVE";
                        pw.println(message_to_server);
                        pw.flush();

                        user_scanner.useDelimiter("\\s*\n\\s*");
                        System.out.println("Insert date: ");
                        date = user_scanner.next();
                        System.out.println("Insert length: ");
                        length = user_scanner.next();
                        System.out.println("Insert time: ");
                        time = user_scanner.next();

                        //message_to_server = "REMOVE"+" ' "+date+" ' "+length+" ' "+time;

                        message_to_server = date+"*"+length+"|"+time+"-";
                        pw.println(message_to_server);
                        pw.flush();

                        message_from_server = server_scanner.nextLine();
                        if(message_from_server.equals("REMOVE_CORRECTLY")){
                            System.out.println("The run has been removed");
                        } else if(message_from_server.equals("REMOVE_NOT_CORRECTLY")){
                            System.out.println("The run doesen't exist");
                        } else {
                            System.out.println("UNKNOWN ERROR -> "+message_from_server);
                        }

                        break;

                    case 3:
                        message_to_server = "SHOW";
                        pw.println(message_to_server);
                        pw.flush();

                        message_from_server = server_scanner.nextLine();
                        boolean show = true;

                        if (message_from_server.equals("START")){
                            while (show){
                                message_from_server = server_scanner.nextLine();
                                if (message_from_server.equals("FINISH")){
                                    show = false;
                                    //System.out.println("List ended");
                                } else {
                                    System.out.println(message_from_server);
                                }
                            }
                        } else {
                            System.out.println("UNKNOWN ERROR -> "+message_from_server);
                        }

                        break;
                    case 4:
                        //save
                        user_scanner.useDelimiter("\\s*\n\\s*");
                        System.out.println("Insert name of the file txt: ");
                        file_name = user_scanner.next();

                        System.out.println("Insert code to create the file txt: ");
                        file_code = user_scanner.next();

                        message_to_server = "SAVE";
                        pw.println(message_to_server);
                        pw.flush();

                        message_to_server = file_name;
                        pw.println(message_to_server);
                        pw.flush();

                        message_to_server = file_code;
                        pw.println(message_to_server);
                        pw.flush();

                        message_from_server = server_scanner.nextLine();
                        if(message_from_server.equals("SAVE_CORRECTLY")){
                            System.out.println("The list has been saved");
                        } else if(message_from_server.equals("SAVE_NOT_CORRECTLY")){
                            System.out.println("The list wasn't saved");
                        } else {
                            System.out.println("UNKNOWN ERROR -> "+message_from_server);
                        }
                        break;

                    case 5:
                        user_scanner.useDelimiter("\\s*\n\\s*");
                        System.out.println("Insert name of the file txt: ");
                        file_name = user_scanner.next();

                        System.out.println("Insert code of the file txt: ");
                        file_code = user_scanner.next();

                        message_to_server = "LOAD";
                        pw.println(message_to_server);
                        pw.flush();

                        message_to_server = file_name;
                        pw.println(message_to_server);
                        pw.flush();

                        message_to_server = file_code;
                        pw.println(message_to_server);
                        pw.flush();

                        message_from_server = server_scanner.nextLine();
                        boolean load_list = true;

                        if (message_from_server.equals("START")) {
                            System.out.println(file_name + " content:");
                            while (load_list) {
                                message_from_server = server_scanner.nextLine();
                                if (message_from_server.equals("FINISH")) {
                                    load_list = false;
                                } else {
                                    System.out.println(message_from_server);
                                }
                            }
                        } else {
                            System.out.println("Error loading file -> "+message_from_server);
                        }

                        break;

                    case 6:
                        pw.println("CLEAR");
                        pw.flush();

                        message_from_server = server_scanner.nextLine();
                        if(message_from_server.equals("CLEAR_CORRECTLY")){
                            System.out.println("The list has been cleared");
                        } else {
                            System.out.println("UNKNOWN ERROR -> "+message_from_server);
                        }
                        break;

                    default:
                        System.out.println("Invalid choice -> "+c);
                }




            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public void login() {

    }*/

}
