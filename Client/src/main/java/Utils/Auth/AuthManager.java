package Utils.Auth;

import Utils.Session;

// Thread safe AuthManager implementation
public class AuthManager {

    private static Session userCredentials = null;
    private static AuthManager authManagerInstance = null;

    public static AuthManager getInstance() {
        if(authManagerInstance == null) {
            authManagerInstance = new AuthManager();
        }
        return authManagerInstance;
    }

    private AuthManager() {
        this.setUserCredentials(Session.NONE);
    }

    public void setUserCredentials(Session userCredentials) {
        this.userCredentials = userCredentials;
    }

    public Session getUserCredentials() {
        return userCredentials;
    }
}
