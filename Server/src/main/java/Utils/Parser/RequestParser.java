package Utils.Parser;

import Utils.Request.Request;
import Utils.Session.Session;

public class RequestParser {
    public Request parseRequest(String requestPacket) {
        String[] responseComponents = requestPacket.split(":");
        Session clientRole = Session.valueOf(responseComponents[0]);
        String clientPayload = "";
        if(responseComponents.length > 1) {
            clientPayload = responseComponents[1].trim();
        } else {
            clientPayload = "login request";
        }

        return new Request(clientRole, clientPayload);
    }
}
