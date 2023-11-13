package com.example.hospital.Domain;

import com.example.hospital.Controller.DTO.AvailabilityOutput;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;


@Generated
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "availabilities")
@IdClass(value = AvailabilityPK.class)

public class Availability {

    @Id
    private String collegeNumber;
    @Id
    private LocalDate day;
    @Id
    private LocalTime hour;



    public Availability(String collegeNumber, LocalDate day, LocalTime hour) {
        this.collegeNumber = collegeNumber;
        this.day = day;
        this.hour = hour;
    }
}
