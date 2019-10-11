import java.io.*; 
import java.util.*; 
import java.net.*; 

// Server class 
public class chat_server 
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
        listOfUsers = new ArrayList<ClientHandler>(MAX_CLIENTS);
        
		// Infinite loop bc clients are connecting
		while (true) 
		{ 
			Socket s = null; 
			System.out.println("Waiting for a client...");
			try
			{ 
				if (listOfUsers.size() >= 100) {
					System.out.println("Maximum number of clients connected.");
                }
                // socket object to receive incoming client requests 
				s = ss.accept(); 
				System.out.println("Client accepted"); 
				
				// obtaining input and out streams 
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

				// new thread for each client, add to array list
				ClientHandler t = new ClientHandler(s, dis, dos); 
				listOfUsers.add(t);
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

	public String printList() {
		String s = "List of clients & states\n";
		for (ClientHandler e : listOfUsers) {
			s = s + e.getUserName() +"\t\t\t"+e.get_State()+"\n";
		}
		return s;
	}
} 

// ClientHandler class 
class ClientHandler extends Thread 
{ 
	// instance of server to access ArrayList
	chat_server myServer = new chat_server();
	final DataInputStream dis; 
	final DataOutputStream dos; 
    final Socket s; 

	boolean setUserName;
	boolean setTargetUser;

	String state;
	String userName;
	String targetUser;

	// peer connection list
	HashMap<ClientHandler, ClientHandler> pcl = new HashMap<>();
	
	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) 
	{ 
		this.s = s; 
		this.dis = dis; 
        this.dos = dos; 
		setUserName = false;
        setTargetUser = false;
        state = "free";
	} 

	@Override
	public void run() 
	{ 
		String received;
		// made a temp to see what was blocking user from writing in switch statement after Speak to User 
		String temp = "";
		while (true) 
		{ 
            try { 
				//  !!If user has not set their user name yet, set it
				if (setUserName == false) {
					dos.writeUTF("Enter client name: ");
					// receive the answer from client 
					received = dis.readUTF(); 
					userName = received;
					setUserName = true;
					//send list to users once a connection is established
					System.out.println(myServer.printList());
					dos.writeUTF(myServer.printList());
				}
				
				//  !!If user has not designated who they want to talk to, set their target user
				while(temp!="Exit")
				{
					if (setTargetUser == false) {
						synchronized(this) {

                        dos.writeUTF("Connect to which client?");
                        // receive the answer from client 
                        received = dis.readUTF();
						targetUser = received;

						// if (targetUser.equalsIgnoreCase("y")) {
						// 	this.chatRoom();
						// } else if (targetUser.equalsIgnoreCase("n")) {
						// 	// ask what they want to connect to then
						// 	dos.writeUTF("Connect to which client?");
						// 	received = dis.readUTF();
						// 	targetUser = received;
						// }
						setTargetUser = true;
                        // new if branch to search
                        boolean userFound = false;
                        while(!userFound)
                        {
                            //find user they want to connect to and check for name, state
                            for (ClientHandler e : myServer.listOfUsers) {
                                if(e.getUserName().equalsIgnoreCase(targetUser)) {
									userFound = true;
                                    if (e.get_State().equalsIgnoreCase("free")) {
										// System.out.println("flush time");
										// e.dos.flush();
										// System.out.println("wait time");
										// e.dis.wait();
										// System.out.println("wait good!");
										e.dos.writeUTF("Received request from " + userName +"\nConnect? 'y' or 'n'");
										e.dis.notify();
                                        //ask user to connect
                                        if(e.dis.readUTF().equalsIgnoreCase("y")) {
                                            // We are allowed to connect.
                                            pcl.put(this, e);
											pcl.put(e, this);
                                            //update targetUser
											e.setState("busy");
                                            this.setState("busy");
                                            dos.writeUTF("You are connected to " + targetUser);
                                            this.chatRoom();
										}
                                    }else {
                                        dos.writeUTF("User is busy.");
									}
									break;
                                } 
                            }
                            dos.writeUTF("User cannot be found");
                            break;
                        }						
				}
				}//end synchronized block
            }
			// }catch(InterruptedException e){
			// 	//catch block for thread sleep()
			// 	System.out.println("Shutdown timeout interrupted, shutting down immediately");
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

    public void chatRoom() {
        // add some breaks to this for they can leave eternal doom
        while(pcl.containsKey(this)) {
           // constantly read client
            try {
                if (dis.available() > 0) {
                    pcl.get(this).dos.writeUTF(dis.readUTF());
                }
                if (pcl.get(this).dis.available() > 0) {
                    dos.writeUTF(pcl.get(this).dis.readUTF());
                }
            }catch (IOException e) { 
				e.printStackTrace(); 
				System.out.println("Shutting down");
				//actually exit instead of infinite loop
				System.exit(0);
			} 

        }
    }


} 