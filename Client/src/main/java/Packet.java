import Utils.Session;

import java.io.Serializable;

public class Packet implements Serializable {
    String message;
    Session session;

    public Packet(String message) {
        this.message = message;
    }

    public Packet(Session session) {
        this.session = session;
    }

    public Packet(String message, Session session) {
        this.message = message;
        this.session = session;
    }

    public String getSession() {
        return this.session.toString();
    }
}
