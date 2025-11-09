package com.codingshuttle.linkedInProject.ConnectionsService.auth;

public class AuthContextHolder {

    public static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    static void setCurrentUserId(Long userId){
        currentUserId.set(userId);
    }
    public static Long getCurrentUserId() {
        return currentUserId.get();
    }
    static void clear(){
        currentUserId.remove();
    }
}
