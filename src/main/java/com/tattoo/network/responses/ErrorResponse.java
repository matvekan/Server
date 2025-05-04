package com.tattoo.network.responses;

public class ErrorResponse implements Response {
    public ErrorResponse(String unknownCommande) {
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
