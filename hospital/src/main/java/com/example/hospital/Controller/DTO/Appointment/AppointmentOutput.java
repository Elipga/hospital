package com.example.hospital.Controller.DTO.Appointment;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Domain.Appointment;
import com.example.hospital.Exception.InvalidException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AppointmentOutput {
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    @Pattern(regexp="^[0-9]+$", message = "College Number contains only numbers")
    private String collegeNumber;
    @NotNull(message = "Patient DNI is null")
    @NotEmpty(message = "Patient DNI is null")
    private String patientDni;
    @Future(message = "Must be a future date")
    private LocalDate dateOfAppointment;
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime timeOfAppointment;
    public AppointmentOutput(String collegeNumber, String patientDni, LocalDate dateOfAppointment, LocalTime timeOfAppointment) throws InvalidException {
        Validation validation = new Validation();
        validation.validateCNumber(collegeNumber);
        this.collegeNumber = collegeNumber;
        validation.validateDni(patientDni);
        this.patientDni = patientDni;
        this.dateOfAppointment = dateOfAppointment;
        validation.validateTime(timeOfAppointment);
        this.timeOfAppointment = timeOfAppointment;
    }
    public static AppointmentOutput getAppointment(Appointment appointment) throws InvalidException {
        return new AppointmentOutput(appointment.getCollegeNumber(), appointment.getPatientDni(), appointment.getDateOfAppointment(),
                appointment.getTimeOfAppointment());
    }
}
