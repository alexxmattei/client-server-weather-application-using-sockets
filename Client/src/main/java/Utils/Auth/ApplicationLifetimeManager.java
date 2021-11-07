package Utils.Auth;

public class ApplicationLifetimeManager {
    private static Boolean isApplicationRunning = true;
    private static ApplicationLifetimeManager authManagerInstance = null;

    public static ApplicationLifetimeManager getInstance() {
        if (authManagerInstance == null) {
            authManagerInstance = new ApplicationLifetimeManager();
        }
        return authManagerInstance;
    }

    private ApplicationLifetimeManager() {
        this.setIsApplicationRunning(true);
    }

    public static Boolean getIsApplicationRunning() {
        return isApplicationRunning;
    }

    public static void setIsApplicationRunning(Boolean isApplicationRunning) {
        ApplicationLifetimeManager.isApplicationRunning = isApplicationRunning;
    }

}
