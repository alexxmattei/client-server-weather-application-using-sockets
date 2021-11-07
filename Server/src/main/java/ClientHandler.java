import Models.WeatherForecast;
import Utils.Parser.RequestParser;
import Utils.Request.Request;
import Utils.Weather.WeatherManager;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientAuthLevel;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientAuthLevel = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: You are now logged in as: " + clientAuthLevel);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                // try for blocking operation
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
                System.out.println(messageFromClient);
                RequestParser requestParser = new RequestParser();
                Request logRequest = requestParser.parseRequest(messageFromClient);
                String clientMessage = logRequest.getMessage();
                switch (clientMessage) {
                    case "weather":
                        int requestOperation = 0;
                        while (!logRequest.getMessage().equals("quit")) {
                            if (requestOperation < 2) {
                                broadcastMessage("Please type in the latitude");
                                String latitude = bufferedReader.readLine();
                                Request latitudeRequest = requestParser.parseRequest(latitude);
                                requestOperation++;
                                broadcastMessage("Please type in the longitude");
                                String longitude = bufferedReader.readLine();
                                Request longitudeRequest = requestParser.parseRequest(longitude);
                                requestOperation++;
                                Double clientRequestLatitude = Double.parseDouble(latitudeRequest.getMessage());
                                Double clientRequestLongitude = Double.parseDouble(longitudeRequest.getMessage());
                                WeatherForecast resultCity = WeatherManager.calculateClosestCity(clientRequestLatitude, clientRequestLongitude);
                                broadcastMessage("Weather in "+ resultCity.getCity() + " is " + resultCity.getTemperature().toString() + " degrees!");
                            } else {
                                broadcastMessage("If you want to quit type 'quit' \n If you want to continue type 'continue'");
                                String userChoiceInput = bufferedReader.readLine();
                                Request userChoiceInputRequest = requestParser.parseRequest(userChoiceInput);
                                logRequest.setMessage(userChoiceInputRequest.getMessage().toLowerCase());
                                requestOperation = 0;
                            }

                        }
                        break;
                    case "logout":
                        System.out.println("Client with role: " + logRequest.getRole() + " has now logged out!");
                        break;
                    default:
                        System.out.println("Client with role: " + logRequest.getRole() + " has sent the following request: " + logRequest.getMessage());
                        break;
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientAuthLevel + " has disconnected!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
