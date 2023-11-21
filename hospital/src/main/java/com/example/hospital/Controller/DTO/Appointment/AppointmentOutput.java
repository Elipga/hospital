package com.example.hospital.Controller.DTO.Appointment;

import com.example.hospital.Domain.Appointment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AppointmentOutput {
    private String collegeNumber;
    private String patientDni;
    private LocalDate dateOfAppointment;
    private LocalTime timeOfAppointment;
    public AppointmentOutput(String collegeNumber, String patientDni, LocalDate dateOfAppointment, LocalTime timeOfAppointment) {
        this.collegeNumber = collegeNumber;
        this.patientDni = patientDni;
        this.dateOfAppointment = dateOfAppointment;
        this.timeOfAppointment = timeOfAppointment;
    }
    public static AppointmentOutput getAppointment(Appointment appointment) {
        return new AppointmentOutput(appointment.getCollegeNumber(), appointment.getPatientDni(), appointment.getDateOfAppointment(),
                appointment.getTimeOfAppointment());
    }
}
