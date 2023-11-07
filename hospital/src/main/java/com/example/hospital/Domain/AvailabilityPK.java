package com.example.hospital.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class AvailabilityPK implements Serializable {

    private String collegeNumber;
    private LocalDate day;
    private LocalTime hour;

    public AvailabilityPK(String collegeNumber, LocalDate day, LocalTime hour) {
        this.collegeNumber = collegeNumber;
        this.day = day;
        this.hour = hour;
    }
}
