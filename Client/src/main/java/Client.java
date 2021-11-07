import Utils.Auth.ApplicationLifetimeManager;
import Utils.Auth.AuthManager;
import Utils.Cookie;
import Utils.Menus.GeneralMenu;
import Utils.Status;
import Utils.Session;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Session role;
    private Cookie authState = Cookie.STATELESS;
    private Status status = Status.CONNECTED;
    ApplicationLifetimeManager appLifetimeManager = ApplicationLifetimeManager.getInstance();

    public Session validateAuthorization(String userInput) {
        userInput = userInput.toUpperCase();
        for (Session role : Session.values()) {
            if (userInput.equals(role.toString())) {
                return role;
            }
        }
        return Session.NONE;
    }

    public Client(Socket socket, String role) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.role = validateAuthorization(role);

            if (this.role.toString().equals("NONE")) {
                GeneralMenu.displayIncorrectMessagePrompt();
            } else {
                if (this.role == Session.CLIENT) {
                    GeneralMenu.displayClientOptions();
                } else if (this.role == Session.ADMIN) {
                    GeneralMenu.displayAdminOptions();
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // function used to send request from the client
    // requests are handled only once you specify a state
    // therefore you can only send requests to the server when logged in
    public void requestSender() {
        try {
            bufferedWriter.write(role.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            // continue to send requests as long as the server runs and the client socket is connected
            while (socket.isConnected()) {
                String request = scanner.nextLine();
                switch (request.toLowerCase()) {
                    case "weather":
                        GeneralMenu.displayWeatherPrompt();
                        break;
                    case "logout":
                        AuthManager.getInstance().setUserCredentials(Session.NONE);
                        GeneralMenu.displayLogoutPrompt();
                        return;
                    case "close":
                        GeneralMenu.displayApplicationOnClosePrompt();
                        ApplicationLifetimeManager.setIsApplicationRunning(false);
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    default:
                        if (role.toString().equals(Session.CLIENT.toString())) {
                            GeneralMenu.displayClientOptions();
                        } else if (role.toString().equals(Session.ADMIN.toString())) {
                            GeneralMenu.displayAdminOptions();
                        }
                        break;
                }
                bufferedWriter.write(role.toString() + ": " + request);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // listen to incoming messages from the server
    // dispatches a new thread so it does not block the client interface
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromServer;

                while (socket.isConnected()) {
                    try {
                        messageFromServer = bufferedReader.readLine();
                        System.out.println(messageFromServer);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    // method used to safely disconnect the client
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}