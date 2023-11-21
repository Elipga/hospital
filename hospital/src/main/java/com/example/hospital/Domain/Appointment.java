package com.example.hospital.Domain;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@IdClass(value = AppointmentPK.class)
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    private String collegeNumber;
    @Id
    private String patientDni;
    @Id
    @Future(message = "Must be a future date")
    private LocalDate dateOfAppointment;
    @Id
    private LocalTime timeOfAppointment;
    public Appointment(String collegeNumber, String patientDni, LocalDate dateOfAppointment, LocalTime timeOfAppointment) throws InvalidException {
        Validation validation = new Validation();
        validation.validateCNumber(collegeNumber);
        this.collegeNumber = collegeNumber;
        validation.validateDni(patientDni);
        this.patientDni = patientDni;
        this.dateOfAppointment = dateOfAppointment;
        validation.validateTime(timeOfAppointment);
        this.timeOfAppointment = timeOfAppointment;
    }
}
