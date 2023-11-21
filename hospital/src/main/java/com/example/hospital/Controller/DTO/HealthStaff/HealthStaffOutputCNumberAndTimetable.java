package com.example.hospital.Controller.DTO.HealthStaff;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class HealthStaffOutputCNumberAndTimetable {
    private String collegeNumber;
    private LocalTime startingTime;
    private LocalTime endingTime;
    public HealthStaffOutputCNumberAndTimetable(String collegeNumber, LocalTime startingTime, LocalTime endingTime) {
        this.collegeNumber = collegeNumber;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }
    public static HealthStaffOutputCNumberAndTimetable getHealthStaff(String collegeNumber, HealthStaffUpdate healthStaffUpdate) {
        return new HealthStaffOutputCNumberAndTimetable(collegeNumber, healthStaffUpdate.getStartingTime(),
                healthStaffUpdate.getEndingTime());
    }
}
