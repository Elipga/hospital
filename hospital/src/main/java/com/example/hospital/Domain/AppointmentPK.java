package com.example.hospital.Domain;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Exception.InvalidException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class AppointmentPK implements Serializable {
    private String collegeNumber;
    private String patientDni;
    @Future
    private LocalDate dateOfAppointment;
    private LocalTime timeOfAppointment;
    public AppointmentPK(String collegeNumber, String patientDni, LocalDate dateOfAppointment, LocalTime timeOfAppointment) throws InvalidException {
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
