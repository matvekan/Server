package com.tattoo.network.responses;

public class BookingResponse implements Response {
    private final boolean success;
    private final String message;
    private Long appointmentId;

    public BookingResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public BookingResponse(boolean success, String message, Long appointmentId) {
        this.success = success;
        this.message = message;
        this.appointmentId = appointmentId;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }
}