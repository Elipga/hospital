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

    private String staffId;
    private String patientId;
    private LocalDate dateOfAppointment;
    private LocalTime timeOfAppointment;

    public AppointmentPK(String staffId, String patientId, LocalDate dateOfAppointment, LocalTime timeOfAppointment) {
        this.staffId = staffId;
        this.patientId = patientId;
        this.dateOfAppointment = dateOfAppointment;
        this.timeOfAppointment = timeOfAppointment;
    }

}
