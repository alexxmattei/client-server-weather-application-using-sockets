package Utils;

public enum Cookie {
    STATELESS(false),
    AUTHENTICATED(true);

    Boolean cookie;

    Cookie(Boolean cookie) {
        this.cookie = cookie;
    }
}
