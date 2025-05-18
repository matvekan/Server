package com.tattoo.network.responses;

public class RegisterResponse implements Response {
    private final boolean success;
    private final String message;
    private final String createdLogin;

    public RegisterResponse(boolean success, String message, String createdLogin) {
        this.success = success;
        this.message = message;
        this.createdLogin = createdLogin;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getCreatedLogin() { return createdLogin; }
}