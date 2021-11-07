import Utils.Auth.ApplicationLifetimeManager;
import Utils.Auth.AuthManager;
import Utils.Menus.AuthMenu;
import Utils.Session;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        AuthMenu authMenu = AuthMenu.getInstance();
        AuthManager authManager = AuthManager.getInstance();
        Socket socket = new Socket("localhost", 1234);

        while (ApplicationLifetimeManager.getIsApplicationRunning()) {

            authMenu.displayLoginOptions();
            Scanner scanner = new Scanner(System.in);
            String userChosenRole = scanner.nextLine();
            authManager.setUserCredentials(Session.valueOf(userChosenRole.toUpperCase()));
            System.out.println(authManager.getUserCredentials());

            while (authManager.getUserCredentials() != Session.NONE) {
                Client client = new Client(socket, userChosenRole);
                client.listenForMessage();
                client.requestSender();
            }
        }
    }
}