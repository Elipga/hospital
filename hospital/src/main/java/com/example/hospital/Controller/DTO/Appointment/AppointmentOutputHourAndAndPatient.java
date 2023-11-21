package com.example.hospital.Controller.DTO.Appointment;

import com.example.hospital.Domain.Appointment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AppointmentOutputHourAndAndPatient {

    private String patientDni;
    private LocalTime timeOfAppointment;

    public AppointmentOutputHourAndAndPatient(String patientDni, LocalTime timeOfAppointment) {
        this.patientDni = patientDni;
        this.timeOfAppointment = timeOfAppointment;
    }

    public static AppointmentOutputHourAndAndPatient getAppointment(Appointment appointment) {
        return new AppointmentOutputHourAndAndPatient(appointment.getPatientDni(), appointment.getTimeOfAppointment());
    }
}
