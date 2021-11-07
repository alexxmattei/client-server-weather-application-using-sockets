import Models.WeatherForecast;
import Utils.Parser.MapJsonToFile;
import Utils.Parser.RequestParser;
import Utils.Request.Request;
import Utils.Session.Session;
import Utils.Weather.WeatherManager;
import Validators.CoordinatesValidator;

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
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        RequestParser requestParser = new RequestParser();

        while (socket.isConnected()) {
            try {
                // try for blocking operation
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
                System.out.println(messageFromClient);
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

                                boolean isOk = CoordinatesValidator.validateLatitude(latitudeRequest.getMessage());
                                while (!isOk) {
                                    broadcastMessage("Please make sure that the latitude value is in the range of [-90;90]");
                                    latitude = bufferedReader.readLine();
                                    latitudeRequest = requestParser.parseRequest(latitude);
                                    isOk = CoordinatesValidator.validateLatitude(latitudeRequest.getMessage());
                                }
                                requestOperation++;
                                broadcastMessage("Please type in the longitude");
                                String longitude = bufferedReader.readLine();
                                Request longitudeRequest = requestParser.parseRequest(longitude);
                                isOk = CoordinatesValidator.validateLongitude(longitudeRequest.getMessage());
                                while(!isOk) {
                                    broadcastMessage("Please make sure that the longitude value is in the range of [-180;180]");
                                    longitude = bufferedReader.readLine();
                                    longitudeRequest = requestParser.parseRequest(longitude);
                                    isOk = CoordinatesValidator.validateLongitude(longitudeRequest.getMessage());
                                }
                                requestOperation++;

                                Double clientRequestLatitude = Double.parseDouble(latitudeRequest.getMessage());
                                Double clientRequestLongitude = Double.parseDouble(longitudeRequest.getMessage());
                                WeatherForecast resultCity = WeatherManager.calculateClosestCity(clientRequestLatitude, clientRequestLongitude);

                                broadcastMessage("\nWeather in "+ resultCity.getCity() + " is " + resultCity.getTemperature().toString() + " degrees!\n");
                            } else {
                                broadcastMessage("If you want to quit type 'quit' \nIf you want to continue type 'continue'");

                                String userChoiceInput = bufferedReader.readLine();
                                Request userChoiceInputRequest = requestParser.parseRequest(userChoiceInput);
                                logRequest.setMessage(userChoiceInputRequest.getMessage().toLowerCase());
                                requestOperation = 0;
                            }

                        }
                        break;
                    case "weather forecast":
                        if(logRequest.getRole().equals(Session.ADMIN)) {
                            broadcastMessage("The current data is: \n" + WeatherManager.getAllCitiesResponse());
                        } else {
                            broadcastMessage("You do not have permission to access this feature\nThe role you are required to have is 'ADMIN' your current role is: " + logRequest.getRole().toString());
                        }
                        break;
                    case "update":
                        if(logRequest.getRole().equals(Session.ADMIN)) {
                            broadcastMessage("Enter the new dataset (copy the JSON in one line):");
                            String clientUpdateInput = bufferedReader.readLine();
                            Request clientUpdateRequest = requestParser.parseDataUpdateRequest(clientUpdateInput);
                            if(clientUpdateRequest.getMessage().equals("quit")) {
                                return;
                            }
                            MapJsonToFile.mapJsonToFile(clientUpdateRequest.getMessage());
                            broadcastMessage("Updated records!\n" + clientUpdateRequest.getMessage());
                        } else {
                            broadcastMessage("You do not have permission to access this feature\nThe role you are required to have is 'ADMIN' your current role is: " + logRequest.getRole().toString());
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
