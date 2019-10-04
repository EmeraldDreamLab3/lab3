import java.io.*;
import java.net.*;
import java.util.Scanner;

// Written by Dennis Perea and Christopher Walls

public class chat_client implements Runnable{

    public Socket clientSock = null;
    HelloState state = null;
    private BufferedReader in = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    /* default constructor */
    public chat_client(Socket clientSocket, HelloState state){
        this.clientSock = clientSocket;
        this.state = state;
    }

    /* constructor to put ip address and port */
    public chat_client (String address, int port){
        /* make connection */
        try {
            clientSock = new Socket (address, port);
        } catch (Exception i) {
            System.out.println("Error in IP or port");
            System.exit(1);
        }

        // try{
        //     in = new BufferedReader(new InputStreamReader(System.in));
        //     input = new DataInputStream(clientSock.getInputStream());
        //     output = new DataOutputStream(clientSock.getOutputStream());
        // } catch (IOException i) {
        //     System.out.println(i);
        // }

        try {
            
            /* establish means of communicating between server/client & to console */
            in = new BufferedReader(new InputStreamReader(System.in));
            input = new DataInputStream(clientSock.getInputStream());
            output = new DataOutputStream(clientSock.getOutputStream());
            // System.out.println("Enter client name:");
            // server asks for client to enter name
            System.out.println(in.readUTF());
            // user responds with client name
            // server greets user and prompts which client to connect to

            // UNDER THIS LINE IDK IF IT WORKS
            name = keyboard.nextLine();
            output.writeUTF(name);
            System.out.println(input.readUTF());


            // for(int i = countdown;i>0;--i){
            //     output.writeUTF("Closing in " + i +"...");
            //     Thread.sleep(1000);
            // }
            output.writeUTF("Good bye\n");
            output.close();
            input.close();
        }catch(EOFException e){
            System.out.println("Server closed connection");
        }
        catch (Exception e){
            System.out.println("Client"+e.getMessage());
        }
    }
    public static void main(String args[]) 
    { 
	    if (args.length < 2) {
	        System.out.println("Client usage: java Client #IP_address #port_number");
	    } else {
		Client client = new Client(args[0], Integer.parseInt(args[1])); 
	    }
    }

}