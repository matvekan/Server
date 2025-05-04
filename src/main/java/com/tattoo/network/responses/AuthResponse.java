package com.tattoo.network.responses;

public class AuthResponse implements Response {
    private final boolean success;
    private final String message;

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override public boolean isSuccess() { return success; }
    @Override public String getMessage() { return message; }
}