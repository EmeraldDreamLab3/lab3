import java.io.*; 
import java.util.*; 
import java.net.*; 

// Server class 
public class Server 
{ 
	static ArrayList<ClientHandler> listOfUsers;
	final static int MAX_CLIENTS = 100;
	public static void main(String[] args) throws IOException 
	{ 
		if (args.length < 1) {
			System.out.println("Server usage: java Server #port_number");
		}

		// Port the server is listening to 
		int port = Integer.parseInt(args[0]);
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port); 
		} catch (Exception e) {
			System.out.println("Error in port");
		}

		System.out.println("Server started...");
		// List of users

        listOfUsers = new ArrayList<ClientHandler>(MAX_CLIENTS);
        
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
				if (listOfUsers.size() >= 100) {
					System.out.println("Maximum number of clients connected.");
				}
				s = ss.accept(); 
				System.out.println("Client accepted"); 
				
				// obtaining input and out streams 
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

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

	//could do state as boolean
	String state = "free";
	String userName;
	String targetUser;

	// peer connection list
	HashMap<ClientHandler, ClientHandler> pcl = new HashMap<>();
	// HashMap<String, DataOutputStream> connOut = new HashMap<>();
	// HashMap<String, DataInputStream> connIn = new HashMap<>();
	
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
						dos.writeUTF("Select an Option: [Speak to User | Exit].."); 
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
									// new if branch to search

									//should be a smaller try block
									for (ClientHandler e : myServer.listOfUsers) {
										if(e.getUserName().equalsIgnoreCase(targetUser)) {
											if (e.get_State().equalsIgnoreCase("free")) {
												// then they can connect!!!!
												pcl.put(this, e);
												pcl.put(e, this);
												//update targetUser
												e.setState("busy");
												this.setState("busy");
												dos.writeUTF("You are connected to " + targetUser);
											} else {
												//dos.writeUTF("User is busy.");
											}
										} else {
											//dos.writeUTF("User is not connected to the server.");
										}
									}

									// add some breaks to this for they can leave eternal doom
									while(pcl.containsKey(this)) {
										// constantly read client
										if (dis.available() > 0) {
											pcl.get(this).dos.writeUTF(dis.readUTF());
										}
										if (pcl.get(this).dis.available() > 0) {
											dos.writeUTF(pcl.get(this).dis.readUTF());
										}
									}
									
									
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

	public String get_State() {
		if(this.state.equals("free")) {
			return "free";
		}
		return "busy";
	}
	public void setState(String s) {
		this.state = s;
	}
	public String toString() {
		return getUserName() + "\t" + get_State();
	}
} 
