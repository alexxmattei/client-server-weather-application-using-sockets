package Utils.Auth;

import Utils.Cookie;

// Thread safe AuthManager implementation
public class AuthManager {

    private static Cookie userCredentials = null;
    private static AuthManager authManagerInstance = null;

    public static AuthManager getInstance() {
        if(authManagerInstance == null) {
            authManagerInstance = new AuthManager();
        }
        return authManagerInstance;
    }

    private AuthManager() {
        this.setUserCredentials(Cookie.STATELESS);
    }

    public void setUserCredentials(Cookie userCredentials) {
        this.userCredentials = userCredentials;
    }

    public Cookie getUserCredentials() {
        return userCredentials;
    }
}
