package com.tattoo.network.commands;

import java.time.LocalDateTime;

public class BookingCommand extends Command {
    private Long userId;
    private Long masterId;
    private Long serviceId;
    private LocalDateTime dateTime;
    private String clientNotes;

    public BookingCommand(Long userId, Long masterId, Long serviceId,
                          LocalDateTime dateTime, String clientNotes) {
        super(Type.BOOK_APPOINTMENT);
        this.userId = userId;
        this.masterId = masterId;
        this.serviceId = serviceId;
        this.dateTime = dateTime;
        this.clientNotes = clientNotes;
    }

    // Геттеры
    public Long getUserId() { return userId; }
    public Long getMasterId() { return masterId; }
    public Long getServiceId() { return serviceId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getClientNotes() { return clientNotes; }
}