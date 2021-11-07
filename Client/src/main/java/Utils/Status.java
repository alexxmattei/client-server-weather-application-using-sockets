package Utils;

public enum Status {
    DISCONNECTED(false),
    CONNECTED(true);

    Boolean status;

    Status(Boolean status) {
        this.status = status;
    }
}
