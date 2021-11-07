package Utils.Request;

import Utils.Session.Session;

public class Request {
    private Session role;
    private String message;

    public Request() {
        this.role = Session.NONE;
        this.message = "";
    }

    public Request(Session role, String message) {
        this.role = role;
        this.message = message;
    }

    public Session getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public void setRole(Session role) {
        this.role = role;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
