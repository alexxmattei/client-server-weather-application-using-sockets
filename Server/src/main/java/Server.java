import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//add in the client handler array a role property
//parse the message from the client and return per case message a response
public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        System.out.println("Server started successfully!");
    }

    public void startServer() {
        try {
            while(!serverSocket.isClosed()) {
                // this is a blocker method - it will wait for a client to connect
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {

        }
    }

    public void closeServerSocket() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
