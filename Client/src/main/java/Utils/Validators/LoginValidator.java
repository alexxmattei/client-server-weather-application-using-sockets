package Utils.Validators;

import Utils.Session;

public class LoginValidator {
    public static Boolean validateLoginAttempt(String userLoginAttempt) {
        return (userLoginAttempt.equals(Session.CLIENT.toString())) || (userLoginAttempt.equals(Session.ADMIN.toString()));
    }
}
