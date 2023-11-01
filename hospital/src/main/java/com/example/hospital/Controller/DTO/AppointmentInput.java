package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Appointment;
import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AppointmentInput {
    @NotNull(message = "Staff ID is null")
    @NotEmpty(message = "Staff ID is null")
    private String staffId;
    @NotNull(message = "Patient ID is null")
    @NotEmpty(message = "Patient ID is null")
    private String patientId;
    @Future
    private LocalDate dateOfAppointment;
    private LocalTime timeOfAppointment;

    public AppointmentInput(String staffId, String patientId, LocalDate dateOfAppointment, LocalTime timeOfAppointment) throws InvalidException {
        validateId(staffId);
        this.staffId = staffId;
        validateId(patientId);
        this.patientId = patientId;
        this.dateOfAppointment = dateOfAppointment;
        this.timeOfAppointment = timeOfAppointment;
    }

    private static void validateId(String id) throws InvalidException {
        if((id.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!id.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("ID must contain 8 numbers" +
                "and 1 letter");
    }

    public static Appointment getAppointment(AppointmentInput appointmentInput) throws InvalidException {
    return new Appointment(appointmentInput.getStaffId(), appointmentInput.getPatientId(),
            appointmentInput.getDateOfAppointment(), appointmentInput.getTimeOfAppointment());
    }
}
