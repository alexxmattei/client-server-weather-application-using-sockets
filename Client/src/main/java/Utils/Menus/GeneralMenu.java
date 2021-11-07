package Utils.Menus;

public class GeneralMenu {
    public static void displayClientOptions() {
        System.out.println("1. To get a weather in a city type 'weather'");
        System.out.println("2. To log out please type 'logout'");
    }

    public static void displayAdminOptions() {
        System.out.println("Choose what operation you want: ");
        System.out.println("1. To get data about all cities type 'weather forecast'.");
        System.out.println("2. To change the dataset type 'update'.");
        System.out.println("3. To log out please type 'logout'");
    }

    public static void displayWeatherPrompt() {
        System.out.println("\nTo make a request for weather please type in the coordinates\n");
    }

    public static void displayLogoutPrompt() {
        System.out.println("\nLogged out!\n");
    }

    public static void displayApplicationOnClosePrompt() {
        System.out.println("\nApplication closed successfully!\n");
    }

    public static void displayIncorrectMessagePrompt() {
        System.out.println("\nPlease make sure that the role you have chosen matches the options above!\n");
    }
}
