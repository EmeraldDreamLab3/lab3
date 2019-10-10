import java.io.*; 
import java.util.*; 
import java.net.*; 

// Server class 
public class Server 
{ 
	static ArrayList<ClientHandler> listOfUsers;
	public static void main(String[] args) throws IOException 
	{ 
		// Port the server is listening to 
		ServerSocket ss = new ServerSocket(5056); 
		System.out.println("Server started...");
		// List of users

		/* **
		** TODO: CHECK up to 100 users 
		** */

        listOfUsers = new ArrayList<ClientHandler>();
        
		// Infinite loop bc clients are connecting
		while (true) 
		{ 
			Socket s = null; 
			System.out.println("Waiting for a client...");
			/* **
			** TODO: constantly loop through peer connection list to check for updates, when its different send it out
			** */
			try
			{ 
				// socket object to receive incoming client requests 
				s = ss.accept(); 
				System.out.println("Client accepted"); 
				
				// obtaining input and out streams 
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
				
				//System.out.println("Assigning new thread for this client"); 

				// create a new thread object 
				ClientHandler t = new ClientHandler(s, dis, dos); 
                // add client to list of clients
				listOfUsers.add(t);
				// Invoking the start() method 
				t.start();
			} 
			catch (Exception e){ 
				ss.close();
				s.close(); 
				e.printStackTrace(); 
			} 
		} 
	}
	public ArrayList<ClientHandler> getList() {
		return listOfUsers;
	} 

	public void printList() {
		System.out.println("List of clients and states");
		for (ClientHandler e : listOfUsers) {
			System.out.println(e.getUserName() +"\t"+e.getState());
		}
	}
	public String stringList() {
		String s = "List of clients & states\n";
		for (ClientHandler e : listOfUsers) {
			s = s + e.getUserName() +"\t"+e.getState()+"\n";
		}
		return s;
	}
} 

// ClientHandler class 
class ClientHandler extends Thread 
{ 
	// instance of server to access ArrayList
	Server myServer = new Server();
	final DataInputStream dis; 
	final DataOutputStream dos; 
    final Socket s; 
    // bool to check if user name was set
	boolean setUserName;
	// bool to check if target user to chat to has been set to
	boolean setTargetUser;
	String userName;
	String targetUser;
	
	/* **
		** TODO: to store name, STATE, and socket
		** */
    
	

	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) 
	{ 
		this.s = s; 
		this.dis = dis; 
        this.dos = dos; 
		setUserName = false;
		setTargetUser = false;
	} 

	@Override
	public void run() 
	{ 
		String received;
		// made a temp to see what was blocking user from writing in switch statement after Speak to User 
		String temp = "";
		String toreturn; 
		while (true) 
		{ 
            try { 
				//  !!If user has not set their user name yet, set it
				if (setUserName == false) {
					dos.writeUTF("Enter client name: ");

					// receive the answer from client 
					received = dis.readUTF(); 
					userName = received;
					//dos.writeUTF("You set your username to:" + userName);
					setUserName = true;
					//send list to users once a connection is established
					myServer.printList();
					dos.writeUTF(myServer.stringList());
				}
				
				//  !!If user has not designated who they want to talk to, set their target user
				while(temp!="Speak to User")
				{
					if (setTargetUser == false) {
						dos.writeUTF("Select an Option: [Speak to User | Exit]..\n"); 
						

						// receive the answer from client 
						received = dis.readUTF(); 
						temp = received;

						//add to switch statement to continue unless exit
						/*
						** TODO add case where another user can connect anytime and they can accept/decline
						*/
							switch (temp) {     
								case "Speak to User" :
									dos.writeUTF("Please list who you wish to speak to:");
									//dos.writeUTF(myServer.stringList());
									//dis.readUTF() <-- Make sure to have client write to unblock this
									received = dis.readUTF();
									targetUser = received;
									setTargetUser = true;
									break;
							
								case "Exit":
									System.out.println("Client " + this.s + " sends exit..."); 
									System.out.println("Closing this connection."); 
									this.s.close(); 
									this.dis.close();
									this.dos.close();
									System.out.println("Connection closed"); 
									Thread.sleep(20000); 

									/* **
									** TODO: server needs to update client information and broadcast peer connection table 
									** */

									// we only want this thread to close It doesnt not do this yet
									break; 
								default: 
									dos.writeUTF("Invalid Option"); 
									break; 
							}//end switch
					}//end if
				}//end while

				/* **
				** TODO connect users to eachother via input
				** */
				//clienthandler ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) comparison userName(str) & received
				//arraylist listOfUsers = new ArrayList<ClientHandler>(); use myServer object to access helper methods
				/* pseudo code
				we have the userName of the ClientHandler we want to connect to
				1. check myServer.ArrayList<ClientHandler>.getUserName() == received
				
				1. make method to grab ArrayList<ClientHandler>
				2. while loop to check for threads to send to client, and server should forward between them

				not finished here clearly, am brain dead
				*/
				System.out.println("You are connected to " + targetUser);

			}catch(InterruptedException e){
				//catch block for thread sleep()
				System.out.println("Shutdown timeout interrupted, shutting down immediately");
			}catch (IOException e) { 
				e.printStackTrace(); 
				System.out.println("Shutting down");
				//actually exit instead of infinite loop
				System.exit(0);
			} 
			try
			{ 
				// closing resources
				this.dis.close(); 
				this.dos.close(); 
				
			}catch(IOException e){ 
				e.printStackTrace(); 
			}
        }  
    } 
    
    public String getUserName() {
        return userName;
    }
} 
