package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Appointment;
import com.example.hospital.Domain.Availability;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AvailabilityOutput {
    private String collegeNumber;
    private LocalDate day;
    private LocalTime hour;

    public AvailabilityOutput(String collegeNumber, LocalDate day, LocalTime hour) {
        this.collegeNumber = collegeNumber;
        this.day = day;
        this.hour = hour;
    }

    public static AvailabilityOutput getAvailability(Availability availability) {
        return new AvailabilityOutput(availability.getCollegeNumber(), availability.getDay(),
                availability.getHour());
    }
}
