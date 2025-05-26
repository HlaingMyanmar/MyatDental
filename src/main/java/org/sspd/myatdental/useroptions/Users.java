package org.sspd.myatdental.useroptions;


import lombok.Getter;


@Getter
public enum Users {
    DR_AUNG("Dr. Aung", "aung123"),
    DR_MYINT("Thu Thu Zin", "12345"),
    ADMIN("Admin", "admin123");

    private final String displayName;
    private final String password;

    Users(String displayName, String password) {
        this.displayName = displayName;
        this.password = password;
    }

    @Override
    public String toString() {
        return displayName;
    }
}