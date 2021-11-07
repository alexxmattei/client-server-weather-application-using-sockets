package Utils.Menus;

public class AuthMenu {

    private static AuthMenu authMenuInstance = null;

    public static AuthMenu getInstance() {
        if (authMenuInstance == null) {
            authMenuInstance = new AuthMenu();
        }
        return authMenuInstance;
    }

    public void displayLoginOptions() {
        System.out.println("Choose a role to log in with: ");
        System.out.println("1. Client");
        System.out.println("2. Admin");
        System.out.println("Choose the role you want by typing the option above!");
        return;
    }

}
