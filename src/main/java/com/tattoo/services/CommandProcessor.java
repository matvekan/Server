package com.tattoo.services;

import com.tattoo.models.Master;
import com.tattoo.models.Service;
import com.tattoo.network.commands.*;
import com.tattoo.network.responses.*;
import com.tattoo.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CommandProcessor {
    public Response process(Command command) {
        try {
            switch (command.getType()) {
                case AUTH:
                    AuthCommand authCmd = (AuthCommand) command;
                    boolean authSuccess = AuthService.authenticate(
                            authCmd.getLogin(),
                            authCmd.getPassword()
                    );
                    return new AuthResponse(
                            authSuccess,
                            authSuccess ? "Login successful" : "Invalid credentials"
                    );

                case BOOK_APPOINTMENT:
                    BookingCommand bookCmd = (BookingCommand) command;
                    return BookingService.bookAppointment(
                            bookCmd.getUserId(),
                            bookCmd.getMasterId(),
                            bookCmd.getServiceId(),
                            bookCmd.getDateTime(),
                            bookCmd.getClientNotes()
                    );

                case MASTER:
                    MasterCommand masterCmd = (MasterCommand) command;
                    switch (masterCmd.getAction()) {
                        case GET_ALL:
                            List<Master> masters = MasterService.getAllMasters();
                            return new ListResponse<>(true, "Masters retrieved", masters);

                        case GET_AVAILABLE_TIME:
                            List<LocalDateTime> slots = MasterService.getAvailableTimeSlots(
                                    masterCmd.getMasterId(),
                                    LocalDate.parse(masterCmd.getDate())
                            );
                            return new ListResponse<>(true, "Available slots", slots);
                    }
                    break;

                case SERVICE:
                    ServiceCommand serviceCmd = (ServiceCommand) command;
                    switch (serviceCmd.getAction()) {
                        case GET_ALL:
                            List<Service> services = ServiceService.getAllServices();
                            return new ListResponse<>(true, "Services retrieved", services);

                        case GET_BY_MASTER:
                            List<Service> masterServices = ServiceService.getServicesByMaster(
                                    serviceCmd.getMasterId()
                            );
                            return new ListResponse<>(true, "Master services retrieved", masterServices);
                    }
                    break;
            }

            return new ErrorResponse("Unknown command type");
        } catch (Exception e) {
            return new ErrorResponse("Server error: " + e.getMessage());
        }
    }
}