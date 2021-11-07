import Utils.Auth.ApplicationLifetimeManager;
import Utils.Auth.AuthManager;
import Utils.Menus.AuthMenu;
import Utils.Session;
import Utils.Validators.LoginValidator;

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
            while(!LoginValidator.validateLoginAttempt(userChosenRole.toUpperCase())) {
                System.out.println("There is no such Authorization level as: " + userChosenRole);
                System.out.println("Please choose between 'admin' and 'client'!");
                userChosenRole = scanner.nextLine();
            }
            authManager.setUserCredentials(Session.valueOf(userChosenRole.toUpperCase()));

            while (authManager.getUserCredentials() != Session.NONE) {
                Client client = new Client(socket, userChosenRole);
                client.listenForMessage();
                client.requestSender();
            }
        }
    }
}