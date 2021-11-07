import Utils.Auth.ApplicationLifetimeManager;
import Utils.Menus.AuthMenu;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        AuthMenu authMenu = AuthMenu.getInstance();
        Scanner scanner = new Scanner(System.in);
        authMenu.displayLoginOptions();
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        while(ApplicationLifetimeManager.getIsApplicationRunning()) {
            Client client = new Client(socket, username);
            client.listenForMessage();
            client.requestSender();
        }
    }
}