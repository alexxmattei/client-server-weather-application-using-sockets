import Utils.Auth.ApplicationLifetimeManager;
import Utils.Auth.AuthManager;
import Utils.Cookie;
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
                System.out.println("Please make sure that the role you have chosen matches the options above!");
            } else {
                if (this.role == Session.CLIENT) {
                    System.out.println("1. To get a weather in a city type 'weather'");
                    System.out.println("2. To log out please type 'logout'");
                } else if (this.role == Session.ADMIN) {
                    System.out.println("Choose what operation you want: ");
                    System.out.println("1. Change json dataset");
                    System.out.println("2. Get all cities");
                    System.out.println("3. To log out please type 'logout'");
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
                        System.out.println("To make a request for weather please type in the coordinates");
                        break;
                    case "logout":
                        AuthManager.getInstance().setUserCredentials(Session.NONE);
                        System.out.println("Logged out!");
                        return;
                    case "close":
                        System.out.println("Application closed successfully!");
                        ApplicationLifetimeManager.setIsApplicationRunning(false);
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    default:
                        if(role.toString().equals(Session.CLIENT.toString())) {
                            System.out.println("1. To get a weather in a city type 'weather'");
                            System.out.println("2. To log out please type 'logout'");
                        } else if(role.toString().equals(Session.ADMIN.toString())) {
                            System.out.println("Choose what operation you want: ");
                            System.out.println("1. Change json dataset");
                            System.out.println("2. Get all cities");
                            System.out.println("3. To log out please type 'logout'");
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
                        System.out.println("Response from your request: " + messageFromServer);
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