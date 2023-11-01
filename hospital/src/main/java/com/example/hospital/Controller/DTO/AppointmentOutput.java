package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Appointment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AppointmentOutput {
    @NotNull(message = "Staff ID is null")
    @NotEmpty(message = "Staff ID is null")
    private String staffId;
    @NotNull(message = "Patient ID is null")
    @NotEmpty(message = "Patient ID is null")
    private String patientId;

    @Future
    @NotNull(message = "Date of appointment is null")
    @NotEmpty(message = "Date of appointment is null")
    private LocalDate dateOfAppointment;

    @NotNull(message = "Time of appointment is null")
    @NotEmpty(message = "Time of appointment is null")
    private LocalTime timeOfAppointment;

    public AppointmentOutput(String staffId, String patientId, LocalDate dateOfAppointment, LocalTime timeOfAppointment) {
        this.staffId = staffId;
        this.patientId = patientId;
        this.dateOfAppointment = dateOfAppointment;
        this.timeOfAppointment = timeOfAppointment;
    }

    public static AppointmentOutput getAppointment(Appointment appointment) {
        return new AppointmentOutput(appointment.getStaffId(), appointment.getPatientId(), appointment.getDateOfAppointment(),
                appointment.getTimeOfAppointment());
    }
}
