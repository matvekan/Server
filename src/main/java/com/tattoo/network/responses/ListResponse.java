package com.tattoo.network.responses;

import java.util.List;

public class ListResponse<T> implements Response {
    private final boolean success;
    private final String message;
    private final List<T> data;

    public ListResponse(boolean success, String message, List<T> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    @Override public boolean isSuccess() { return success; }
    @Override public String getMessage() { return message; }
    public List<T> getData() { return data; }
}