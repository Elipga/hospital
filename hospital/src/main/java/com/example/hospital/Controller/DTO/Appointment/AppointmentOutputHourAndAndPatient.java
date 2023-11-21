package com.example.hospital.Controller.DTO.Appointment;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Domain.Appointment;
import com.example.hospital.Exception.InvalidException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AppointmentOutputHourAndAndPatient {

    @NotNull(message = "Patient DNI is null")
    @NotEmpty(message = "Patient DNI is null")
    private String patientDni;
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime timeOfAppointment;

    public AppointmentOutputHourAndAndPatient(String patientDni, LocalTime timeOfAppointment) throws InvalidException {
        Validation validation = new Validation();
        validation.validateDni(patientDni);
        this.patientDni = patientDni;
        validation.validateTime(timeOfAppointment);
        this.timeOfAppointment = timeOfAppointment;
    }

    public static AppointmentOutputHourAndAndPatient getAppointment(Appointment appointment) throws InvalidException {
        return new AppointmentOutputHourAndAndPatient(appointment.getPatientDni(), appointment.getTimeOfAppointment());
    }
}
