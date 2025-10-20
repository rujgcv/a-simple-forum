package com.fufunode.context;

public class BaseContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ROLE = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        USER_ID.set(id);
    }

    public static Long getCurrentId() {
        return USER_ID.get();
    }

    public static void setCurrentRole(String role) {
        USER_ROLE.set(role);
    }

    public static String getCurrentRole() {
        return USER_ROLE.get();
    }

    public static void removeCurrent() {
        USER_ID.remove();
        USER_ROLE.remove();
    }
}