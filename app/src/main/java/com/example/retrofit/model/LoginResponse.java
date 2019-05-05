package com.example.retrofit.model;

public class LoginResponse {
    private boolean error;
    private String message;
    private User user;


    public LoginResponse(boolean error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
