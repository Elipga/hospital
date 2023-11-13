package com.example.hospital.Domain;

import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private String patientId;
    @Id
    private LocalDate dateOfAppointment;
    @Id
    private LocalTime timeOfAppointment;


    public Appointment(String collegeNumber, String patientId, LocalDate dateOfAppointment, LocalTime timeOfAppointment) throws InvalidException {
        this.collegeNumber = collegeNumber;
        validateId(patientId);
        this.patientId = patientId;
        this.dateOfAppointment = dateOfAppointment;
        validatetimeOfAppointment(timeOfAppointment);
        this.timeOfAppointment = timeOfAppointment;
    }

    private void validateId(String id) throws InvalidException {
        if((id.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!id.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("ID must contain 8 numbers" +
                "and 1 letter");
    }

    private void validatetimeOfAppointment(LocalTime timeOfAppointment) throws InvalidException {
        //We supposed that time of appointment is each 15 mins
        if((timeOfAppointment.getMinute() != 00) && (timeOfAppointment.getMinute() != 15) &&
                (timeOfAppointment.getMinute() != 30) && (timeOfAppointment.getMinute() != 45))
            throw new InvalidException("Time appointment has to be at minute 00, 15, 30 or 45");
    }
}
