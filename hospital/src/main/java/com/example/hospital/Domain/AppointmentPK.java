package com.example.hospital.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class AppointmentPK implements Serializable {

    private String collegeNumber;
    private String patientId;
    private LocalDate dateOfAppointment;
    private LocalTime timeOfAppointment;

    public AppointmentPK(String collegeNumber, String patientId, LocalDate dateOfAppointment, LocalTime timeOfAppointment) {
        this.collegeNumber = collegeNumber;
        this.patientId = patientId;
        this.dateOfAppointment = dateOfAppointment;
        this.timeOfAppointment = timeOfAppointment;
    }

}
