package Utils.Parser;

import Utils.Request.Request;
import Utils.Session.Session;

public class RequestParser {
    public Request parseRequest(String requestPacket) {
        String[] responseComponents = requestPacket.split(":");
        Session clientRole = Session.valueOf(responseComponents[0]);
        String clientPayload = responseComponents[1].trim();

        return new Request(clientRole, clientPayload);
    }
}
