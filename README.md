# lab3
**Chat Server** chat_server.java
The chat server should act as a message forwarder, and it should only forward messages from one client to
another. The jobs of the chat server are as follows: accept a socket connection from a new client; maintain a
list of existing clients’ name, their state (busy or available), and their socket; show the existing clients’ name
and their state to the new client; maintain a connection table with the list of peer connections; and forward
message from one client to another as per the connection table.

Note that the server should be able to accept new connection at any state, without waiting for input from
the existing clients. So, the server should be able to maintain the connection to all existing sockets, receive
packets from and send packets to the sockets, and poll for a new socket connection from a new client. You
can assume the server can support a maximum of 100 clients. You should implement the main class for the
chat server in a file called chat server.java.

Requirements:
- Accept a socket connection from new clients (up to 100 users) use threads because multiple clients
- Maintain a list of existing clients' name, state (busy or available), and their socket
- Show existing clients' name and their state to the new client (broadcast each time updated)
- Maintain a connection table with the list of peer connections
- Forward message from one client to another as per the connection table

**Chat Client** chat_client.java
When a new client enters the chat program, it should choose an available client from a list of clients to get
connected to and start the chat. A client can get out of the program at any time they want. This should
not affect the existing clients; but the server needs to remove the client’s name, state and update the peer
connection table. You should implement the main class for the chat client in a file called chat client.java.

See below for a sample output of the chat program. The clients are connected to the chat server sequentially
— Client 1; then, Client 2; and then, Client 3. The outputs are color-coded to distinguish the relationship
between peer connections (via server). Of course, you do not have to color-code your program output.

Requirements:
- Connect to chat_server using #IP and #port
- Prompt the user to enter their client's name
- Get server to send a list of clients (name, state) which they can connect to and start the chat.
- End chat at any point: server needs to remove name, state, and update list of clients (peer connection table)
