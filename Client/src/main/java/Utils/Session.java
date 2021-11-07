package Utils;

public enum Session {
    NONE(0),
    CLIENT(1),
    ADMIN(2);

    int roleId;

    Session(int role) {
        roleId = role;
    }
}


